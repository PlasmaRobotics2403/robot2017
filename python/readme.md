2017 FIRST Robotics Competition (FRC) FIRST Steamworks
Raspberry Pi Based Vision Tracking
FRC Team 2403 Plasma Robotics
www.plasmarobotics.com

This filesystem contains the code to FRC Team 2403 Plasma Robotics' Vision Tracking.
In the 2017 game, FIRST Steamworks, robots can vision track various strips of retroreflective tape found on both the boiler,
where fuel (plastic wiffle balls) can be shot, and the airship, where robots can place large plastic gears onto spring pegs.

Plasma Robotics is using two Raspberry Pis to compute vision tracking distances and angles from two Logitech c920 cameras.

shooter_vision.sh and gear_vision.sh both setup the cameras (edit HSV values, brightness, contrast, etc.) and run the respective
vision program. Running autostart.sh will setup the cameras and autorun the selected program from Pi startup. In order to change
program is selected, comment out which program (shooter_vision.sh or gear_vision.sh) you don't want to run. Do NOT run both programs
at the same time.
