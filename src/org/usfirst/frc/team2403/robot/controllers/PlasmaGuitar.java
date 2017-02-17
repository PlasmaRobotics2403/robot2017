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
		
		GREEN = new PlasmaButton(1, port);
		RED = new PlasmaButton(2, port);
		YELLOW = new PlasmaButton(4, port);
		BLUE = new PlasmaButton(3, port);
		ORANGE = new PlasmaButton(5, port);
		BACK = new PlasmaButton(7, port);
		START = new PlasmaButton(8, port);
		dPad = new PlasmaDPad(0, port);
		
		whammyBar = new PlasmaAxis(4, port);
	}
	
}
