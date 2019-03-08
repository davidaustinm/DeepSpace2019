/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RotateToHeading extends Command {
  double heading;
  double leftSpeed, rightSpeed;
  long timeout;
  public RotateToHeading(double heading, double leftSpeed, double rightSpeed) {
    this.heading = heading;
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
    //rightr Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    timeout = System.currentTimeMillis() + 2000;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double error = heading - Robot.sensors.getHeading();
    double correction = 0.01 * error;
    if(correction > 1) correction = 1;
    if(correction < -1) correction = -1;
    if (Math.abs(Robot.oi.driver.getX(Hand.kRight)) > 0.2) {
      Robot.driveTrain.runDefaultCommand();
    } else{ 
      Robot.driveTrain.setPower(-correction * leftSpeed, correction * rightSpeed);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if (System.currentTimeMillis() > timeout) return true;
    return Math.abs(heading - Robot.sensors.getHeading()) < 4.0;
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
