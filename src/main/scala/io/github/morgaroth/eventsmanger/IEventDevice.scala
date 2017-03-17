package io.github.morgaroth.eventsmanger

/**
  * Represents configurable parameters of an input axis. set*() should affect the value in the device.
  *
  * Copyright (C) 2009 Giacomo Ferrari
  *
  * @author Giacomo Ferrari
  */
trait InputAxisParameters {
  def getValue: Int

  def setValue(value: Int)

  def getMin: Int

  def setMin(min: Int)

  def getMax: Int

  def setMax(max: Int)

  def getFuzz: Int

  def setFuzz(fuzz: Int)

  def getFlat: Int

  def setFlat(flat: Int)
}

trait InputListener {
  def event(e: InputEvent)
}

/**
  * Represents a connection to a Linux Evdev device.
  * For additional info, see input/input.txt and input/input-programming.txt in the Linux kernel Documentation.
  * IMPORTANT: If you want higher-level access for your joystick/pad/whatever, check com.dgis.input.evdev.devices
  * for useful drivers to make your life easier!
  * Copyright (C) 2009 Giacomo Ferrari
  *
  * @author Giacomo Ferrari
  */
trait IEventDevice {
  /**
    * @return The version of Evdev reported by the kernel.
    */
  def getEvdevVersion: Int

  /**
    * @return the bus ID of the attached device.
    */
  def getBusID: Short

  /**
    * @return the vendor ID of the attached device.
    */
  def getVendorID: Short

  /**
    * @return the product ID of the attached device.
    */
  def getProductID: Short

  /**
    * @return the version ID of the attached device.
    */
  def getVersionID: Short

  /**
    * @return the name of the attached device.
    */
  def getDeviceName: String

  /**
    * @return A mapping from device supported event types to list of supported event codes.
    */
  def getSupportedEvents: Map[Int, List[Int]]

  /**
    * Obtains the configurable parameters of an absolute axis (value, min, max, fuzz, flatspot) from the device.
    *
    * @param axis The axis number (an event code under event type 3 (abs)).
    * @return The parameters, or null if there was an error. Modifications to this object will be reflected in the device.
    */
  def getAxisParameters(axis: Int): InputAxisParameters

  /**
    * Adds an event listener to this device.
    * When an event is received from Evdev, all InputListeners registered
    * will be notified by a call to event().
    * If the listener is already on the listener list,
    * this method has no effect.
    *
    * @param list The listener to add. Must not be null.
    */
  def addListener(list: InputListener)

  /**
    * Removes an event listener to this device.
    * If the listener is not on the listener list,
    * this method has no effect.
    *
    * @param list The listener to remove. Must not be null.
    */
  def removeListener(list: InputListener)

  /**
    * Releases all resources held by this EventDevice. No more events will be generated.
    * It is impossible to restart an EventDevice once this method is called.
    */
  def close()
}
