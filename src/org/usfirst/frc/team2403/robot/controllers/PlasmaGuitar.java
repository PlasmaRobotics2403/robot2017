package org.usfirst.frc.team2403.robot.controllers;

public class PlasmaGuitar {
	
	public PlasmaButton GREEN;
	public PlasmaButton RED;
	public PlasmaButton YELLOW;
	public PlasmaButton BLUE;
	public PlasmaButton ORANGE;
	public PlasmaButton BACK;
	public PlasmaButton START;
	
	public PlasmaDPad dPad;
	
	public PlasmaAxis whammyBar;
	
	private final int port;
	
	public PlasmaGuitar(int port) {
		this.port = port;
		
		GREEN = new PlasmaButton(JoystickConstants.GUITAR_GREEN_ID, port);
		RED = new PlasmaButton(JoystickConstants.GUITAR_RED_ID, port);
		YELLOW = new PlasmaButton(JoystickConstants.GUITAR_YELLOW_ID, port);
		BLUE = new PlasmaButton(JoystickConstants.GUITAR_BLUE_ID, port);
		ORANGE = new PlasmaButton(JoystickConstants.GUITAR_ORANGE_ID, port);
		BACK = new PlasmaButton(JoystickConstants.GUITAR_BACK_ID, port);
		START = new PlasmaButton(JoystickConstants.GUITAR_START_ID, port);
		dPad = new PlasmaDPad(JoystickConstants.GUITAR_DPAD_ID, port);
		
		whammyBar = new PlasmaAxis(JoystickConstants.GUITAR_WHAMMY_ID, port);
	}
	
	public int getPort(){
		return port;
	}
}
