package io.github.morgaroth.eventsmanger.evdev

import java.nio.ShortBuffer

import org.joda.time.DateTime

import scala.collection.mutable


object InputEvent {

  val types = mutable.Map.empty[Short, (String, String)]
  val values = mutable.Map.empty[Short, Map[Int, (String, String)]]

  def registerEvent(name: String, key: Short, humanName: String = "") = {
    if (types.contains(key)) {
      println(s"event type $key already in map old: ${types(key)} new: $name")
    }
    types += key -> (name, Some(humanName).filter(_.nonEmpty).getOrElse(name))
    values += key -> Map.empty
  }

  def registerEventType(name: String, key: Int, evType: Short, humanName: String = "") = {
    if (values(evType).contains(key)) {
      println(s"value $key already in map old: ${values(evType)(key)} new: $name")
    }
    values.update(evType, values(evType).updated(key, (name, Some(humanName).filter(_.nonEmpty).getOrElse(name))))
  }


  /**
    * size of the input_event struct in bytes.
    */
  val STRUCT_SIZE_BYTES = 24
  registerEvent("EV_SYN", 0x00, "syntetic")
  registerEvent("EV_KEY", 0x01, "key")
  registerEvent("EV_REL", 0x02)
  registerEvent("EV_ABS", 0x03)
  registerEvent("EV_MSC", 0x04, "mouse")
  registerEvent("EV_SW", 0x05)
  registerEvent("EV_LED", 0x11)
  registerEvent("EV_SND", 0x12)
  registerEvent("EV_REP", 0x14)
  registerEvent("EV_FF", 0x15)
  registerEvent("EV_PWR", 0x16)
  registerEvent("EV_FF_STATUS", 0x17)
  val EV_MAX: Short = 0x1f
  val EV_CNT = EV_MAX + 1
  registerEventType("SYN_REPORT", 0, 0x00, "syn space")
  registerEventType("SYN_CONFIG", 1, 0x00)
  registerEventType("SYN_MT_REPORT", 2, 0x00)

