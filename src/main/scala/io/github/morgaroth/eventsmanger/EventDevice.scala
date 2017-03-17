package io.github.morgaroth.eventsmanger

import java.io.{FileInputStream, IOException}
import java.nio.channels.FileChannel
import java.nio.{ByteBuffer, ByteOrder}
import java.util

import ch.jodersky.jni.nativeLoader

import scala.collection.mutable

@nativeLoader("libEventsManager0")
class EventDevice(val device: String) extends IEventDevice {

  private val inputBuffer = ByteBuffer.allocate(InputEvent.STRUCT_SIZE_BYTES)
  inputBuffer.order(ByteOrder.LITTLE_ENDIAN)
  initDevice()
  /**
    * Notify these guys about input events.
    */
  var listeners = List.empty[InputListener]
  /**
    * Attached to device we're using.
    */
  private var deviceInput: FileChannel = _
  /**
    * When this is true, the reader thread should terminate ASAP.
    */
  private var terminate = false
  /**
    * This thread repeatedly calls readEvent().
    */
  private var readerThread: Thread = _
  private val idResponse = new Array[Short](4)
  private var evdevVersionResponse = 0
  private var deviceNameResponse: String = _
  /**
    * Maps supported event types (keys) to lists of supported event codes.
    */
  private var supportedEvents = Map.empty[Int, List[Int]]
  /**
    * Ensures only one instance of InputAxisParameters is created for each axis (more would be wasteful).
    */
  private var axisParams = Map.empty[Int, InputAxisParameters]

  private def initDevice() {
    if (!ioctlGetID(device, idResponse)) {
      System.err.println("WARN: couldn't get device ID: " + device)
      util.Arrays.fill(idResponse, 0.toShort)
    }
    evdevVersionResponse = ioctlGetEvdevVersion(device)
    val devName = Array.fill[Byte](255)(0)
    if (ioctlGetDeviceName(device, devName)) {
      deviceNameResponse = new String(devName)
    } else {
      System.err.println("WARN: couldn't get device name: " + device)
      deviceNameResponse = "Unknown Device"
    }
    readSupportedEvents()
    val fis = new FileInputStream(device)
    deviceInput = fis.getChannel
    readerThread = new Thread() {
      override def run() {
        while (!terminate) {
          val ev = readEvent
          distributeEvent(ev)
        }
      }
    }
    readerThread.setDaemon(true) /* We don't want this thread to prevent the JVM from terminating */
    readerThread.start()
  }

  /**
    * Get supported events from device, and place into supportedEvents.
    * Adapted from evtest.c.
    */
  private def readSupportedEvents() {
    //System.out.println("Detecting device capabilities...");
    val bit = Array.fill[Array[Long]](InputEvent.EV_MAX)(Array.fill[Long](NBITS(InputEvent.KEY_MAX))(0))
    ioctlEVIOCGBIT(device, bit(0), 0, bit(0).length)
    /* Loop over event types */ var i = 0
    for (i <- 0 until InputEvent.EV_MAX) {
      if (testBit(bit(0), i)) {
        /* Is this event supported? */
        //System.out.printf("  Event type %d\n", i);
        if (i != 0) {
          //todo: continue is not supported
          val supportedTypes = new mutable.ListBuffer[Int]
          ioctlEVIOCGBIT(device, bit(i), i, InputEvent.KEY_MAX)
          /* Loop over event codes for type */ var j = 0
          for (j <- 0 until InputEvent.KEY_MAX) {
            if (testBit(bit(i), j)) {
              /* Is this event code supported? */
              //System.out.printf("    Event code %d\n", j);
              supportedTypes += j
            }
          }
          supportedEvents += i -> supportedTypes.toList
        }
      }
    }
  }

  private def testBit(array: Array[Long], bit: Int) = ((array(LONG(bit)) >>> OFF(bit)) & 1) != 0

  private def LONG(x: Int) = x / 64

  private def OFF(x: Int) = x % 64

  private def NBITS(x: Int) = ((x - 1) / (8 * 8)) + 1

  /**
    * Distribute an event to all registered listeners.
    *
    * @param ev The event to distribute.
    */
  private def distributeEvent(ev: InputEvent) {
    listeners synchronized {
      for (il <- listeners) {
        il.event(ev)
      }
    }
  }

  /**
    * Obtain an InputEvent from the input channel. Delegate to InputEvent for parsing.
    *
    * @return
    */
  private def readEvent = try {
    /* Read exactly the amount of bytes specified by InputEvent.STRUCT_SIZE_BYTES (intrinsic size of inputBuffer)*/ inputBuffer.clear
    while (inputBuffer.hasRemaining) deviceInput.read(inputBuffer)
    /* We want to read now */ inputBuffer.flip
    /* Delegate parsing to InputEvent.parse() */ InputEvent.parse(inputBuffer.asShortBuffer, device)
  } catch {
    case e: IOException => null
  }

  /**
    * @see com.dgis.input.evdev.IEventDevice#close()
    */
  def close() {
    terminate = true
    try
      readerThread.join()

    catch {
      case e: InterruptedException =>
        e.printStackTrace()
    }
    try
      deviceInput.close()

    catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  /**
    * @see com.dgis.input.evdev.IEventDevice#getBusID()
    */
  def getBusID: Short = idResponse(InputEvent.ID_BUS)

  /**
    * @see com.dgis.input.evdev.IEventDevice#getDeviceName()
    */
  def getDeviceName: String = deviceNameResponse

  /**
    * @see com.dgis.input.evdev.IEventDevice#getProductID()
    */
  def getProductID: Short = {
    // TODO Auto-generated method stub
    idResponse(InputEvent.ID_PRODUCT)
  }

  /**
    * @see com.dgis.input.evdev.IEventDevice#getSupportedEvents()
    */
  def getSupportedEvents: Map[Int, List[Int]] = supportedEvents

  /**
    * @see com.dgis.input.evdev.IEventDevice#getVendorID()
    */
  def getVendorID: Short = idResponse(InputEvent.ID_VENDOR)

  /**
    * @see com.dgis.input.evdev.IEventDevice#getEvdevVersion()
    */
  def getEvdevVersion: Int = evdevVersionResponse

  /**
    * @see com.dgis.input.evdev.IEventDevice#getVersionID()
    */
  def getVersionID: Short = idResponse(InputEvent.ID_VERSION)

  def getAxisParameters(axis: Int): InputAxisParameters = {
    var params = axisParams.get(axis).orNull
    if (params == null) {
      params = new InputAxisParametersImpl(this, axis)
      axisParams += (axis -> params)
    }
    params
  }

  /**
    * @see com.dgis.input.evdev.IEventDevice#addListener(com.dgis.input.evdev.InputListener)
    */
  def addListener(list: InputListener) {
    listeners synchronized {
      listeners :+ list
    }
  }

  /**
    * @see com.dgis.input.evdev.IEventDevice#removeListener(com.dgis.input.evdev.InputListener)
    */
  def removeListener(list: InputListener) {
    listeners synchronized {
      listeners = listeners.filter(_ != list)
    }
  }

  def getDevicePath: String = device

  ////BEGIN JNI METHODS////
  @native def ioctlGetID(device: String, resp: Array[Short]): Boolean

  @native def ioctlGetEvdevVersion(device: String): Int

  @native def ioctlGetDeviceName(device: String, resp: Array[Byte]): Boolean

  @native def ioctlEVIOCGBIT(device: String, resp: Array[Long], start: Int, stop: Int): Boolean

  @native def ioctlEVIOCGABS(device: String, resp: Array[Int], axis: Int): Boolean
}