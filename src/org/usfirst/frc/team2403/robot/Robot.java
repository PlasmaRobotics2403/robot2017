package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaJoystick;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

	PlasmaJoystick joystick;
	DriveTrain driveTrain;

	
	@Override
	public void robotInit() {
		joystick = new PlasmaJoystick(Constants.JOYSTICK1_PORT);
		driveTrain = new DriveTrain(Constants.TALON_L_ID,
									Constants.TALON_L_SLAVE_ID,
									Constants.TALON_R_ID,
									Constants.TALON_R_SLAVE_ID);
	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {

	}
	
	@Override
	public void teleopInit(){
		driveTrain.navX.zeroYaw();
	}
	
	@Override
	public void teleopPeriodic() {
		driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
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

