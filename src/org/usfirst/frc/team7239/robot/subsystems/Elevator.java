package org.usfirst.frc.team7239.robot.subsystems;

import org.usfirst.frc.team7239.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Elevator extends Subsystem {

    VictorSP elevatorMotor = new VictorSP(RobotMap.ELEVATORMOTOR);
    DigitalInput limSwitch1 = new DigitalInput(RobotMap.LIMITSWITCHTOP);
    DigitalInput limSwitch2 = new DigitalInput(RobotMap.LIMITSWITCHBOT);
    
    
    int sensorTopVal = 101010101; //45284.0 43116.0
    
	private static Elevator m_instance;
	public static synchronized Elevator getInstance() {
		if (m_instance == null){
			m_instance = new Elevator();
		}
		return m_instance;
		
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public boolean getBotSwitch() {
    	return limSwitch2.get();
    }
    
    public void move (double speed) {
    	
    	SmartDashboard.putBoolean("TopLim", limSwitch1.get());
    	SmartDashboard.putBoolean("BotLim", limSwitch2.get());
    	int elpos = DriveTrain.getInstance().getElevatorPos();
    	SmartDashboard.putNumber("HeisPos", elpos);
    	if(limSwitch1.get() == true && speed > 0) {
    		elevatorMotor.set(0);
    		sensorTopVal = DriveTrain.getInstance().getElevatorPos();
    		SmartDashboard.putNumber("HeisMax", sensorTopVal);
    	} else if(limSwitch2.get() == true && speed < 0) {
    		elevatorMotor.set(0);
    		DriveTrain.getInstance().setElevatorZero();
    	} else {
    		elevatorMotor.set(speed);
    	}
    }
    
    public void autoMove (int pos, double tolerance) {
    	int elpos = DriveTrain.getInstance().getElevatorPos();
    	double error = pos-elpos;
		double pk = 1.0/tolerance;
    	move(error*pk);
    }
}

