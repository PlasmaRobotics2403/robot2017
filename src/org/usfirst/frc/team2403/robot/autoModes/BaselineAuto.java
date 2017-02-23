package org.usfirst.frc.team2403.robot.autoModes;

import org.usfirst.frc.team2403.robot.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class BaselineAuto extends Command {
	
	DriveTrain drive;
	double time;
	
    public BaselineAuto(DriveTrain drive) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	this.drive = drive;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	time = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drive.FPSDrive(.5, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.FPSDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