  registerEventType("KEY_RESERVED", 0, 0x01)
  registerEventType("KEY_ESC", 1, 0x01)
  registerEventType("KEY_1", 2, 0x01, "1")
  registerEventType("KEY_2", 3, 0x01, "2")
  registerEventType("KEY_3", 4, 0x01, "3")
  registerEventType("KEY_4", 5, 0x01, "4")
  registerEventType("KEY_5", 6, 0x01, "5")
  registerEventType("KEY_6", 7, 0x01)
  registerEventType("KEY_7", 8, 0x01)
  registerEventType("KEY_8", 9, 0x01)
  registerEventType("KEY_9", 10, 0x01)
  registerEventType("KEY_0", 11, 0x01)
  registerEventType("KEY_MINUS", 12, 0x01)
  registerEventType("KEY_EQUAL", 13, 0x01)
  registerEventType("KEY_BACKSPACE", 14, 0x01)
  registerEventType("KEY_TAB", 15, 0x01)
  registerEventType("KEY_Q", 16, 0x01)
  registerEventType("KEY_W", 17, 0x01)
  registerEventType("KEY_E", 18, 0x01)
  registerEventType("KEY_R", 19, 0x01)
  registerEventType("KEY_T", 20, 0x01)
  registerEventType("KEY_Y", 21, 0x01)
  registerEventType("KEY_U", 22, 0x01)
  registerEventType("KEY_I", 23, 0x01)
  registerEventType("KEY_O", 24, 0x01)
  registerEventType("KEY_P", 25, 0x01)
  registerEventType("KEY_LEFTBRACE", 26, 0x01)
  registerEventType("KEY_RIGHTBRACE", 27, 0x01)
  registerEventType("KEY_ENTER", 28, 0x01)
  registerEventType("KEY_LEFTCTRL", 29, 0x01)
  registerEventType("KEY_A", 30, 0x01)
  registerEventType("KEY_S", 31, 0x01)
  registerEventType("KEY_D", 32, 0x01)
  registerEventType("KEY_F", 33, 0x01)
  registerEventType("KEY_G", 34, 0x01)
  registerEventType("KEY_H", 35, 0x01)
  registerEventType("KEY_J", 36, 0x01)
  registerEventType("KEY_K", 37, 0x01)
  registerEventType("KEY_L", 38, 0x01)
  registerEventType("KEY_SEMICOLON", 39, 0x01)
  registerEventType("KEY_APOSTROPHE", 40, 0x01)
  registerEventType("KEY_GRAVE", 41, 0x01)
  registerEventType("KEY_LEFTSHIFT", 42, 0x01)
  registerEventType("KEY_BACKSLASH", 43, 0x01)
  registerEventType("KEY_Z", 44, 0x01)
  registerEventType("KEY_X", 45, 0x01)
  registerEventType("KEY_C", 46, 0x01)
  registerEventType("KEY_V", 47, 0x01)
  registerEventType("KEY_B", 48, 0x01)
  registerEventType("KEY_N", 49, 0x01)
  registerEventType("KEY_M", 50, 0x01)
  registerEventType("KEY_COMMA", 51, 0x01)
  registerEventType("KEY_DOT", 52, 0x01)
  registerEventType("KEY_SLASH", 53, 0x01)
  registerEventType("KEY_RIGHTSHIFT", 54, 0x01)
  registerEventType("KEY_KPASTERISK", 55, 0x01)
  registerEventType("KEY_LEFTALT", 56, 0x01)
  registerEventType("KEY_SPACE", 57, 0x01)
  registerEventType("KEY_CAPSLOCK", 58, 0x01)
  registerEventType("KEY_F1", 59, 0x01)
  registerEventType("KEY_F2", 60, 0x01)
  registerEventType("KEY_F3", 61, 0x01)
  registerEventType("KEY_F4", 62, 0x01)
  registerEventType("KEY_F5", 63, 0x01)
  registerEventType("KEY_F6", 64, 0x01)
  registerEventType("KEY_F7", 65, 0x01)
  registerEventType("KEY_F8", 66, 0x01)
  registerEventType("KEY_F9", 67, 0x01)
  registerEventType("KEY_F10", 68, 0x01)
  registerEventType("KEY_NUMLOCK", 69, 0x01)
  registerEventType("KEY_SCROLLLOCK", 70, 0x01)
  registerEventType("KEY_KP7", 71, 0x01)
  registerEventType("KEY_KP8", 72, 0x01)
  registerEventType("KEY_KP9", 73, 0x01)
  registerEventType("KEY_KPMINUS", 74, 0x01)
  registerEventType("KEY_KP4", 75, 0x01)
  registerEventType("KEY_KP5", 76, 0x01)
  registerEventType("KEY_KP6", 77, 0x01)
  registerEventType("KEY_KPPLUS", 78, 0x01)
  registerEventType("KEY_KP1", 79, 0x01)
  registerEventType("KEY_KP2", 80, 0x01)
  registerEventType("KEY_KP3", 81, 0x01)
  registerEventType("KEY_KP0", 82, 0x01)
  registerEventType("KEY_KPDOT", 83, 0x01)
  registerEventType("KEY_ZENKAKUHANKAKU", 85, 0x01)
  registerEventType("KEY_102ND", 86, 0x01)
  registerEventType("KEY_F11", 87, 0x01)
  registerEventType("KEY_F12", 88, 0x01)
  registerEventType("KEY_RO", 89, 0x01)
  registerEventType("KEY_KATAKANA", 90, 0x01)
  registerEventType("KEY_HIRAGANA", 91, 0x01)
  registerEventType("KEY_HENKAN", 92, 0x01)
  registerEventType("KEY_KATAKANAHIRAGANA", 93, 0x01)
  registerEventType("KEY_MUHENKAN", 94, 0x01)
  registerEventType("KEY_KPJPCOMMA", 95, 0x01)
  registerEventType("KEY_KPENTER", 96, 0x01)
  registerEventType("KEY_RIGHTCTRL", 97, 0x01)
  registerEventType("KEY_KPSLASH", 98, 0x01)
  registerEventType("KEY_SYSRQ", 99, 0x01)
  registerEventType("KEY_RIGHTALT", 100, 0x01)
  registerEventType("KEY_LINEFEED", 101, 0x01)
  registerEventType("KEY_HOME", 102, 0x01)
  registerEventType("KEY_UP", 103, 0x01)
  registerEventType("KEY_PAGEUP", 104, 0x01)
  registerEventType("KEY_LEFT", 105, 0x01)
  registerEventType("KEY_RIGHT", 106, 0x01)
  registerEventType("KEY_END", 107, 0x01)
  registerEventType("KEY_DOWN", 108, 0x01)
  registerEventType("KEY_PAGEDOWN", 109, 0x01)
  registerEventType("KEY_INSERT", 110, 0x01)
  registerEventType("KEY_DELETE", 111, 0x01)
  registerEventType("KEY_MACRO", 112, 0x01)
  registerEventType("KEY_MUTE", 113, 0x01)
  registerEventType("KEY_VOLUMEDOWN", 114, 0x01)
  registerEventType("KEY_VOLUMEUP", 115, 0x01)
  registerEventType("KEY_POWER", 116, 0x01)
  /* SC System Power Down */
  registerEventType("KEY_KPEQUAL", 117, 0x01)
  registerEventType("KEY_KPPLUSMINUS", 118, 0x01)
  registerEventType("KEY_PAUSE", 119, 0x01)
  registerEventType("KEY_SCALE", 120, 0x01)
  /* AL Compiz Scale (Expose, 0x01) */
  registerEventType("KEY_KPCOMMA", 121, 0x01)
  registerEventType("KEY_HANGEUL", 122, 0x01)
  //  val KEY_HANGUEL = KEY_HANGEUL
  registerEventType("KEY_HANJA", 123, 0x01)
  registerEventType("KEY_YEN", 124, 0x01)
  registerEventType("KEY_LEFTMETA", 125, 0x01)
  registerEventType("KEY_RIGHTMETA", 126, 0x01)
  registerEventType("KEY_COMPOSE", 127, 0x01)
  registerEventType("KEY_STOP", 128, 0x01)
  /* AC Stop */
  registerEventType("KEY_AGAIN", 129, 0x01)
  registerEventType("KEY_PROPS", 130, 0x01)
  /* AC Properties */
  registerEventType("KEY_UNDO", 131, 0x01)
  /* AC Undo */
  registerEventType("KEY_FRONT", 132, 0x01)
  registerEventType("KEY_COPY", 133, 0x01)
  /* AC Copy */
  registerEventType("KEY_OPEN", 134, 0x01)
  /* AC Open */
  registerEventType("KEY_PASTE", 135, 0x01)
  /* AC Paste */
  registerEventType("KEY_FIND", 136, 0x01)
  /* AC Search */
  registerEventType("KEY_CUT", 137, 0x01)
  /* AC Cut */
  registerEventType("KEY_HELP", 138, 0x01)
  /* AL Integrated Help Center */
  registerEventType("KEY_MENU", 139, 0x01)
  /* Menu (show menu, 0x01) */
  registerEventType("KEY_CALC", 140, 0x01)
  /* AL Calculator */
  registerEventType("KEY_SETUP", 141, 0x01)
  registerEventType("KEY_SLEEP", 142, 0x01)
  /* SC System Sleep */
  registerEventType("KEY_WAKEUP", 143, 0x01)
  /* System Wake Up */
  registerEventType("KEY_FILE", 144, 0x01)
  /* AL Local Machine Browser */
  registerEventType("KEY_SENDFILE", 145, 0x01)
  registerEventType("KEY_DELETEFILE", 146, 0x01)
  registerEventType("KEY_XFER", 147, 0x01)
  registerEventType("KEY_PROG1", 148, 0x01)
  registerEventType("KEY_PROG2", 149, 0x01)
  registerEventType("KEY_WWW", 150, 0x01)
  /* AL Internet Browser */
  registerEventType("KEY_MSDOS", 151, 0x01)
  registerEventType("KEY_COFFEE", 152, 0x01)
  /* AL Terminal Lock/Screensaver */
  //  val KEY_SCREENLOCK = KEY_COFFEE
  registerEventType("KEY_DIRECTION", 153, 0x01)
  registerEventType("KEY_CYCLEWINDOWS", 154, 0x01)
  registerEventType("KEY_MAIL", 155, 0x01)
  registerEventType("KEY_BOOKMARKS", 156, 0x01)
  /* AC Bookmarks */
  registerEventType("KEY_COMPUTER", 157, 0x01)
  registerEventType("KEY_BACK", 158, 0x01)
  /* AC Back */
  registerEventType("KEY_FORWARD", 159, 0x01)
  /* AC Forward */
  registerEventType("KEY_CLOSECD", 160, 0x01)
  registerEventType("KEY_EJECTCD", 161, 0x01)
  registerEventType("KEY_EJECTCLOSECD", 162, 0x01)
  registerEventType("KEY_NEXTSONG", 163, 0x01)
  registerEventType("KEY_PLAYPAUSE", 164, 0x01)
  registerEventType("KEY_PREVIOUSSONG", 165, 0x01)
  registerEventType("KEY_STOPCD", 166, 0x01)
  registerEventType("KEY_RECORD", 167, 0x01)
  registerEventType("KEY_REWIND", 168, 0x01)
  registerEventType("KEY_PHONE", 169, 0x01)
  /* Media Select Telephone */
  registerEventType("KEY_ISO", 170, 0x01)
  registerEventType("KEY_CONFIG", 171, 0x01)
  /*
                           * AL Consumer Control
                           * Configuration
                           */
  registerEventType("KEY_HOMEPAGE", 172, 0x01)
  /* AC Home */
  registerEventType("KEY_REFRESH", 173, 0x01)
  /* AC Refresh */
  registerEventType("KEY_EXIT", 174, 0x01)
  /* AC Exit */
  registerEventType("KEY_MOVE", 175, 0x01)
  registerEventType("KEY_EDIT", 176, 0x01)
  registerEventType("KEY_SCROLLUP", 177, 0x01)
  registerEventType("KEY_SCROLLDOWN", 178, 0x01)
  registerEventType("KEY_KPLEFTPAREN", 179, 0x01)
  registerEventType("KEY_KPRIGHTPAREN", 180, 0x01)
  registerEventType("KEY_NEW", 181, 0x01)
  /* AC New */
  registerEventType("KEY_REDO", 182, 0x01)
  /* AC Redo/Repeat */
  registerEventType("KEY_F13", 183, 0x01)
  registerEventType("KEY_F14", 184, 0x01)
  registerEventType("KEY_F15", 185, 0x01)
  registerEventType("KEY_F16", 186, 0x01)
  registerEventType("KEY_F17", 187, 0x01)
  registerEventType("KEY_F18", 188, 0x01)
  registerEventType("KEY_F19", 189, 0x01)
  registerEventType("KEY_F20", 190, 0x01)
  registerEventType("KEY_F21", 191, 0x01)
  registerEventType("KEY_F22", 192, 0x01)
  registerEventType("KEY_F23", 193, 0x01)
  registerEventType("KEY_F24", 194, 0x01)
  registerEventType("KEY_PLAYCD", 200, 0x01)
  registerEventType("KEY_PAUSECD", 201, 0x01)
  registerEventType("KEY_PROG3", 202, 0x01)
  registerEventType("KEY_PROG4", 203, 0x01)
  registerEventType("KEY_DASHBOARD", 204, 0x01)
  /* AL Dashboard */
  registerEventType("KEY_SUSPEND", 205, 0x01)
  registerEventType("KEY_CLOSE", 206, 0x01)
  /* AC Close */
  registerEventType("KEY_PLAY", 207, 0x01)
  registerEventType("KEY_FASTFORWARD", 208, 0x01)
  registerEventType("KEY_BASSBOOST", 209, 0x01)
  registerEventType("KEY_PRINT", 210, 0x01)
  /* AC Print */
  registerEventType("KEY_HP", 211, 0x01)
  registerEventType("KEY_CAMERA", 212, 0x01)
  registerEventType("KEY_SOUND", 213, 0x01)
  registerEventType("KEY_QUESTION", 214, 0x01)
  registerEventType("KEY_EMAIL", 215, 0x01)
  registerEventType("KEY_CHAT", 216, 0x01)
  registerEventType("KEY_SEARCH", 217, 0x01)
  registerEventType("KEY_CONNECT", 218, 0x01)
  registerEventType("KEY_FINANCE", 219, 0x01)
  /* AL Checkbook/Finance */
  registerEventType("KEY_SPORT", 220, 0x01)
  registerEventType("KEY_SHOP", 221, 0x01)
  registerEventType("KEY_ALTERASE", 222, 0x01)
  registerEventType("KEY_CANCEL", 223, 0x01)
  /* AC Cancel */
  registerEventType("KEY_BRIGHTNESSDOWN", 224, 0x01)
  registerEventType("KEY_BRIGHTNESSUP", 225, 0x01)
  registerEventType("KEY_MEDIA", 226, 0x01)
  registerEventType("KEY_SWITCHVIDEOMODE", 227, 0x01)
  /*
                               * Cycle between
                               * available video
                               * outputs
                               * (Monitor/LCD/TV
                               * -out/etc, 0x01)
                               */
  registerEventType("KEY_KBDILLUMTOGGLE", 228, 0x01)
  registerEventType("KEY_KBDILLUMDOWN", 229, 0x01)
  registerEventType("KEY_KBDILLUMUP", 230, 0x01)
  registerEventType("KEY_SEND", 231, 0x01)
  /* AC Send */
  registerEventType("KEY_REPLY", 232, 0x01)
  /* AC Reply */
  registerEventType("KEY_FORWARDMAIL", 233, 0x01)
  /* AC Forward Msg */
  registerEventType("KEY_SAVE", 234, 0x01)
  /* AC Save */
  registerEventType("KEY_DOCUMENTS", 235, 0x01)
  registerEventType("KEY_BATTERY", 236, 0x01)
  registerEventType("KEY_BLUETOOTH", 237, 0x01)
  registerEventType("KEY_WLAN", 238, 0x01)
  registerEventType("KEY_UWB", 239, 0x01)
  registerEventType("KEY_UNKNOWN", 240, 0x01)
  registerEventType("KEY_VIDEO_NEXT", 241, 0x01)
  /* drive next video source */
  registerEventType("KEY_VIDEO_PREV", 242, 0x01)
  /*
                             * drive previous video
                             * source
                             */
  registerEventType("KEY_BRIGHTNESS_CYCLE", 243, 0x01)
  /*
                               * brightness up, after
                               * max is min
                               */
  registerEventType("KEY_BRIGHTNESS_ZERO", 244, 0x01)
  /*
                               * brightness off, use
                               * ambient
                               */
  registerEventType("KEY_DISPLAY_OFF", 245, 0x01)
  /*
                             * display device to off
                             * state
                             */
  registerEventType("KEY_WIMAX", 246, 0x01)

