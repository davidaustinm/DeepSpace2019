/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.utilities.Utilities;

public class DriveToTarget2 extends Command implements AutoTarget {
  double encoderTarget;
  boolean finished = false;
  boolean dontReadDistance = false;
  boolean neverSeen = true;
  double speed;
  double lastDistance = 1000;
  Timer timer;
  final static double defaultSpeed = 0.30;
  long stopTime;

  public DriveToTarget2() {
    this(defaultSpeed);
  }

  public DriveToTarget2(double speed) {
    this.speed = speed;
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    stopTime = System.currentTimeMillis() + 3000;
    timer = null;
    dontReadDistance = false;
    double distance = Robot.targetInfo.getDistance();
    if (Double.isNaN(distance)) {
      encoderTarget = Double.POSITIVE_INFINITY;
      neverSeen = true;
      return;
    }
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double avgEncoder = (driveEncoders[0] + driveEncoders[1])/2.0;
    encoderTarget = avgEncoder + distance*Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
  }

  // Called repeatedly when this Command is scheduled to run
  double kAngle = 0.005; //0.0075;
  double rampDown = 35;
  double clipAnglePct = 0.2;
  double distanceCutOut = 30;
  int countNotSeen = 0;
  double frontEnd = 12;
  @Override
  protected void execute() {
    double distance = Robot.targetInfo.getDistance();

    /*  allow driver control
    if (Double.isNaN(distance) && neverSeen) {
      // we haven't seen the target yet so let the driver move robot
      Robot.driveTrain.runDefaultCommand();
      return;
    }
    */

    neverSeen = true; // ok, we've seen the target
    distance = getDistance();
    if(distance < distanceCutOut && timer == null) {
      timer = new Timer();
      timer.start();
    };
    if (distance < 10) dontReadDistance = true;
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double position = (driveEncoders[0]+driveEncoders[1])/2.0;
    double error = 0;
    if (!Double.isNaN(distance) && !dontReadDistance) {
      encoderTarget = position + distance*Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
      if(distance < 0) encoderTarget = Double.POSITIVE_INFINITY;
      error = Robot.targetInfo.getAngle();
    }

    double currentSpeed = speed;
    if (distance > 75) currentSpeed = 0.4;
   
    double correction = kAngle * error;
    if(dontReadDistance) correction = 0;
    double ramp = 1;
    double remaining = (encoderTarget - position)/Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
    if (remaining < rampDown) ramp = remaining/rampDown;
    currentSpeed *= ramp;

    /* Allow driver control
    if (Math.abs(Robot.oi.driver.getX(Hand.kRight)) > 0.3) {
      correction = -Robot.oi.driver.getX(Hand.kRight);
    }
    */

    correction = Utilities.clip(correction, -clipAnglePct * currentSpeed, clipAnglePct * currentSpeed);
    double leftPower = currentSpeed - correction;
    double rightPower = currentSpeed + correction;
    //Robot.driveTrain.setPower(leftPower, rightPower);
    //System.out.println(error + " " + leftPower + " " + rightPower);
  }

  public double getDistance() {
    double lidarDistance = Robot.lidar.getDistance();
    double cameraDistance = Robot.targetInfo.getDistance();
    if (Double.isNaN(cameraDistance)) {
      lastDistance = lidarDistance;
      return lidarDistance;
    }
    cameraDistance -= 6;  // offset to front of robot
    if (Double.isNaN(lidarDistance)) {
      lastDistance = cameraDistance;
      return cameraDistance;
    }
    double alpha = 1/(1+Math.exp(-(lastDistance-40)/2.0));
    double distance = alpha*cameraDistance + (1-alpha)*lidarDistance;
    lastDistance = distance;
    //System.out.println(lidarDistance + " " + cameraDistance + " " + distance);
    return distance;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    //if (System.currentTimeMillis() > stopTime) return true;
    //if (timer != null && timer.get() > 1.5) return true;
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double position = (driveEncoders[0] + driveEncoders[1])/2.0;
    return position + frontEnd >= encoderTarget;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveTrain.setPower(0,0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}

