package io.github.morgaroth.eventsmanger

import java.io._
import java.nio.{ByteBuffer, ByteOrder}
import javafx.print.PageLayout

/*


https://github.com/progman32/evdev-java/blob/master/native/evdev-java.c
https://github.com/progman32/evdev-java/blob/master/src/com/dgis/input/evdev/EventDevice.java
http://stackoverflow.com/questions/3662368/dev-input-keyboard-format


 */


object EvCodes {
  val EV_SYN = 0x00
  val EV_KEY = 0x01
  val EV_REL = 0x02
  val EV_ABS = 0x03
  val EV_MSC = 0x04
  val EV_SW = 0x05
  val EV_LED = 0x11
  val EV_SND = 0x12
  val EV_REP = 0x14
  val EV_FF = 0x15
  val EV_PWR = 0x16
  val EV_FF_STATUS = 0x17
  val EV_MAX = 0x1f
}

/**
  * Created by mateusz on 3/14/17.
  */
object Main {

  // 2 bytes, Short
  def readShort(r: InputStreamReader): Short = {
    val ch1 = r.read()
    val ch2 = r.read()
    if ((ch1 | ch2) < 0) throw new EOFException
    //    ((ch1 << 8) + ch2).toShort
    ((ch2 << 8) + ch1).toShort
  }

  // 4 bytes, Int
  def readInt(r: InputStreamReader): Int = {
    val ch1 = r.read()
    val ch2 = r.read()
    val ch3 = r.read()
    val ch4 = r.read()
    if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException
    //    (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0)
    (ch4 & 255 << 24) + (ch3 & 255 << 16) + (ch2 & 255 << 8) + (ch1 & 255 << 0)
  }

  // 8 bytes, Long
  def readLong(r: InputStreamReader): Long = {
    val ch1 = r.read()
    val ch2 = r.read()
    val ch3 = r.read()
    val ch4 = r.read()
    val ch5 = r.read()
    val ch6 = r.read()
    val ch7 = r.read()
    val ch8 = r.read()
    if ((ch1 | ch2 | ch3 | ch4 | ch5 | ch6 | ch7 | ch8) < 0) throw new EOFException
    (ch8.toLong << 56) + ((ch7 & 255).toLong << 48) + ((ch6 & 255).toLong << 40) + ((ch5 & 255).toLong << 32) + ((ch4 & 255).toLong << 24) + ((ch3 & 255) << 16) + ((ch2 & 255) << 8) + ((ch1 & 255) << 0)
    //    (ch1.toLong << 56) + ((ch2  & 255).toLong << 48) + ((ch3  & 255).toLong << 40) + ((ch4  & 255).toLong << 32) + ((ch5  & 255).toLong << 24) + ((ch6  & 255) << 16) + ((ch7  & 255) << 8) + ((ch8  & 255) << 0)
  }

  def main(args: Array[String]): Unit = {
    try {
      val path1 = "/dev/input/event4"
      val path2 = "/dev/input/event5"
      println(s"Can read? ${new File(path1).canRead}")
      println(s"Can read? ${new File(path2).canRead}")
      val fis = new FileInputStream(path1)
      val channel = fis.getChannel
      while (true) {
        val inputBuffer = ByteBuffer.allocate(InputEvent.STRUCT_SIZE_BYTES)
        inputBuffer.order(ByteOrder.LITTLE_ENDIAN)
        for (i <- 0 until InputEvent.STRUCT_SIZE_BYTES) {
          channel.read(inputBuffer)
        }
        inputBuffer.flip()
        val ev = InputEvent.parse(inputBuffer.asShortBuffer(), "test")
        println(ev)
      }
    }
    catch {
      case t: IOException =>
        t.printStackTrace()
    }
  }

}
