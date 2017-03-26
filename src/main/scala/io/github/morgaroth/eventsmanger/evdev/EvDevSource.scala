package io.github.morgaroth.eventsmanger.evdev

import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.nio.{ByteBuffer, ByteOrder}

import akka.NotUsed
import akka.stream.scaladsl.Source

object EvDevSource {
  def apply(file: File): Source[(ByteBuffer, String), NotUsed] = Source.unfoldResource[(ByteBuffer, String), FileChannel](
    () => FileChannel.open(file.toPath, StandardOpenOption.READ), { chan =>
      val buf = ByteBuffer.allocateDirect(InputEvent.STRUCT_SIZE_BYTES)
      buf.order(ByteOrder.LITTLE_ENDIAN)
      chan.read(buf)
      buf.flip()
      Some((buf, file.getPath))
    }, _.close()
  )
}
