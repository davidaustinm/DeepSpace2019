/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.utilities.CSVReader;
import frc.robot.utilities.Utilities;

public class ExecuteDriveProfile extends Command implements Runnable {
  double[][] profile;
  Notifier notifier;

  double kP = 0.025;
  double kAngle = 0.0125;
  //double kAngle = 0.0;
  double vMax = 1.44;
	boolean isNotifierRunning = false;
  int currentPoint = 0;
  Object lock = new Object();
  String filename;

  public ExecuteDriveProfile(String filename) {
    this.filename = filename;
    CSVReader reader = new CSVReader(filename);
    profile = reader.parseCSV();
    System.out.println("length of profile " + profile.length);
    //vMax = reader.getVmax();
    notifier = new Notifier(this);
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.sensors.resetDriveEncoders();
    isNotifierRunning = true;
    notifier.startPeriodic(0.01);
    System.out.println("initializing profile " + filename);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return isNotifierFinished();
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    notifier.stop();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }

  public void stopNotifier() {
    synchronized(lock) {
      isNotifierRunning = false;
    }
  }

  public boolean isNotifierFinished() {
    synchronized(lock) {
      return !isNotifierRunning;
    }
  }

  public void run() {
    if (currentPoint >= profile.length) {
      stopNotifier();
      return;
    }
    double leftPos = profile[currentPoint][0];
    double leftVel = profile[currentPoint][1];
    double rightPos = profile[currentPoint][2];
    double rightVel = profile[currentPoint][3];
    double heading = profile[currentPoint][4];
      
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double currentLeftPos = driveEncoders[0]/Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
    double currentRightPos = driveEncoders[1]/Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
    	
   	double leftError = leftPos-currentLeftPos;   	
    double rightError = rightPos-currentRightPos;
    if(RobotMap.DEBUG){
      //System.out.println(leftError + " " + rightError);
    }
    double angleError = Utilities.normalizeAngle(heading - Robot.sensors.getHeading(), 180);
    double correction = kAngle * angleError;
    if (profile.length - currentPoint < 20) correction = 0;
    if (profile.length - currentPoint == 20) kP /= 2.0;

   	currentPoint++;
    	
   	double leftPower = leftVel / vMax + (kP * (leftError)) - correction;
   	double rightPower = rightVel / vMax + (kP * (rightError)) + correction;
    	
   	leftPower = Utilities.clip(leftPower, -1, 1);
   	rightPower = Utilities.clip(rightPower, -1, 1);
    	
   	Robot.driveTrain.setPower(leftPower, rightPower);
    	
   	if(currentPoint == profile.length) {
      stopNotifier();
    }
  }
}