  //  registerEventType("BTN_MISC", 0x100, 0x01)
  registerEventType("BTN_0", 0x100, 0x01)
  registerEventType("BTN_1", 0x101, 0x01)
  registerEventType("BTN_2", 0x102, 0x01)
  registerEventType("BTN_3", 0x103, 0x01)
  registerEventType("BTN_4", 0x104, 0x01)
  registerEventType("BTN_5", 0x105, 0x01)
  registerEventType("BTN_6", 0x106, 0x01)
  registerEventType("BTN_7", 0x107, 0x01)
  registerEventType("BTN_8", 0x108, 0x01)
  registerEventType("BTN_9", 0x109, 0x01)
  registerEventType("BTN_MOUSE|LEFT", 0x110, 0x01)
  //  registerEventType("BTN_LEFT", 0x110, 0x01)
  registerEventType("BTN_RIGHT", 0x111, 0x01)
  registerEventType("BTN_MIDDLE", 0x112, 0x01)
  registerEventType("BTN_SIDE", 0x113, 0x01)
  registerEventType("BTN_EXTRA", 0x114, 0x01)
  registerEventType("BTN_FORWARD", 0x115, 0x01)
  registerEventType("BTN_BACK", 0x116, 0x01)
  registerEventType("BTN_TASK", 0x117, 0x01)
  //  registerEventType("BTN_JOYSTICK", 0x120, 0x01)
  registerEventType("BTN_TRIGGER|JOYSTICK", 0x120, 0x01)
  registerEventType("BTN_THUMB", 0x121, 0x01)
  registerEventType("BTN_THUMB2", 0x122, 0x01)
  registerEventType("BTN_TOP", 0x123, 0x01)
  registerEventType("BTN_TOP2", 0x124, 0x01)
  registerEventType("BTN_PINKIE", 0x125, 0x01)
  registerEventType("BTN_BASE", 0x126, 0x01)
  registerEventType("BTN_BASE2", 0x127, 0x01)
  registerEventType("BTN_BASE3", 0x128, 0x01)
  registerEventType("BTN_BASE4", 0x129, 0x01)
  registerEventType("BTN_BASE5", 0x12a, 0x01)
  registerEventType("BTN_BASE6", 0x12b, 0x01)
  registerEventType("BTN_DEAD", 0x12f, 0x01)
  registerEventType("BTN_GAMEPAD|A", 0x130, 0x01)
  //  registerEventType("BTN_A", 0x130, 0x01)
  registerEventType("BTN_B", 0x131, 0x01)
  registerEventType("BTN_C", 0x132, 0x01)
  registerEventType("BTN_X", 0x133, 0x01)
  registerEventType("BTN_Y", 0x134, 0x01)
  registerEventType("BTN_Z", 0x135, 0x01)
  registerEventType("BTN_TL", 0x136, 0x01)
  registerEventType("BTN_TR", 0x137, 0x01)
  registerEventType("BTN_TL2", 0x138, 0x01)
  registerEventType("BTN_TR2", 0x139, 0x01)
  registerEventType("BTN_SELECT", 0x13a, 0x01)
  registerEventType("BTN_START", 0x13b, 0x01)
  registerEventType("BTN_MODE", 0x13c, 0x01)
  registerEventType("BTN_THUMBL", 0x13d, 0x01)
  registerEventType("BTN_THUMBR", 0x13e, 0x01)
  registerEventType("BTN_DIGI|TOOL_PEN", 0x140, 0x01)
  //  registerEventType("BTN_TOOL_PEN", 0x140, 0x01)
  registerEventType("BTN_TOOL_RUBBER", 0x141, 0x01)
  registerEventType("BTN_TOOL_BRUSH", 0x142, 0x01)
  registerEventType("BTN_TOOL_PENCIL", 0x143, 0x01)
  registerEventType("BTN_TOOL_AIRBRUSH", 0x144, 0x01)
  registerEventType("BTN_TOOL_FINGER", 0x145, 0x01)
  registerEventType("BTN_TOOL_MOUSE", 0x146, 0x01)
  registerEventType("BTN_TOOL_LENS", 0x147, 0x01)
  registerEventType("BTN_TOUCH", 0x14a, 0x01)
  registerEventType("BTN_STYLUS", 0x14b, 0x01)
  registerEventType("BTN_STYLUS2", 0x14c, 0x01)
  registerEventType("BTN_TOOL_DOUBLETAP", 0x14d, 0x01)
  registerEventType("BTN_TOOL_TRIPLETAP", 0x14e, 0x01)
  registerEventType("BTN_TOOL_QUADTAP", 0x14f, 0x01)
  /*
                               * Four fingers on
                               * trackpad
                               */
  registerEventType("BTN_WHEEL|GEAR_DOWN", 0x150, 0x01)
  //  registerEventType("BTN_GEAR_DOWN", 0x150, 0x01)
  registerEventType("BTN_GEAR_UP", 0x151, 0x01)
  registerEventType("KEY_OK", 0x160, 0x01)
  registerEventType("KEY_SELECT", 0x161, 0x01)
  registerEventType("KEY_GOTO", 0x162, 0x01)
  registerEventType("KEY_CLEAR", 0x163, 0x01)
  registerEventType("KEY_POWER2", 0x164, 0x01)
  registerEventType("KEY_OPTION", 0x165, 0x01)
  registerEventType("KEY_INFO", 0x166, 0x01)
  /* AL OEM Features/Tips/Tutorial */
  registerEventType("KEY_TIME", 0x167, 0x01)
  registerEventType("KEY_VENDOR", 0x168, 0x01)
  registerEventType("KEY_ARCHIVE", 0x169, 0x01)
  registerEventType("KEY_PROGRAM", 0x16a, 0x01)
  /* Media Select Program Guide */
  registerEventType("KEY_CHANNEL", 0x16b, 0x01)
  registerEventType("KEY_FAVORITES", 0x16c, 0x01)
  registerEventType("KEY_EPG", 0x16d, 0x01)
  registerEventType("KEY_PVR", 0x16e, 0x01)
  /* Media Select Home */
  registerEventType("KEY_MHP", 0x16f, 0x01)
  registerEventType("KEY_LANGUAGE", 0x170, 0x01)
  registerEventType("KEY_TITLE", 0x171, 0x01)
  registerEventType("KEY_SUBTITLE", 0x172, 0x01)
  registerEventType("KEY_ANGLE", 0x173, 0x01)
  registerEventType("KEY_ZOOM", 0x174, 0x01)
  registerEventType("KEY_MODE", 0x175, 0x01)
  registerEventType("KEY_KEYBOARD", 0x176, 0x01)
  registerEventType("KEY_SCREEN", 0x177, 0x01)
  registerEventType("KEY_PC", 0x178, 0x01)
  /* Media Select Computer */
  registerEventType("KEY_TV", 0x179, 0x01)
  /* Media Select TV */
  registerEventType("KEY_TV2", 0x17a, 0x01)
  /* Media Select Cable */
  registerEventType("KEY_VCR", 0x17b, 0x01)
  /* Media Select VCR */
  registerEventType("KEY_VCR2", 0x17c, 0x01)
  /* VCR Plus */
  registerEventType("KEY_SAT", 0x17d, 0x01)
  /* Media Select Satellite */
  registerEventType("KEY_SAT2", 0x17e, 0x01)
  registerEventType("KEY_CD", 0x17f, 0x01)
  /* Media Select CD */
  registerEventType("KEY_TAPE", 0x180, 0x01)
  /* Media Select Tape */
  registerEventType("KEY_RADIO", 0x181, 0x01)
  registerEventType("KEY_TUNER", 0x182, 0x01)
  /* Media Select Tuner */
  registerEventType("KEY_PLAYER", 0x183, 0x01)
  registerEventType("KEY_TEXT", 0x184, 0x01)
  registerEventType("KEY_DVD", 0x185, 0x01)
  /* Media Select DVD */
  registerEventType("KEY_AUX", 0x186, 0x01)
  registerEventType("KEY_MP3", 0x187, 0x01)
  registerEventType("KEY_AUDIO", 0x188, 0x01)
  registerEventType("KEY_VIDEO", 0x189, 0x01)
  registerEventType("KEY_DIRECTORY", 0x18a, 0x01)
  registerEventType("KEY_LIST", 0x18b, 0x01)
  registerEventType("KEY_MEMO", 0x18c, 0x01)
  /* Media Select Messages */
  registerEventType("KEY_CALENDAR", 0x18d, 0x01)
  registerEventType("KEY_RED", 0x18e, 0x01)
  registerEventType("KEY_GREEN", 0x18f, 0x01)
  registerEventType("KEY_YELLOW", 0x190, 0x01)
  registerEventType("KEY_BLUE", 0x191, 0x01)
  registerEventType("KEY_CHANNELUP", 0x192, 0x01)
  /* Channel Increment */
  registerEventType("KEY_CHANNELDOWN", 0x193, 0x01)
  /* Channel Decrement */
  registerEventType("KEY_FIRST", 0x194, 0x01)
  registerEventType("KEY_LAST", 0x195, 0x01)
  /* Recall Last */
  registerEventType("KEY_AB", 0x196, 0x01)
  registerEventType("KEY_NEXT", 0x197, 0x01)
  registerEventType("KEY_RESTART", 0x198, 0x01)
  registerEventType("KEY_SLOW", 0x199, 0x01)
  registerEventType("KEY_SHUFFLE", 0x19a, 0x01)
  registerEventType("KEY_BREAK", 0x19b, 0x01)
  registerEventType("KEY_PREVIOUS", 0x19c, 0x01)
  registerEventType("KEY_DIGITS", 0x19d, 0x01)
  registerEventType("KEY_TEEN", 0x19e, 0x01)
  registerEventType("KEY_TWEN", 0x19f, 0x01)
  registerEventType("KEY_VIDEOPHONE", 0x1a0, 0x01)
  /* Media Select Video Phone */
  registerEventType("KEY_GAMES", 0x1a1, 0x01)
  /* Media Select Games */
  registerEventType("KEY_ZOOMIN", 0x1a2, 0x01)
  /* AC Zoom In */
  registerEventType("KEY_ZOOMOUT", 0x1a3, 0x01)
  /* AC Zoom Out */
  registerEventType("KEY_ZOOMRESET", 0x1a4, 0x01)
  /* AC Zoom */
  registerEventType("KEY_WORDPROCESSOR", 0x1a5, 0x01)
  /* AL Word Processor */
  registerEventType("KEY_EDITOR", 0x1a6, 0x01)
  /* AL Text Editor */
  registerEventType("KEY_SPREADSHEET", 0x1a7, 0x01)
  /* AL Spreadsheet */
  registerEventType("KEY_GRAPHICSEDITOR", 0x1a8, 0x01)
  /* AL Graphics Editor */
  registerEventType("KEY_PRESENTATION", 0x1a9, 0x01)
  /* AL Presentation App */
  registerEventType("KEY_DATABASE", 0x1aa, 0x01)
  /* AL Database App */
  registerEventType("KEY_NEWS", 0x1ab, 0x01)
  /* AL Newsreader */
  registerEventType("KEY_VOICEMAIL", 0x1ac, 0x01)
  /* AL Voicemail */
  registerEventType("KEY_ADDRESSBOOK", 0x1ad, 0x01)
  /* AL Contacts/Address Book */
  registerEventType("KEY_MESSENGER", 0x1ae, 0x01)
  /* AL Instant Messaging */
  registerEventType("KEY_DISPLAYTOGGLE", 0x1af, 0x01)
  /*
                               * Turn display (LCD, 0x01) on
                               * and off
                               */
  registerEventType("KEY_SPELLCHECK", 0x1b0, 0x01)
  /* AL Spell Check */
  registerEventType("KEY_LOGOFF", 0x1b1, 0x01)
  /* AL Logoff */
  registerEventType("KEY_DOLLAR", 0x1b2, 0x01)
  registerEventType("KEY_EURO", 0x1b3, 0x01)
  registerEventType("KEY_FRAMEBACK", 0x1b4, 0x01)
  /*
                             * Consumer - transport
                             * controls
                             */
  registerEventType("KEY_FRAMEFORWARD", 0x1b5, 0x01)
  registerEventType("KEY_CONTEXT_MENU", 0x1b6, 0x01)
  /*
                               * GenDesc - system
                               * context menu
                               */
  registerEventType("KEY_MEDIA_REPEAT", 0x1b7, 0x01)
  /*
                               * Consumer - transport
                               * control
                               */
  registerEventType("KEY_DEL_EOL", 0x1c0, 0x01)
  registerEventType("KEY_DEL_EOS", 0x1c1, 0x01)
  registerEventType("KEY_INS_LINE", 0x1c2, 0x01)
  registerEventType("KEY_DEL_LINE", 0x1c3, 0x01)
  registerEventType("KEY_FN", 0x1d0, 0x01)
  registerEventType("KEY_FN_ESC", 0x1d1, 0x01)
  registerEventType("KEY_FN_F1", 0x1d2, 0x01)
  registerEventType("KEY_FN_F2", 0x1d3, 0x01)
  registerEventType("KEY_FN_F3", 0x1d4, 0x01)
  registerEventType("KEY_FN_F4", 0x1d5, 0x01)
  registerEventType("KEY_FN_F5", 0x1d6, 0x01)
  registerEventType("KEY_FN_F6", 0x1d7, 0x01)
  registerEventType("KEY_FN_F7", 0x1d8, 0x01)
  registerEventType("KEY_FN_F8", 0x1d9, 0x01)
  registerEventType("KEY_FN_F9", 0x1da, 0x01)
  registerEventType("KEY_FN_F10", 0x1db, 0x01)
  registerEventType("KEY_FN_F11", 0x1dc, 0x01)
  registerEventType("KEY_FN_F12", 0x1dd, 0x01)
  registerEventType("KEY_FN_1", 0x1de, 0x01)
  registerEventType("KEY_FN_2", 0x1df, 0x01)
  registerEventType("KEY_FN_D", 0x1e0, 0x01)
  registerEventType("KEY_FN_E", 0x1e1, 0x01)
  registerEventType("KEY_FN_F", 0x1e2, 0x01)
  registerEventType("KEY_FN_S", 0x1e3, 0x01)
  registerEventType("KEY_FN_B", 0x1e4, 0x01)
  registerEventType("KEY_BRL_DOT1", 0x1f1, 0x01)
  registerEventType("KEY_BRL_DOT2", 0x1f2, 0x01)
  registerEventType("KEY_BRL_DOT3", 0x1f3, 0x01)
  registerEventType("KEY_BRL_DOT4", 0x1f4, 0x01)
  registerEventType("KEY_BRL_DOT5", 0x1f5, 0x01)
  registerEventType("KEY_BRL_DOT6", 0x1f6, 0x01)
  registerEventType("KEY_BRL_DOT7", 0x1f7, 0x01)
  registerEventType("KEY_BRL_DOT8", 0x1f8, 0x01)
  registerEventType("KEY_BRL_DOT9", 0x1f9, 0x01)
  registerEventType("KEY_BRL_DOT10", 0x1fa, 0x01)
  registerEventType("KEY_NUMERIC_0", 0x200, 0x01)
  /*
                             * used by phones, remote
                             * controls,
                             */
  registerEventType("KEY_NUMERIC_1", 0x201, 0x01)
  /* and other keypads */
  registerEventType("KEY_NUMERIC_2", 0x202, 0x01)
  registerEventType("KEY_NUMERIC_3", 0x203, 0x01)
  registerEventType("KEY_NUMERIC_4", 0x204, 0x01)
  registerEventType("KEY_NUMERIC_5", 0x205, 0x01)
  registerEventType("KEY_NUMERIC_6", 0x206, 0x01)
  registerEventType("KEY_NUMERIC_7", 0x207, 0x01)
  registerEventType("KEY_NUMERIC_8", 0x208, 0x01)
  registerEventType("KEY_NUMERIC_9", 0x209, 0x01)
  registerEventType("KEY_NUMERIC_STAR", 0x20a, 0x01)
  registerEventType("KEY_NUMERIC_POUND", 0x20b, 0x01)
  /* We avoid low common keys in module aliases so they don't get huge. */
  //  val KEY_MIN_INTERESTING = KEY_MUTE
  registerEventType("KEY_MAX", 0x2ff, 0x01)
  registerEventType("KEY_CNT", 0x2ff + 1, 0x01)
  registerEventType("REL_X", 0x00, 0x02)
  registerEventType("REL_Y", 0x01, 0x02)
  registerEventType("REL_Z", 0x02, 0x02)
  registerEventType("REL_RX", 0x03, 0x02)
  registerEventType("REL_RY", 0x04, 0x02)
  registerEventType("REL_RZ", 0x05, 0x02)
  registerEventType("REL_HWHEEL", 0x06, 0x02)
  registerEventType("REL_DIAL", 0x07, 0x02)
  registerEventType("REL_WHEEL", 0x08, 0x02)
  registerEventType("REL_MISC", 0x09, 0x02)
  registerEventType("REL_MAX", 0x0f, 0x02)
  registerEventType("REL_CNT", 0x0f + 1, 0x02)
  registerEventType("ABS_X", 0x00, 0x03)
  registerEventType("ABS_Y", 0x01, 0x03)
  registerEventType("ABS_Z", 0x02, 0x03)
  registerEventType("ABS_RX", 0x03, 0x03)
  registerEventType("ABS_RY", 0x04, 0x03)
  registerEventType("ABS_RZ", 0x05, 0x03)
  registerEventType("ABS_THROTTLE", 0x06, 0x03)
  registerEventType("ABS_RUDDER", 0x07, 0x03)
  registerEventType("ABS_WHEEL", 0x08, 0x03)
  registerEventType("ABS_GAS", 0x09, 0x03)
  registerEventType("ABS_BRAKE", 0x0a, 0x03)
  registerEventType("ABS_HAT0X", 0x10, 0x03)
  registerEventType("ABS_HAT0Y", 0x11, 0x03)
  registerEventType("ABS_HAT1X", 0x12, 0x03)
  registerEventType("ABS_HAT1Y", 0x13, 0x03)
  registerEventType("ABS_HAT2X", 0x14, 0x03)
  registerEventType("ABS_HAT2Y", 0x15, 0x03)
  registerEventType("ABS_HAT3X", 0x16, 0x03)
  registerEventType("ABS_HAT3Y", 0x17, 0x03)
  registerEventType("ABS_PRESSURE", 0x18, 0x03)
  registerEventType("ABS_DISTANCE", 0x19, 0x03)
  registerEventType("ABS_TILT_X", 0x1a, 0x03)
  registerEventType("ABS_TILT_Y", 0x1b, 0x03)
  registerEventType("ABS_TOOL_WIDTH", 0x1c, 0x03)
  registerEventType("ABS_VOLUME", 0x20, 0x03)
  registerEventType("ABS_MISC", 0x28, 0x03)
  registerEventType("ABS_MT_TOUCH_MAJOR", 0x30, 0x03)
  /*
                               * Major axis of
                               * touching ellipse
                               */
  registerEventType("ABS_MT_TOUCH_MINOR", 0x31, 0x03)
  /*
                               * Minor axis (omit if
                               * circular, 0x03)
                               */
  registerEventType("ABS_MT_WIDTH_MAJOR", 0x32, 0x03)
  /*
                               * Major axis of
                               * approaching ellipse
                               */
  registerEventType("ABS_MT_WIDTH_MINOR", 0x33, 0x03)
  registerEventType("ABS_MT_ORIENTATION", 0x34, 0x03)
  /* Ellipse orientation */
  registerEventType("ABS_MT_POSITION_X", 0x35, 0x03)
  /*
                               * Center X ellipse
                               * position
                               */
  registerEventType("ABS_MT_POSITION_Y", 0x36, 0x03)
  /*
                               * Center Y ellipse
                               * position
                               */
  registerEventType("ABS_MT_TOOL_TYPE", 0x37, 0x03)
  /* Type of touching device */
  registerEventType("ABS_MT_BLOB_ID", 0x38, 0x03)
  /*
                             * Group a set of packets as
                             * a blob
                             */
  registerEventType("ABS_MT_TRACKING_ID", 0x39, 0x03)
  /*
                               * Unique ID of
                               * initiated contact
                               */
  registerEventType("ABS_MAX", 0x3f, 0x03)
  registerEventType("ABS_CNT", 0x3f + 1, 0x03)
  registerEventType("SW_LID", 0x00, 0x05)
  /* set = lid shut */
  registerEventType("SW_TABLET_MODE", 0x01, 0x05)
  /* set = tablet mode */
  registerEventType("SW_HEADPHONE_INSERT", 0x02, 0x05)
  /* set = inserted */
  registerEventType("SW_RFKILL_ALL", 0x03, 0x05)
  /*
                             * rfkill master switch,
                             * type "any" set = radio
                             * enabled
                             */
  //  val SW_RADIO = SW_RFKILL_ALL
  /* deprecated */
  registerEventType("SW_MICROPHONE_INSERT", 0x04, 0x05)
  registerEventType("SW_DOCK", 0x05, 0x05)
  /* set = plugged into dock */
  registerEventType("SW_LINEOUT_INSERT", 0x06, 0x05)
  registerEventType("SW_JACK_PHYSICAL_INSERT", 0x07, 0x05)
  /*
                                 * set = mechanical
                                 * switch set
                                 */
  registerEventType("SW_VIDEOOUT_INSERT", 0x08, 0x05)
  registerEventType("SW_MAX", 0x0f, 0x05)
  registerEventType("SW_CNT", 0x0f + 1, 0x05)

