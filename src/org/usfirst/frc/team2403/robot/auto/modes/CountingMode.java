/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class CountingMode extends AutoMode {
	
	public CountingMode(){
		
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		while(true){
			runAction(new CountToFive());
		}
	}

}
