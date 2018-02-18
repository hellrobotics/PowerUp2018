package org.usfirst.frc.team7239.robot.subsystems;

import org.usfirst.frc.team7239.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Grabber extends Subsystem {
	
	Spark rGrab = new Spark(RobotMap.RIGHTGRABBER);
	Spark lGrab = new Spark(RobotMap.LEFTGRABBER);
	
	DoubleSolenoid pneuGrab = new DoubleSolenoid(0,1);

	private static Grabber m_instance;
	public static synchronized Grabber getInstance() {
		if (m_instance == null){
			m_instance = new Grabber();
		}
		return m_instance;
		
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void move (double speed) {
    	rGrab.set(speed);
    	lGrab.set(speed);
    	
    	if(Elevator.getInstance().getBotSwitch() == true || speed != 0) {
    		pneuGrab.set(DoubleSolenoid.Value.kForward);
    	} else {
    		pneuGrab.set(DoubleSolenoid.Value.kReverse);
    	}
    }
}