  registerEventType("MSC_SERIAL", 0x00, 0x04)
  registerEventType("MSC_PULSELED", 0x01, 0x04)
  registerEventType("MSC_GESTURE", 0x02, 0x04)
  registerEventType("MSC_RAW", 0x03, 0x04)
  registerEventType("MSC_SCAN", 0x04, 0x04)
  registerEventType("MSC_MAX", 0x07, 0x04)
  registerEventType("MSC_CNT", 0x07 + 1, 0x04)
  registerEventType("LED_NUML", 0x00, 0x11)
  registerEventType("LED_CAPSL", 0x01, 0x11)
  registerEventType("LED_SCROLLL", 0x02, 0x11)
  registerEventType("LED_COMPOSE", 0x03, 0x11)
  registerEventType("LED_KANA", 0x04, 0x11)
  registerEventType("LED_SLEEP", 0x05, 0x11)
  registerEventType("LED_SUSPEND", 0x06, 0x11)
  registerEventType("LED_MUTE", 0x07, 0x11)
  registerEventType("LED_MISC", 0x08, 0x11)
  registerEventType("LED_MAIL", 0x09, 0x11)
  registerEventType("LED_CHARGING", 0x0a, 0x11)
  registerEventType("LED_MAX", 0x0f, 0x11)
  registerEventType("LED_CNT", 0x0f + 1, 0x11)

