package org.usfirst.frc.team7239.robot.commands;

import org.usfirst.frc.team7239.robot.subsystems.DriveTrain;
//import org.usfirst.frc.tm744yr18.robot.config.TmSdKeysI.SdKeysE;
//import org.usfirst.frc.tm744yr18.robot.exceptions.TmExceptions;
//import org.usfirst.frc.tm744yr18.robot.interfaces.TmToolsI.Tt;
//import org.usfirst.frc.tm744yr18.robot.subsystems.TmSsDriveTrain;
import fileIo.TmTrajectoryFileIo;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TmACmdFollowTrajectory extends Command {
	DriveTrain ssDrive;
	DriverStation m_tds;
	DriverStation m_ds;
	TmTrajectoryFileIo tWorker; // = new Worker_TrajFileIo(OPSYS);
	char switchSide;
	char scaleSide;
	boolean runAuto = true;
	int traj_index_left;
	int traj_index_right;
	Timer autoTime;
	
	/* Will follow the trajectory found in filename. Given that the correct switch and scale is found.
	 * filename - name of file with trajectory data
	 * switchSide - char. Should be 'L' if you only run trajectory when you own the left side switch
	 * 					  Should be 'R' if you only run trajectory when you own the right side switch
	 * 						   Can be anything else ('*' is good) if you run the trajectory regardless of switch side.
	 * scaleSide - char. Should be 'L' if you only run trajectory when you own the left side scale
	 * 					 Should be 'R' if you only run trajectory when you own the right side scale
	 * 				     Can be anything else ('*' is good) if you run the trajectory regardless of scale side.
	 * 
	 */
    public TmACmdFollowTrajectory(String filename, char switchSide, char scaleSide) {
    	this(filename, switchSide, scaleSide, SHOW_FILE);
    }
	public static final boolean NO_SHOW_FILE = false;
	public static final boolean SHOW_FILE = true;
    public TmACmdFollowTrajectory(String filename, char switchSide, char scaleSide, boolean showFile) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	m_tds = DriverStation.getInstance();
    	m_ds = DriverStation.getInstance();
     	ssDrive = DriveTrain.getInstance();
     	autoTime = new Timer();
     	requires(ssDrive);
     	
     	this.switchSide = switchSide;
     	this.scaleSide  = scaleSide;
		
		String m_trajFnArg = filename; //trajectory filename
		tWorker = new TmTrajectoryFileIo(m_trajFnArg);
		tWorker.loadCsvData();
		
		if(showFile) { tWorker.showOnConsole(this.getClass().getSimpleName() + " -- "); }	
//		int cnt = 0;
//		System.out.println(Tt.getClassName(this) + " showing trajectory file " + filename);
//		for(TrajDataCsv l : tWorker.trajDataCsvL) {
//			P.printFrmt(P.PrtYn.Y, "line %3d: %0 2.6f  %0 2.6f  %0 2.6f  %0 2.6f  %0 2.6f"
//					+ "     %0 2.6f  %0 2.6f  %0 2.6f  %0 2.6f  %0 2.6f      %0 2.6f  %0 2.6f  %0 2.6f  %0 2.6f"
//					+ "     %0 2.6f  %0 2.6f  %0 2.6f\n",
//		                        (++cnt),
//		                        l.getColA(), l.getColB(), l.getColC(), l.getColD(), l.getColE(),
//		                        l.getColF(), l.getColG(), l.getColH(), l.getColI(), l.getColJ(),
//		                        l.getColK(), l.getColL(), l.getColM(), l.getColN(), l.getColO(),
//		                        l.getColP(), l.getColQ()
//		                        );
//		}
		
		//inspect the file to ensure assumptions hold
//		boolean foundProbs = false;
//		for(TrajDataCsv l : tWorker.trajDataCsvL) {
//			if(l.getLeftTime() != l.getRightTime()) { foundProbs = true; }	
//		}
//		if(foundProbs) {
//			System.out.println("File " + m_trajFnArg + " does not meet assumption that left and right times will be identical");
//			throw TmExceptions.getInstance().new Team744RunTimeEx("trajectory file " + m_trajFnArg + " does not meet assumptions");
//		}
     	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//check if this is a switch or scale side-specific auto
    	//if so, skip or run accordingly
    	runAuto = true;
     	String gameData = m_ds.getGameSpecificMessage();
        if(gameData.length() > 0) {
        	if(gameData.charAt(0) != switchSide && 
        		(switchSide == 'L' || switchSide == 'R')) {
				runAuto = false;
        	} //else runAuto
        	
        	if(gameData.charAt(1) != scaleSide && 
        		(switchSide == 'L' || switchSide == 'R')) {
				runAuto = false;
        	} //else runAuto        	
		}
        else { //don't run auto is no gameData available
        	runAuto = false;
        }
        
        //initialize things
        traj_index_left  = 0;
        traj_index_right = 0;
        autoTime.reset();
        autoTime.start();
        ssDrive.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	int listSize = tWorker.trajDataCsvL.size();

    	if(traj_index_left < listSize) {
    		while((traj_index_left < listSize) && tWorker.trajDataCsvL.get(traj_index_left).getLeftTime()<autoTime.get()) {
    			traj_index_left++;
    		}
    	}

    	if(traj_index_right < listSize) {
    		while((traj_index_right < listSize) && tWorker.trajDataCsvL.get(traj_index_right).getRightTime()<autoTime.get()) {
    			traj_index_right++;
    		}
    	}

    	//Nav
    	if(traj_index_left < listSize && traj_index_right < listSize) {
    		double rightError = ssDrive.getPositionRightNu()*DriveTrain.talonUnitToM 
    				- tWorker.trajDataCsvL.get(traj_index_right).getRightPos();
    		double leftError  = ssDrive.getPositionLeftNu()*DriveTrain.talonUnitToM  
    				- tWorker.trajDataCsvL.get(traj_index_left).getLeftPos();
    		//		double thetaError = 
    		SmartDashboard.putNumber("rErrM",
    				rightError);
    		SmartDashboard.putNumber("lErrM",
    				leftError);
    		double rightVelInput = tWorker.trajDataCsvL.get(traj_index_right).getRightVel() + 
    				DriveTrain.DRV_POS_P_GAIN * rightError;// + 
    		//							   TmSsDriveTrain.DrvTrainCnst.DRV_ANGLE_P_GAIN  * thetaError;
    		double leftVelInput = tWorker.trajDataCsvL.get(traj_index_left).getLeftVel() + 
    				DriveTrain.DRV_POS_P_GAIN * leftError;// - //sign is intentionally different from above 
    		//				   TmSsDriveTrain.DrvTrainCnst.DRV_ANGLE_P_GAIN  * thetaError;

    		ssDrive.setVelocity(rightVelInput, leftVelInput);
    		//		drvMtrRearRAndEnc.set(ControlMode.Velocity, rightVelInput);
    		//		drvMtrCenterR.set(ControlMode.Follower, drvMtrRearRAndEnc.getDeviceID());
    		//		drvMtrFrontR.set(ControlMode.Follower, drvMtrRearRAndEnc.getDeviceID());
    		//
    		//		drvMtrRearLAndEnc.set(ControlMode.Velocity, leftVelInput);
    		//		drvMtrCenterL.set(ControlMode.Follower, drvMtrRearLAndEnc.getDeviceID());
    		//		drvMtrFrontL.set(ControlMode.Follower, drvMtrRearLAndEnc.getDeviceID());
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean ans = false;
     	if( m_tds.isAutonomous() 
     			&& runAuto 
     			&& 
     			(traj_index_left < tWorker.trajDataCsvL.size() 
     			|| traj_index_right < tWorker.trajDataCsvL.size())
     			) {
     		ans = false;
     	} else {
     		ans = true;
     		//ssDrive.stopAutonTrajectoryTest();
     	}
     	return ans;
    }

    // Called once after isFinished returns true
    protected void end() {
    	ssDrive.arcadeDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
 		//ssDrive.stopAutonTrajectoryTest();
 		ssDrive.arcadeDrive(0, 0);
    }
}
