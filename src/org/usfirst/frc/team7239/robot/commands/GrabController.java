package org.usfirst.frc.team7239.robot.commands;

import org.usfirst.frc.team7239.robot.OI;
import org.usfirst.frc.team7239.robot.subsystems.Elevator;
import org.usfirst.frc.team7239.robot.subsystems.Grabber;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GrabController extends Command {
	//hallo
	private Grabber ssGrab;
	private OI oi;

    public GrabController() {
    	ssGrab = Grabber.getInstance();
    	requires(ssGrab);
    	oi = OI.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (oi.stick.getRawButton(1) == true) {
    		ssGrab.move(0.75);
    	} else if (oi.stick.getRawButton(2) == true) {
    		ssGrab.move(-0.75);
    	} else {    		
    		ssGrab.move(0);
    	}
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
