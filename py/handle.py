from evdev import InputDevice, ecodes, InputEvent
from select import select

mouse = InputDevice('/dev/input/event4')  # mouse
keyboard = InputDevice('/dev/input/event5')  # keyboard

while True:
    r, w, x = select([mouse], [], [])
    for dev in r:
        for event in dev.read():
            print(event.__class__, event)
