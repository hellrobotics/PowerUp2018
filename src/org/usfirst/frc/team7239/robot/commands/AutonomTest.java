package org.usfirst.frc.team7239.robot.commands;

import org.usfirst.frc.team7239.robot.OI;



import org.usfirst.frc.team7239.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomTest extends Command {
	private DriveTrain ssTrain;
	private OI oi;
	
	private boolean isRunningNow = false;
	
    public AutonomTest() {
    	ssTrain = DriveTrain.getInstance();
    	requires(ssTrain);
    	oi = OI.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("Kjorer autonom");
    	ssTrain.AutoMove(1, 0);
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	//if(isRunningNow == false) {
    		//ssTrain.AutoMove(1, 0);
    		//isRunningNow = true;
    	//}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
