#!/bin/bash
/usr/bin/v4l2-ctl --set-ctrl=gain=0
/usr/bin/v4l2-ctl --set-ctrl=exposure_auto=1
/usr/bin/v4l2-ctl --set-ctrl=exposure_absolute=8
/usr/bin/v4l2-ctl --set-ctrl=brightness=0
/usr/bin/v4l2-ctl --set-ctrl=saturation=255

cd ~/plasma
source ~/.profile
workon cv
python gear_vision.py