  registerEventType("REP_DELAY", 0x00, 0x14)
  registerEventType("REP_PERIOD", 0x01, 0x14)
  //  registerEventType("REP_MAX", 0x01, 0x14)

  registerEventType("SND_CLICK", 0x00, 0x12)
  registerEventType("SND_BELL", 0x01, 0x12)
  registerEventType("SND_TONE", 0x02, 0x12)
  registerEventType("SND_MAX", 0x07, 0x12)
  registerEventType("SND_CNT", 0x07 + 1, 0x12)

  //  registerEventType("ID_BUS", 0, 00)
  //  registerEventType("ID_VENDOR", 1, 00)
  //  registerEventType("ID_PRODUCT", 2, 00)
  //  registerEventType("ID_VERSION", 3, 00)
  //  registerEventType("BUS_PCI", 0x01, 00)
  //  registerEventType("BUS_ISAPNP", 0x02, 00)
  //  registerEventType("BUS_USB", 0x03, 00)
  //  registerEventType("BUS_HIL", 0x04, 00)
  //  registerEventType("BUS_BLUETOOTH", 0x05, 00)
  //  registerEventType("BUS_VIRTUAL", 0x06, 00)
  //  registerEventType("BUS_ISA", 0x10, 00)
  //  registerEventType("BUS_I8042", 0x11, 00)
  //  registerEventType("BUS_XTKBD", 0x12, 00)
  //  registerEventType("BUS_RS232", 0x13, 00)
  //  registerEventType("BUS_GAMEPORT", 0x14, 00)
  //  registerEventType("BUS_PARPORT", 0x15, 00)
  //  registerEventType("BUS_AMIGA", 0x16, 00)
  //  registerEventType("BUS_ADB", 0x17, 00)
  //  registerEventType("BUS_I2C", 0x18, 00)
  //  registerEventType("BUS_HOST", 0x19, 00)
  //  registerEventType("BUS_GSC", 0x1A, 00)
  //  registerEventType("BUS_ATARI", 0x1B, 00)
  /*
   * MT_TOOL types
   */
  //  registerEventType("MT_TOOL_FINGER", 0, 00)
  //  registerEventType("MT_TOOL_PEN", 1, 00)
  /*
   * Values describing the status of a force-feedback effect
   */
  registerEventType("FF_STATUS_STOPPED", 0x00, 0x15)
  registerEventType("FF_STATUS_PLAYING", 0x01, 0x15)
  //  registerEventType("FF_STATUS_MAX", 0x01, 0x15)

