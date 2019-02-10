/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DriveToPoint extends Command {
  double targetX;
  double targetY;
  double speed;
  double distance;
  double lastDistance = 10000;

  public DriveToPoint(double x, double y, double speed) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
    targetX = x;
    targetY = y;
    this.speed = speed;
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
    distance = Math.sqrt(changeInX * changeInX + changeInY * changeInY);
    double heading = Math.toDegrees(Math.atan2(changeInY, changeInX));
    double error = heading - Robot.sensors.getHeading();
    while(error > 180) error -= 360;
    while(error < -180) error += 360;
    double correction = 0.02 * error;
    double ramp = 0.01 * distance * Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
    if(ramp > 1) ramp = 1;
    Robot.driveTrain.setPower((speed - correction) * ramp, (speed + correction) * ramp);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    boolean finished = distance > lastDistance;
    lastDistance = distance;
    return finished;
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
