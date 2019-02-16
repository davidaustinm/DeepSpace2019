/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class RaiseRearLift extends Command {
  final int REARONLY = 0;
  final int FRONTONLY = 1;
  final int BOTH = 2;
  int state = BOTH;
  double frontDownSpeed = -0.8;
  double rearDownSpeed = -0.8;
  final int frontBottom = 100;
  public RaiseRearLift() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.rearLift);
    requires(Robot.frontLift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double pitch = Robot.sensors.getPitch(); 
    if (pitch > 5) state = FRONTONLY;
    if (pitch < -5) state = REARONLY;
    if (Math.abs(pitch) < 2) state = BOTH;
    double frontPower = frontDownSpeed;
    double rearPower = rearDownSpeed;
    if (state == FRONTONLY) rearPower = 0;
    if (state == REARONLY) frontPower = 0;
    Robot.frontLift.setPower(frontPower);
    Robot.rearLift.setPower(rearPower);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return Robot.frontLift.getPosition() <= frontBottom;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