  def parse(shortBuffer: ShortBuffer, source: String): InputEvent = {
    var a = 0
    var b = 0
    var c = 0
    var d = 0
    a = shortBuffer.get
    b = shortBuffer.get
    c = shortBuffer.get
    d = shortBuffer.get
    val time_sec = (d << 48) | (c << 32) | (b << 16) | a & 0xffff
    a = shortBuffer.get
    b = shortBuffer.get
    c = shortBuffer.get
    d = shortBuffer.get
    val time_usec = (d << 48) | (c << 32) | (b << 16) | a & 0xffff
    val kind = shortBuffer.get
    val code = shortBuffer.get
    c = shortBuffer.get
    d = shortBuffer.get
    val value = (d << 16) | c
    InputEvent(time_sec, time_usec, kind.toShort, code.toShort, value.toInt, source)
  }

  val keyboard = Map(
    1 -> "ESC", 2 -> "1", 3 -> "2", 4 -> "3", 5 -> "4", 6 -> "5", 7 -> "6", 8 -> "7", 9 -> "8", 10 -> "9",
    11 -> "0", 12 -> "-", 13 -> "=", 14 -> "bcksp", 15 -> "tab", 16 -> "q", 17 -> "w", 18 -> "e", 19 -> "r",
    20 -> "t", 21 -> "y", 22 -> "u", 23 -> "i", 24 -> "o", 25 -> "p", 26 -> "[", 27 -> "]", 28 -> "enter", 29 -> "Left Ctrl",
    30 -> "a", 31 -> "s", 32 -> "d", 33 -> "f", 34 -> "g", 35 -> "h", 36 -> "j", 37 -> "k", 38 -> "l", 39 -> ";",
    40 -> "'", 42 -> "L Shift", 43 -> "\"", 44 -> "z", 45 -> "x", 46 -> "c", 47 -> "v", 48 -> "b", 49 -> "n",
    50 -> "m", 51 -> ",", 56 -> "Left Alt", 57 -> "SPACE", 59 -> "F01",
    60 -> "F02", 61 -> "F03", 62 -> "F04", 63 -> "F05", 64 -> "F06", 65 -> "F07", 66 -> "F08", 67 -> "F09", 68 -> "F10",
    87 -> "F11", 88 -> "F12",
    100 -> "Right Alt", 103 -> "Up Arr", 105 -> "Left Arr", 106 -> "Right Arr", 108 -> "Down Arr",
    110 -> "INS", 111 -> "DEL",
    125 -> "Left Win", 126 -> "Right Win",
  )

