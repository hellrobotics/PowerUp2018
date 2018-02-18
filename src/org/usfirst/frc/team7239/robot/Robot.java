
package org.usfirst.frc.team7239.robot;


import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team7239.robot.commands.ArcadeDrive;
import org.usfirst.frc.team7239.robot.commands.AutonomTest;
import org.usfirst.frc.team7239.robot.commands.ElevatorControl;
import org.usfirst.frc.team7239.robot.commands.ExampleCommand;
import org.usfirst.frc.team7239.robot.commands.GrabController;
import org.usfirst.frc.team7239.robot.subsystems.ExampleSubsystem;





public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
	Command arcadeDrive = new ArcadeDrive();
	Command elevatorControl = new ElevatorControl();
	Command autonomTest = new AutonomTest();
	Command gControl = new GrabController();
	Thread visionThread;
	

	@Override
	public void robotInit() {
		
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		/*
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(640, 480);

			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
			// Setup a CvSource. This will send images back to the Dashboard
			CvSource outputStream = CameraServer.getInstance().putVideo("Rectangle", 640, 480);

			// Mats are very memory expensive. Lets reuse this Mat.
			Mat mat = new Mat();

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat.  If there is an error notify the output.
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					outputStream.notifyError(cvSink.getError());
					// skip the rest of the current iteration
					continue;
				}
				// Put a rectangle on the image
				Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
						new Scalar(255, 255, 255), 5);
				
				// Give the output stream a new image to display
				outputStream.putFrame(mat);
			}
		});
		visionThread.setDaemon(true);
		visionThread.start();
		*/
	}
	

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}


	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		
		String autoSelected = SmartDashboard.getString("Auto Selector","Default"); 
		switch(autoSelected) 
		{ 
		case "Test": 
			autonomousCommand = autonomTest; 
		break; 
		case "Default Auto": default: 
			autonomousCommand = new ExampleCommand(); 
		break; 
		}
		autonomTest.start();
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
			
	}


	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		arcadeDrive.start();
		elevatorControl.start();
		gControl.start();
	}


	@Override
	public void teleopPeriodic() {
		
		Scheduler.getInstance().run();
		arcadeDrive.start();
	}


	@Override
	public void testPeriodic() {
		LiveWindow.run();
		autonomTest.start();
	}
}
