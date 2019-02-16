/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.utilities.Utilities;

public class DriveToTarget extends Command {
  double encoderTarget;
  boolean finished = false;
  boolean dontReadDistance = false;
  double speed;
  final static double defaultSpeed = 0.4;

  public DriveToTarget() {
    this(defaultSpeed);
  }

  public DriveToTarget(double speed) {
    this.speed = speed;
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double distance = Robot.client.getDistance();
    if (Double.isNaN(distance)) {
      System.out.println("distance = NaN");
      //finished = true;
      encoderTarget = Double.POSITIVE_INFINITY;
      countNotSeen = 1;
      return;
    }
    double avgEncoder = (driveEncoders[0] + driveEncoders[1])/2.0;
    encoderTarget = avgEncoder + distance*Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
  }

  // Called repeatedly when this Command is scheduled to run
  double kAngle = 0.005;
  double rampDown = 40;
  double clipAnglePct = 0.2;
  double distanceCutOut = 30;
  int countNotSeen = 0;
  @Override
  protected void execute() {
    double distance = Robot.client.getDistance();
    if(distance < distanceCutOut) dontReadDistance = true;
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double position = (driveEncoders[0]+driveEncoders[1])/2.0;
    double error = 0;
    if (!Double.isNaN(distance) && !dontReadDistance) {
      encoderTarget = position + distance*Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
      if(distance < 0) encoderTarget = Double.POSITIVE_INFINITY;
      error = Robot.client.getAngle();
      countNotSeen = 0;
    }
    if(RobotMap.DEBUG){
      //System.out.println(position + " " + encoderTarget + " " + distance);
    }
    if (Double.isNaN(distance) && countNotSeen > 0) {
      countNotSeen++;
      if (countNotSeen > 30) {
        finished = true;
      }
    }
    double correction = kAngle * error;
    if(dontReadDistance) correction = 0;
    correction = Utilities.clip(correction, -clipAnglePct * speed, clipAnglePct * speed);
    double ramp = 1;
    double remaining = (encoderTarget - position)/Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
    if (remaining < rampDown) ramp = remaining/rampDown;
    double leftPower = speed * ramp - correction;
    double rightPower = speed * ramp + correction;
    Robot.driveTrain.setPower(leftPower, rightPower);
    System.out.println(error + " " + leftPower + " " + rightPower);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double position = (driveEncoders[0] + driveEncoders[1])/2.0;
    return finished || position >= encoderTarget;
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
