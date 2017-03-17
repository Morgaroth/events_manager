package io.github.morgaroth.eventsmanger

import java.nio.ShortBuffer
import java.util.Locale


object InputEvent {
  /**
    * size of the input_event struct in bytes.
    */
  val STRUCT_SIZE_BYTES = 24
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
  val EV_MAX: Short = 0x1f
  val EV_CNT = EV_MAX + 1
  val SYN_REPORT = 0
  val SYN_CONFIG = 1
  val SYN_MT_REPORT = 2
  val KEY_RESERVED = 0
  val KEY_ESC = 1
  val KEY_1 = 2
  val KEY_2 = 3
  val KEY_3 = 4
  val KEY_4 = 5
  val KEY_5 = 6
  val KEY_6 = 7
  val KEY_7 = 8
  val KEY_8 = 9
  val KEY_9 = 10
  val KEY_0 = 11
  val KEY_MINUS = 12
  val KEY_EQUAL = 13
  val KEY_BACKSPACE = 14
  val KEY_TAB = 15
  val KEY_Q = 16
  val KEY_W = 17
  val KEY_E = 18
  val KEY_R = 19
  val KEY_T = 20
  val KEY_Y = 21
  val KEY_U = 22
  val KEY_I = 23
  val KEY_O = 24
  val KEY_P = 25
  val KEY_LEFTBRACE = 26
  val KEY_RIGHTBRACE = 27
  val KEY_ENTER = 28
  val KEY_LEFTCTRL = 29
  val KEY_A = 30
  val KEY_S = 31
  val KEY_D = 32
  val KEY_F = 33
  val KEY_G = 34
  val KEY_H = 35
  val KEY_J = 36
  val KEY_K = 37
  val KEY_L = 38
  val KEY_SEMICOLON = 39
  val KEY_APOSTROPHE = 40
  val KEY_GRAVE = 41
  val KEY_LEFTSHIFT = 42
  val KEY_BACKSLASH = 43
  val KEY_Z = 44
  val KEY_X = 45
  val KEY_C = 46
  val KEY_V = 47
  val KEY_B = 48
  val KEY_N = 49
  val KEY_M = 50
  val KEY_COMMA = 51
  val KEY_DOT = 52
  val KEY_SLASH = 53
  val KEY_RIGHTSHIFT = 54
  val KEY_KPASTERISK = 55
  val KEY_LEFTALT = 56
  val KEY_SPACE = 57
  val KEY_CAPSLOCK = 58
  val KEY_F1 = 59
  val KEY_F2 = 60
  val KEY_F3 = 61
  val KEY_F4 = 62
  val KEY_F5 = 63
  val KEY_F6 = 64
  val KEY_F7 = 65
  val KEY_F8 = 66
  val KEY_F9 = 67
  val KEY_F10 = 68
  val KEY_NUMLOCK = 69
  val KEY_SCROLLLOCK = 70
  val KEY_KP7 = 71
  val KEY_KP8 = 72
  val KEY_KP9 = 73
  val KEY_KPMINUS = 74
  val KEY_KP4 = 75
  val KEY_KP5 = 76
  val KEY_KP6 = 77
  val KEY_KPPLUS = 78
  val KEY_KP1 = 79
  val KEY_KP2 = 80
  val KEY_KP3 = 81
  val KEY_KP0 = 82
  val KEY_KPDOT = 83
  val KEY_ZENKAKUHANKAKU = 85
  val KEY_102ND = 86
  val KEY_F11 = 87
  val KEY_F12 = 88
  val KEY_RO = 89
  val KEY_KATAKANA = 90
  val KEY_HIRAGANA = 91
  val KEY_HENKAN = 92
  val KEY_KATAKANAHIRAGANA = 93
  val KEY_MUHENKAN = 94
  val KEY_KPJPCOMMA = 95
  val KEY_KPENTER = 96
  val KEY_RIGHTCTRL = 97
  val KEY_KPSLASH = 98
  val KEY_SYSRQ = 99
  val KEY_RIGHTALT = 100
  val KEY_LINEFEED = 101
  val KEY_HOME = 102
  val KEY_UP = 103
  val KEY_PAGEUP = 104
  val KEY_LEFT = 105
  val KEY_RIGHT = 106
  val KEY_END = 107
  val KEY_DOWN = 108
  val KEY_PAGEDOWN = 109
  val KEY_INSERT = 110
  val KEY_DELETE = 111
  val KEY_MACRO = 112
  val KEY_MUTE = 113
  val KEY_VOLUMEDOWN = 114
  val KEY_VOLUMEUP = 115
  val KEY_POWER = 116
  /* SC System Power Down */
  val KEY_KPEQUAL = 117
  val KEY_KPPLUSMINUS = 118
  val KEY_PAUSE = 119
  val KEY_SCALE = 120
  /* AL Compiz Scale (Expose) */
  val KEY_KPCOMMA = 121
  val KEY_HANGEUL = 122
  val KEY_HANGUEL = KEY_HANGEUL
  val KEY_HANJA = 123
  val KEY_YEN = 124
  val KEY_LEFTMETA = 125
  val KEY_RIGHTMETA = 126
  val KEY_COMPOSE = 127
  val KEY_STOP = 128
  /* AC Stop */
  val KEY_AGAIN = 129
  val KEY_PROPS = 130
  /* AC Properties */
  val KEY_UNDO = 131
  /* AC Undo */
  val KEY_FRONT = 132
  val KEY_COPY = 133
  /* AC Copy */
  val KEY_OPEN = 134
  /* AC Open */
  val KEY_PASTE = 135
  /* AC Paste */
  val KEY_FIND = 136
  /* AC Search */
  val KEY_CUT = 137
  /* AC Cut */
  val KEY_HELP = 138
  /* AL Integrated Help Center */
  val KEY_MENU = 139
  /* Menu (show menu) */
  val KEY_CALC = 140
  /* AL Calculator */
  val KEY_SETUP = 141
  val KEY_SLEEP = 142
  /* SC System Sleep */
  val KEY_WAKEUP = 143
  /* System Wake Up */
  val KEY_FILE = 144
  /* AL Local Machine Browser */
  val KEY_SENDFILE = 145
  val KEY_DELETEFILE = 146
  val KEY_XFER = 147
  val KEY_PROG1 = 148
  val KEY_PROG2 = 149
  val KEY_WWW = 150
  /* AL Internet Browser */
  val KEY_MSDOS = 151
  val KEY_COFFEE = 152
  /* AL Terminal Lock/Screensaver */
  val KEY_SCREENLOCK = KEY_COFFEE
  val KEY_DIRECTION = 153
  val KEY_CYCLEWINDOWS = 154
  val KEY_MAIL = 155
  val KEY_BOOKMARKS = 156
  /* AC Bookmarks */
  val KEY_COMPUTER = 157
  val KEY_BACK = 158
  /* AC Back */
  val KEY_FORWARD = 159
  /* AC Forward */
  val KEY_CLOSECD = 160
  val KEY_EJECTCD = 161
  val KEY_EJECTCLOSECD = 162
  val KEY_NEXTSONG = 163
  val KEY_PLAYPAUSE = 164
  val KEY_PREVIOUSSONG = 165
  val KEY_STOPCD = 166
  val KEY_RECORD = 167
  val KEY_REWIND = 168
  val KEY_PHONE = 169
  /* Media Select Telephone */
  val KEY_ISO = 170
  val KEY_CONFIG = 171
  /*
                           * AL Consumer Control
                           * Configuration
                           */
  val KEY_HOMEPAGE = 172
  /* AC Home */
  val KEY_REFRESH = 173
  /* AC Refresh */
  val KEY_EXIT = 174
  /* AC Exit */
  val KEY_MOVE = 175
  val KEY_EDIT = 176
  val KEY_SCROLLUP = 177
  val KEY_SCROLLDOWN = 178
  val KEY_KPLEFTPAREN = 179
  val KEY_KPRIGHTPAREN = 180
  val KEY_NEW = 181
  /* AC New */
  val KEY_REDO = 182
  /* AC Redo/Repeat */
  val KEY_F13 = 183
  val KEY_F14 = 184
  val KEY_F15 = 185
  val KEY_F16 = 186
  val KEY_F17 = 187
  val KEY_F18 = 188
  val KEY_F19 = 189
  val KEY_F20 = 190
  val KEY_F21 = 191
  val KEY_F22 = 192
  val KEY_F23 = 193
  val KEY_F24 = 194
  val KEY_PLAYCD = 200
  val KEY_PAUSECD = 201
  val KEY_PROG3 = 202
  val KEY_PROG4 = 203
  val KEY_DASHBOARD = 204
  /* AL Dashboard */
  val KEY_SUSPEND = 205
  val KEY_CLOSE = 206
  /* AC Close */
  val KEY_PLAY = 207
  val KEY_FASTFORWARD = 208
  val KEY_BASSBOOST = 209
  val KEY_PRINT = 210
  /* AC Print */
  val KEY_HP = 211
  val KEY_CAMERA = 212
  val KEY_SOUND = 213
  val KEY_QUESTION = 214
  val KEY_EMAIL = 215
  val KEY_CHAT = 216
  val KEY_SEARCH = 217
  val KEY_CONNECT = 218
  val KEY_FINANCE = 219
  /* AL Checkbook/Finance */
  val KEY_SPORT = 220
  val KEY_SHOP = 221
  val KEY_ALTERASE = 222
  val KEY_CANCEL = 223
  /* AC Cancel */
  val KEY_BRIGHTNESSDOWN = 224
  val KEY_BRIGHTNESSUP = 225
  val KEY_MEDIA = 226
  val KEY_SWITCHVIDEOMODE = 227
  /*
                               * Cycle between
                               * available video
                               * outputs
                               * (Monitor/LCD/TV
                               * -out/etc)
                               */
  val KEY_KBDILLUMTOGGLE = 228
  val KEY_KBDILLUMDOWN = 229
  val KEY_KBDILLUMUP = 230
  val KEY_SEND = 231
  /* AC Send */
  val KEY_REPLY = 232
  /* AC Reply */
  val KEY_FORWARDMAIL = 233
  /* AC Forward Msg */
  val KEY_SAVE = 234
  /* AC Save */
  val KEY_DOCUMENTS = 235
  val KEY_BATTERY = 236
  val KEY_BLUETOOTH = 237
  val KEY_WLAN = 238
  val KEY_UWB = 239
  val KEY_UNKNOWN = 240
  val KEY_VIDEO_NEXT = 241
  /* drive next video source */
  val KEY_VIDEO_PREV = 242
  /*
                             * drive previous video
                             * source
                             */
  val KEY_BRIGHTNESS_CYCLE = 243
  /*
                               * brightness up, after
                               * max is min
                               */
  val KEY_BRIGHTNESS_ZERO = 244
  /*
                               * brightness off, use
                               * ambient
                               */
  val KEY_DISPLAY_OFF = 245
  /*
                             * display device to off
                             * state
                             */
  val KEY_WIMAX = 246
  val BTN_MISC = 0x100
  val BTN_0 = 0x100
  val BTN_1 = 0x101
  val BTN_2 = 0x102
  val BTN_3 = 0x103
  val BTN_4 = 0x104
  val BTN_5 = 0x105
  val BTN_6 = 0x106
  val BTN_7 = 0x107
  val BTN_8 = 0x108
  val BTN_9 = 0x109
  val BTN_MOUSE = 0x110
  val BTN_LEFT = 0x110
  val BTN_RIGHT = 0x111
  val BTN_MIDDLE = 0x112
  val BTN_SIDE = 0x113
  val BTN_EXTRA = 0x114
  val BTN_FORWARD = 0x115
  val BTN_BACK = 0x116
  val BTN_TASK = 0x117
  val BTN_JOYSTICK = 0x120
  val BTN_TRIGGER = 0x120
  val BTN_THUMB = 0x121
  val BTN_THUMB2 = 0x122
  val BTN_TOP = 0x123
  val BTN_TOP2 = 0x124
  val BTN_PINKIE = 0x125
  val BTN_BASE = 0x126
  val BTN_BASE2 = 0x127
  val BTN_BASE3 = 0x128
  val BTN_BASE4 = 0x129
  val BTN_BASE5 = 0x12a
  val BTN_BASE6 = 0x12b
  val BTN_DEAD = 0x12f
  val BTN_GAMEPAD = 0x130
  val BTN_A = 0x130
  val BTN_B = 0x131
  val BTN_C = 0x132
  val BTN_X = 0x133
  val BTN_Y = 0x134
  val BTN_Z = 0x135
  val BTN_TL = 0x136
  val BTN_TR = 0x137
  val BTN_TL2 = 0x138
  val BTN_TR2 = 0x139
  val BTN_SELECT = 0x13a
  val BTN_START = 0x13b
  val BTN_MODE = 0x13c
  val BTN_THUMBL = 0x13d
  val BTN_THUMBR = 0x13e
  val BTN_DIGI = 0x140
  val BTN_TOOL_PEN = 0x140
  val BTN_TOOL_RUBBER = 0x141
  val BTN_TOOL_BRUSH = 0x142
  val BTN_TOOL_PENCIL = 0x143
  val BTN_TOOL_AIRBRUSH = 0x144
  val BTN_TOOL_FINGER = 0x145
  val BTN_TOOL_MOUSE = 0x146
  val BTN_TOOL_LENS = 0x147
  val BTN_TOUCH = 0x14a
  val BTN_STYLUS = 0x14b
  val BTN_STYLUS2 = 0x14c
  val BTN_TOOL_DOUBLETAP = 0x14d
  val BTN_TOOL_TRIPLETAP = 0x14e
  val BTN_TOOL_QUADTAP = 0x14f
  /*
                               * Four fingers on
                               * trackpad
                               */
  val BTN_WHEEL = 0x150
  val BTN_GEAR_DOWN = 0x150
  val BTN_GEAR_UP = 0x151
  val KEY_OK = 0x160
  val KEY_SELECT = 0x161
  val KEY_GOTO = 0x162
  val KEY_CLEAR = 0x163
  val KEY_POWER2 = 0x164
  val KEY_OPTION = 0x165
  val KEY_INFO = 0x166
  /* AL OEM Features/Tips/Tutorial */
  val KEY_TIME = 0x167
  val KEY_VENDOR = 0x168
  val KEY_ARCHIVE = 0x169
  val KEY_PROGRAM = 0x16a
  /* Media Select Program Guide */
  val KEY_CHANNEL = 0x16b
  val KEY_FAVORITES = 0x16c
  val KEY_EPG = 0x16d
  val KEY_PVR = 0x16e
  /* Media Select Home */
  val KEY_MHP = 0x16f
  val KEY_LANGUAGE = 0x170
  val KEY_TITLE = 0x171
  val KEY_SUBTITLE = 0x172
  val KEY_ANGLE = 0x173
  val KEY_ZOOM = 0x174
  val KEY_MODE = 0x175
  val KEY_KEYBOARD = 0x176
  val KEY_SCREEN = 0x177
  val KEY_PC = 0x178
  /* Media Select Computer */
  val KEY_TV = 0x179
  /* Media Select TV */
  val KEY_TV2 = 0x17a
  /* Media Select Cable */
  val KEY_VCR = 0x17b
  /* Media Select VCR */
  val KEY_VCR2 = 0x17c
  /* VCR Plus */
  val KEY_SAT = 0x17d
  /* Media Select Satellite */
  val KEY_SAT2 = 0x17e
  val KEY_CD = 0x17f
  /* Media Select CD */
  val KEY_TAPE = 0x180
  /* Media Select Tape */
  val KEY_RADIO = 0x181
  val KEY_TUNER = 0x182
  /* Media Select Tuner */
  val KEY_PLAYER = 0x183
  val KEY_TEXT = 0x184
  val KEY_DVD = 0x185
  /* Media Select DVD */
  val KEY_AUX = 0x186
  val KEY_MP3 = 0x187
  val KEY_AUDIO = 0x188
  val KEY_VIDEO = 0x189
  val KEY_DIRECTORY = 0x18a
  val KEY_LIST = 0x18b
  val KEY_MEMO = 0x18c
  /* Media Select Messages */
  val KEY_CALENDAR = 0x18d
  val KEY_RED = 0x18e
  val KEY_GREEN = 0x18f
  val KEY_YELLOW = 0x190
  val KEY_BLUE = 0x191
  val KEY_CHANNELUP = 0x192
  /* Channel Increment */
  val KEY_CHANNELDOWN = 0x193
  /* Channel Decrement */
  val KEY_FIRST = 0x194
  val KEY_LAST = 0x195
  /* Recall Last */
  val KEY_AB = 0x196
  val KEY_NEXT = 0x197
  val KEY_RESTART = 0x198
  val KEY_SLOW = 0x199
  val KEY_SHUFFLE = 0x19a
  val KEY_BREAK = 0x19b
  val KEY_PREVIOUS = 0x19c
  val KEY_DIGITS = 0x19d
  val KEY_TEEN = 0x19e
  val KEY_TWEN = 0x19f
  val KEY_VIDEOPHONE = 0x1a0
  /* Media Select Video Phone */
  val KEY_GAMES = 0x1a1
  /* Media Select Games */
  val KEY_ZOOMIN = 0x1a2
  /* AC Zoom In */
  val KEY_ZOOMOUT = 0x1a3
  /* AC Zoom Out */
  val KEY_ZOOMRESET = 0x1a4
  /* AC Zoom */
  val KEY_WORDPROCESSOR = 0x1a5
  /* AL Word Processor */
  val KEY_EDITOR = 0x1a6
  /* AL Text Editor */
  val KEY_SPREADSHEET = 0x1a7
  /* AL Spreadsheet */
  val KEY_GRAPHICSEDITOR = 0x1a8
  /* AL Graphics Editor */
  val KEY_PRESENTATION = 0x1a9
  /* AL Presentation App */
  val KEY_DATABASE = 0x1aa
  /* AL Database App */
  val KEY_NEWS = 0x1ab
  /* AL Newsreader */
  val KEY_VOICEMAIL = 0x1ac
  /* AL Voicemail */
  val KEY_ADDRESSBOOK = 0x1ad
  /* AL Contacts/Address Book */
  val KEY_MESSENGER = 0x1ae
  /* AL Instant Messaging */
  val KEY_DISPLAYTOGGLE = 0x1af
  /*
                               * Turn display (LCD) on
                               * and off
                               */
  val KEY_SPELLCHECK = 0x1b0
  /* AL Spell Check */
  val KEY_LOGOFF = 0x1b1
  /* AL Logoff */
  val KEY_DOLLAR = 0x1b2
  val KEY_EURO = 0x1b3
  val KEY_FRAMEBACK = 0x1b4
  /*
                             * Consumer - transport
                             * controls
                             */
  val KEY_FRAMEFORWARD = 0x1b5
  val KEY_CONTEXT_MENU = 0x1b6
  /*
                               * GenDesc - system
                               * context menu
                               */
  val KEY_MEDIA_REPEAT = 0x1b7
  /*
                               * Consumer - transport
                               * control
                               */
  val KEY_DEL_EOL = 0x1c0
  val KEY_DEL_EOS = 0x1c1
  val KEY_INS_LINE = 0x1c2
  val KEY_DEL_LINE = 0x1c3
  val KEY_FN = 0x1d0
  val KEY_FN_ESC = 0x1d1
  val KEY_FN_F1 = 0x1d2
  val KEY_FN_F2 = 0x1d3
  val KEY_FN_F3 = 0x1d4
  val KEY_FN_F4 = 0x1d5
  val KEY_FN_F5 = 0x1d6
  val KEY_FN_F6 = 0x1d7
  val KEY_FN_F7 = 0x1d8
  val KEY_FN_F8 = 0x1d9
  val KEY_FN_F9 = 0x1da
  val KEY_FN_F10 = 0x1db
  val KEY_FN_F11 = 0x1dc
  val KEY_FN_F12 = 0x1dd
  val KEY_FN_1 = 0x1de
  val KEY_FN_2 = 0x1df
  val KEY_FN_D = 0x1e0
  val KEY_FN_E = 0x1e1
  val KEY_FN_F = 0x1e2
  val KEY_FN_S = 0x1e3
  val KEY_FN_B = 0x1e4
  val KEY_BRL_DOT1 = 0x1f1
  val KEY_BRL_DOT2 = 0x1f2
  val KEY_BRL_DOT3 = 0x1f3
  val KEY_BRL_DOT4 = 0x1f4
  val KEY_BRL_DOT5 = 0x1f5
  val KEY_BRL_DOT6 = 0x1f6
  val KEY_BRL_DOT7 = 0x1f7
  val KEY_BRL_DOT8 = 0x1f8
  val KEY_BRL_DOT9 = 0x1f9
  val KEY_BRL_DOT10 = 0x1fa
  val KEY_NUMERIC_0 = 0x200
  /*
                             * used by phones, remote
                             * controls,
                             */
  val KEY_NUMERIC_1 = 0x201
  /* and other keypads */
  val KEY_NUMERIC_2 = 0x202
  val KEY_NUMERIC_3 = 0x203
  val KEY_NUMERIC_4 = 0x204
  val KEY_NUMERIC_5 = 0x205
  val KEY_NUMERIC_6 = 0x206
  val KEY_NUMERIC_7 = 0x207
  val KEY_NUMERIC_8 = 0x208
  val KEY_NUMERIC_9 = 0x209
  val KEY_NUMERIC_STAR = 0x20a
  val KEY_NUMERIC_POUND = 0x20b
  /* We avoid low common keys in module aliases so they don't get huge. */ val KEY_MIN_INTERESTING = KEY_MUTE
  val KEY_MAX = 0x2ff
  val KEY_CNT = KEY_MAX + 1
  val REL_X = 0x00
  val REL_Y = 0x01
  val REL_Z = 0x02
  val REL_RX = 0x03
  val REL_RY = 0x04
  val REL_RZ = 0x05
  val REL_HWHEEL = 0x06
  val REL_DIAL = 0x07
  val REL_WHEEL = 0x08
  val REL_MISC = 0x09
  val REL_MAX = 0x0f
  val REL_CNT = REL_MAX + 1
  val ABS_X = 0x00
  val ABS_Y = 0x01
  val ABS_Z = 0x02
  val ABS_RX = 0x03
  val ABS_RY = 0x04
  val ABS_RZ = 0x05
  val ABS_THROTTLE = 0x06
  val ABS_RUDDER = 0x07
  val ABS_WHEEL = 0x08
  val ABS_GAS = 0x09
  val ABS_BRAKE = 0x0a
  val ABS_HAT0X = 0x10
  val ABS_HAT0Y = 0x11
  val ABS_HAT1X = 0x12
  val ABS_HAT1Y = 0x13
  val ABS_HAT2X = 0x14
  val ABS_HAT2Y = 0x15
  val ABS_HAT3X = 0x16
  val ABS_HAT3Y = 0x17
  val ABS_PRESSURE = 0x18
  val ABS_DISTANCE = 0x19
  val ABS_TILT_X = 0x1a
  val ABS_TILT_Y = 0x1b
  val ABS_TOOL_WIDTH = 0x1c
  val ABS_VOLUME = 0x20
  val ABS_MISC = 0x28
  val ABS_MT_TOUCH_MAJOR = 0x30
  /*
                               * Major axis of
                               * touching ellipse
                               */
  val ABS_MT_TOUCH_MINOR = 0x31
  /*
                               * Minor axis (omit if
                               * circular)
                               */
  val ABS_MT_WIDTH_MAJOR = 0x32
  /*
                               * Major axis of
                               * approaching ellipse
                               */
  val ABS_MT_WIDTH_MINOR = 0x33
  val ABS_MT_ORIENTATION = 0x34
  /* Ellipse orientation */
  val ABS_MT_POSITION_X = 0x35
  /*
                               * Center X ellipse
                               * position
                               */
  val ABS_MT_POSITION_Y = 0x36
  /*
                               * Center Y ellipse
                               * position
                               */
  val ABS_MT_TOOL_TYPE = 0x37
  /* Type of touching device */
  val ABS_MT_BLOB_ID = 0x38
  /*
                             * Group a set of packets as
                             * a blob
                             */
  val ABS_MT_TRACKING_ID = 0x39
  /*
                               * Unique ID of
                               * initiated contact
                               */
  val ABS_MAX = 0x3f
  val ABS_CNT = ABS_MAX + 1
  val SW_LID = 0x00
  /* set = lid shut */
  val SW_TABLET_MODE = 0x01
  /* set = tablet mode */
  val SW_HEADPHONE_INSERT = 0x02
  /* set = inserted */
  val SW_RFKILL_ALL = 0x03
  /*
                             * rfkill master switch,
                             * type "any" set = radio
                             * enabled
                             */
  val SW_RADIO = SW_RFKILL_ALL
  /* deprecated */
  val SW_MICROPHONE_INSERT = 0x04
  val SW_DOCK = 0x05
  /* set = plugged into dock */
  val SW_LINEOUT_INSERT = 0x06
  val SW_JACK_PHYSICAL_INSERT = 0x07
  /*
                                 * set = mechanical
                                 * switch set
                                 */
  val SW_VIDEOOUT_INSERT = 0x08
  val SW_MAX: Short = 0x0f
  val SW_CNT = SW_MAX + 1
  val MSC_SERIAL = 0x00
  val MSC_PULSELED = 0x01
  val MSC_GESTURE = 0x02
  val MSC_RAW = 0x03
  val MSC_SCAN = 0x04
  val MSC_MAX = 0x07
  val MSC_CNT = MSC_MAX + 1
  val LED_NUML = 0x00
  val LED_CAPSL = 0x01
  val LED_SCROLLL = 0x02
  val LED_COMPOSE = 0x03
  val LED_KANA = 0x04
  val LED_SLEEP = 0x05
  val LED_SUSPEND = 0x06
  val LED_MUTE = 0x07
  val LED_MISC = 0x08
  val LED_MAIL = 0x09
  val LED_CHARGING = 0x0a
  val LED_MAX = 0x0f
  val LED_CNT = LED_MAX + 1
  val REP_DELAY = 0x00
  val REP_PERIOD = 0x01
  val REP_MAX = 0x01
  val SND_CLICK = 0x00
  val SND_BELL = 0x01
  val SND_TONE = 0x02
  val SND_MAX = 0x07
  val SND_CNT = SND_MAX + 1
  val ID_BUS = 0
  val ID_VENDOR = 1
  val ID_PRODUCT = 2
  val ID_VERSION = 3
  val BUS_PCI = 0x01
  val BUS_ISAPNP = 0x02
  val BUS_USB = 0x03
  val BUS_HIL = 0x04
  val BUS_BLUETOOTH = 0x05
  val BUS_VIRTUAL = 0x06
  val BUS_ISA = 0x10
  val BUS_I8042 = 0x11
  val BUS_XTKBD = 0x12
  val BUS_RS232 = 0x13
  val BUS_GAMEPORT = 0x14
  val BUS_PARPORT = 0x15
  val BUS_AMIGA = 0x16
  val BUS_ADB = 0x17
  val BUS_I2C = 0x18
  val BUS_HOST = 0x19
  val BUS_GSC = 0x1A
  val BUS_ATARI = 0x1B
  /*
   * MT_TOOL types
   */
  val MT_TOOL_FINGER = 0
  val MT_TOOL_PEN = 1
  /*
   * Values describing the status of a force-feedback effect
   */
  val FF_STATUS_STOPPED = 0x00
  val FF_STATUS_PLAYING = 0x01
  val FF_STATUS_MAX = 0x01

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
}

case class InputEvent(time_sec: Long, time_usec: Long, kind: Short, code: Short, value: Int, source: String) {
  override def toString: String = {
    //    f"Event: time $time_sec%10d, type $kind%d, code $code%d, value $value%d"
    f"Event: time $time_sec%d.$time_usec%06d, type $kind%d, code $code%d, value $value%d"
  }
}