  def eventDescription(ev: InputEvent): String = {
    import ev.{code, kind, source, value}
    if (kind == 0x01 && code <= 127) {
      val name = keyboard.getOrElse(code, code.toString)
      value match {
        case 1 => s"Key $name down"
        case 0 => s"Key $name up"
        case 2 => s"key $name press"
      }
    } else if (kind == 0x02 && code < 2) {
      s"Mouse ${
        if (code == 1) {
          if (value < 0) "up" else "down"
        } else {
          if (value < 0) "left" else "right"
        }
      }"
    } else if (kind == 0x02 && code == 8) {
      if (value < 0) "Scroll down" else "Scroll up"
    } else {
      f"$kind%d ${types.getOrElse(ev.kind, ("", s"unknown type: $kind"))._2}, code $code%3d (${values.getOrElse(code, Map.empty).getOrElse(kind, ("", s"unknown value: $kind"))._2}), value $value%2d ($source)"
    }
  }
}

case class InputEvent(time_sec: Long, time_usec: Long, kind: Short, code: Short, value: Int, source: String) {
  val millis = time_sec * 1000 + time_usec / 1000
  val ts = new DateTime(millis)

  override def toString: String = {
    f"Event: time $time_sec%d.$time_usec%06d (${ts.toString("HH:mm:ss")}), ${InputEvent.eventDescription(this)}."
  }
}
