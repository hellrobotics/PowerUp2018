package org.usfirst.frc.team7239.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	//PWM!!!!
	public static final int ELEVATORMOTOR = 0;
	public static final int RIGHTGRABBER = 1;
	public static final int LEFTGRABBER = 2;
	
	
	//CAN!!!! DU DIN JAEVEL
	public static final int RIGHTMOTOR1 = 1; //HEIS ENCODER
	public static final int RIGHTMOTOR2 = 2; //rEncoder
	public static final int LEFTMOTOR1 = 3; //lEncoder 
	public static final int LEFTMOTOR2 = 4;
	
	//DIO
	public static final int LIMITSWITCHTOP = 1;
	public static final int LIMITSWITCHBOT = 0;
	
	
	
	

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
