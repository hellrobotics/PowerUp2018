package org.usfirst.frc.team7239.robot.commands;

import org.usfirst.frc.team7239.robot.OI;
import org.usfirst.frc.team7239.robot.subsystems.DriveTrain;
import org.usfirst.frc.team7239.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ElevatorControl extends Command {

	private Elevator ssElevator;
	private OI oi;
	
    public ElevatorControl() {
    	ssElevator = Elevator.getInstance();
    	requires(ssElevator);
    	oi = OI.getInstance();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//System.out.println("POV: " + oi.stick.getPOV());
    	if(oi.stick.getPOV() == 0) {
    		ssElevator.move(1*oi.stick.getRawAxis(3));
    	} else if (oi.stick.getPOV() == 180) {
    		ssElevator.move(-.5*oi.stick.getRawAxis(3));
    	} else {
    		ssElevator.move(0);
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
