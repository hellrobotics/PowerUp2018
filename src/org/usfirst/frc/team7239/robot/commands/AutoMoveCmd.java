package org.usfirst.frc.team7239.robot.commands;

import org.usfirst.frc.team7239.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutoMoveCmd extends Command {
	double meterFWD;
	double degR;
	double time;
	private DriveTrain ssTrain;
	
	public AutoMoveCmd(double meterFWD, double degR, double time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		this.meterFWD = meterFWD;
		this.degR = degR;
		this.time = time;
		setTimeout(time);
		ssTrain = DriveTrain.getInstance();
    	requires(ssTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("Kjorer frem " + meterFWD + " meter og svinger " + degR + " grader.");
    	ssTrain.setSafetyEnabled(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	ssTrain.arcadeDrive(0.5, 0);
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	ssTrain.arcadeDrive(0, 0);
    	ssTrain.setSafetyEnabled(true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	ssTrain.arcadeDrive(0, 0);
    	ssTrain.setSafetyEnabled(true);
    }
}
