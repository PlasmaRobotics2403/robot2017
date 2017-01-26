package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaJoystick;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

	PlasmaJoystick joystick;
	
	@Override
	public void robotInit() {
		joystick = new PlasmaJoystick(Constants.JOYSTICK1_PORT);
	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {

	}
	
	@Override
	public void teleopInit(){
		
	}
	
	@Override
	public void teleopPeriodic() {
		
	}
	
	@Override
	public void testInit(){
	}

	@Override
	public void testPeriodic() {
		joystick.rumble.setLeftRumble(joystick.LT.getFilteredAxis());
		joystick.rumble.setRightRumble(joystick.RT.getFilteredAxis());

	}
}

