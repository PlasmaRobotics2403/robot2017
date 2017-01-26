package org.usfirst.frc.team2403.robot.controllers;

import edu.wpi.first.wpilibj.hal.HAL;

public class PlasmaRumble {
	
	private final byte joystickPort;
		
	private short rightRumble;
	private short leftRumble;
	
	/**
	 * Constructor for the rumble class
	 * 
	 * @param joystickPort - Joystick port that the rumble is for
	 * 
	 * @author Nic A
	 */
	public PlasmaRumble(int joystickPort) {
		this.joystickPort = (byte)joystickPort;
		rightRumble = 0;
		leftRumble = 0;
	}
	
	/**
	 * Set the speed of the left rumble
	 * 
	 * @param speed - Speed of the rumble
	 * 
	 * @author Nic A
	 */
	public void setLeftRumble(double speed){
		speed = (speed < 0) ? 0 : (speed > 1) ? 1 : speed;
		leftRumble = (short)(speed * 65535);
		HAL.setJoystickOutputs(joystickPort, 0, leftRumble, rightRumble);
	}
	
	/**
	 * Set the speed of the right rumble
	 * 
	 * @param speed - Speed of the rumble
	 * 
	 * @author Nic A
	 */
	public void setRightRumble(double speed){
		speed = (speed < 0) ? 0 : (speed > 1) ? 1 : speed;
		rightRumble = (short)(speed * 65535);
		HAL.setJoystickOutputs(joystickPort, 0, leftRumble, rightRumble);
	}
}
