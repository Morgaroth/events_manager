package io.github.morgaroth.eventsmanger

/**
  * Created by mateusz on 3/14/17.
  */
class InputAxisParametersImpl(var device: EventDevice, var axis: Int) extends InputAxisParameters {
  readStatus()
  private var value = 0
  private var min = 0
  private var max = 0
  private var fuzz = 0
  private var flat = 0

  /**
    * Repopulate values stored in this class with values read from the device.
    */
  private def readStatus() {
    val resp = new Array[Int](5)
    device.ioctlEVIOCGABS(device.device, resp, axis)
    value = resp(0)
    min = resp(1)
    max = resp(2)
    fuzz = resp(3)
    flat = resp(4)
  }

  /**
    * Repopulate values stored in the device with values read from this class.
    */
  private def writeStatus() {
    throw new Error("Not implemented yet!")
  }

  def getValue: Int = this synchronized {
    readStatus()
    return value
  }

  def setValue(value: Int) {
    this synchronized {
      this.value = value
      writeStatus()
    }
  }

  def getMin: Int = this synchronized {
    readStatus()
    return min
  }

  def setMin(min: Int) {
    this synchronized {
      this.min = min
      writeStatus()
    }
  }

  def getMax: Int = this synchronized {
    readStatus()
    return max
  }

  def setMax(max: Int) {
    this synchronized {
      this.max = max
      writeStatus()
    }
  }

  def getFuzz: Int = this synchronized {
    readStatus()
    return fuzz
  }

  def setFuzz(fuzz: Int) {
    this synchronized {
      this.fuzz = fuzz
      writeStatus()
    }
  }

  def getFlat: Int = this synchronized {
    readStatus()
    return flat
  }

  def setFlat(flat: Int) {
    this synchronized {
      this.flat = flat
      writeStatus()
    }
  }

  override def toString: String = "Value: " + getValue + " Min: " + getMin + " Max: " + getMax + " Fuzz: " + getFuzz + " Flat: " + getFlat
}
