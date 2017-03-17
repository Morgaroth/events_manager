package io.github.morgaroth.eventsmanger

import java.io.{FileInputStream, IOException}
import java.nio.{ByteBuffer, ByteOrder}

class SimpleEventDevice(val device: String) {

  private val inputBuffer = ByteBuffer.allocate(InputEvent.STRUCT_SIZE_BYTES)
  inputBuffer.order(ByteOrder.LITTLE_ENDIAN)

  var listeners = List.empty[InputListener]

  val fis = new FileInputStream(device)
  val deviceInput = fis.getChannel
  val terminate = false
  val readerThread = new Thread() {
    override def run() {
      while (!terminate) {
        val ev = readEvent
        distributeEvent(ev)
      }
    }
  }

  //    readerThread.setDaemon(true) /* We don't want this thread to prevent the JVM from terminating */
  readerThread.start()
  println("Started")

  private def distributeEvent(ev: InputEvent) {
    listeners synchronized {
      for (il <- listeners) {
        il.event(ev)
      }
    }
  }

  private def readEvent = try {
    /* Read exactly the amount of bytes specified by InputEvent.STRUCT_SIZE_BYTES (intrinsic size of inputBuffer)*/ inputBuffer.clear
    while (inputBuffer.hasRemaining) deviceInput.read(inputBuffer)
    /* We want to read now */ inputBuffer.flip
    /* Delegate parsing to InputEvent.parse() */ InputEvent.parse(inputBuffer.asShortBuffer, device)
  } catch {
    case _: IOException => null
  }

  def close() {
    try readerThread.join() catch {
      case e: InterruptedException =>
        e.printStackTrace()
    }
    try deviceInput.close() catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  def addListener(list: InputListener) {
    listeners synchronized {
      listeners :+ list
    }
  }

  def removeListener(list: InputListener) {
    listeners synchronized {
      listeners = listeners.filter(_ != list)
    }
  }

  def getDevicePath: String = device
}