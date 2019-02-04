/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RotateToPoint extends Command {
  double targetX;
  double targetY;
  double leftSpeed;
  double rightSpeed;
  double angleError;

  public RotateToPoint(double x, double y, double leftSpeed, double rightSpeed) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
    targetX = x;
    targetY = y;
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double[] position = Robot.sensors.getPosition();
    double changeInX = targetX - position[0];
    double changeInY = targetY - position[1];
    double heading = Math.toDegrees(Math.atan2(changeInY, changeInX));
    angleError = heading - Robot.sensors.getHeading();
    while(angleError > 180) angleError -= 360;
    while(angleError < -180) angleError += 360;
    double correction = 0.015 * angleError;
    if(correction > 1) correction = 1;
    if(correction < -1) correction = -1;
    Robot.driveTrain.setPower(-correction * leftSpeed, correction * rightSpeed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Math.abs(angleError) < 4.0;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveTrain.setPower(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
