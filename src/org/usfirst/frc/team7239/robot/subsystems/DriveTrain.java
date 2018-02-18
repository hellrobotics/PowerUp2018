package org.usfirst.frc.team7239.robot.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team7239.robot.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
/**
 *
 */
public class DriveTrain extends Subsystem {
	
	
	double limitFactor = 0.5;
	double slowFactor = 0.35;
	
	double mP1S = 1;
	double degP1S = 45;
	
	public static double diam = 0.1524; //m
	public static double circ = diam*Math.PI; //m
	public static double talonUnitToMPS = (10.0/4096.0)*circ; // 
	public static double talonUnitToM = circ/4096.0;
	public static final double DRV_POS_P_GAIN = 0;
	
	WPI_TalonSRX left1 = new WPI_TalonSRX(RobotMap.LEFTMOTOR1);
	WPI_TalonSRX left2 = new WPI_TalonSRX(RobotMap.LEFTMOTOR2);
	SpeedControllerGroup m_left = new SpeedControllerGroup(left1, left2);
	
	WPI_TalonSRX right1 = new WPI_TalonSRX(RobotMap.RIGHTMOTOR1);
	WPI_TalonSRX right2 = new WPI_TalonSRX(RobotMap.RIGHTMOTOR2);
	SpeedControllerGroup m_right = new SpeedControllerGroup(right1, right2);
	
	DifferentialDrive Drive = new DifferentialDrive(m_right, m_left);
	//DifferentialDrive Drive = new DifferentialDrive(m_left,m_right);
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	private static DriveTrain m_instance;
	public static synchronized DriveTrain getInstance() {
		if (m_instance == null){
			m_instance = new DriveTrain();
		}
		return m_instance;
	}
	
	private DriveTrain() {
		right2.setSensorPhase(true);
		left1.setSensorPhase(true);
		right2.config_kF(0, 0.39346, 0);
		left1.config_kF(0, 0.42625, 0);
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
	public void Arcade(double moveValue, double rotateValue, double maxSpeed) {
		double move = moveValue * maxSpeed;
		double turn = rotateValue * maxSpeed;
		
		Drive.arcadeDrive(move, turn, true);	
		
		SmartDashboard.putNumber("RightRawSpeed", right2.getSelectedSensorVelocity(0));
    	SmartDashboard.putNumber("LeftRawSpeed", left1.getSelectedSensorVelocity(0));
    	SmartDashboard.putNumber("RightSpeed", right2.getSelectedSensorVelocity(0)*talonUnitToMPS);
    	SmartDashboard.putNumber("LeftSpeed", left1.getSelectedSensorVelocity(0)*talonUnitToMPS);
	}
	
	public int getElevatorPos () {
		return right1.getSelectedSensorPosition(0);
	}
	
	public void setElevatorZero () {
		right1.setSelectedSensorPosition(0, 0, 0);
	}
	
	public void AutoMove (double meterFWD, double degR) {
		System.out.println("Kjorer frem " + meterFWD + " meter og svinger " + degR + " grader.");
		Drive.arcadeDrive(-0.5, 0);
	}
	
	public void setVelocity (double rmps, double lmps) {
		double targetVelR = rmps/talonUnitToMPS;
		right2.set(ControlMode.Velocity,targetVelR*-1);
		right1.set(ControlMode.Follower, right2.getDeviceID());
		double targetVelL = lmps/talonUnitToMPS;
		left1.set(ControlMode.Velocity,targetVelL);
		left2.set(ControlMode.Follower, left1.getDeviceID());
		
		SmartDashboard.putNumber("er", right2.getClosedLoopError(0));
		SmartDashboard.putNumber("el", left1.getClosedLoopError(0));
		SmartDashboard.putNumber("ermps", right2.getClosedLoopError(0)*talonUnitToMPS);
		SmartDashboard.putNumber("elmps", left1.getClosedLoopError(0)*talonUnitToMPS);
		SmartDashboard.putNumber("rTVelMPS", rmps*-1);
		SmartDashboard.putNumber("lTVelMPS", lmps);
		SmartDashboard.putNumber("rTVel", targetVelR*-1);
		SmartDashboard.putNumber("lTVel", targetVelL);
		SmartDashboard.putNumber("positionRRaw", right2.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("positionLRaw", left1.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("positionR-meter", right2.getSelectedSensorPosition(0)*talonUnitToM);
		SmartDashboard.putNumber("positionL-meter", left1.getSelectedSensorPosition(0)*talonUnitToM);
		
	}
	
	public void resetEncoders () {
		right2.setSelectedSensorPosition(0, 0, 0);
		left1.setSelectedSensorPosition(0, 0, 0);
	}
	
	public void setSafetyEnabled(boolean inp) {
		Drive.setSafetyEnabled(inp);
	}
	
	public void arcadeDrive(double power, double turn) {
		Drive.arcadeDrive(power, turn);
	}

	public int getPositionLeftNu() {
		return left1.getSelectedSensorVelocity(0);
	}

	public int getPositionRightNu() {
		return right2.getSelectedSensorVelocity(0);
	}
	
}

