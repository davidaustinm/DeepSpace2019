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
  double frontDownSpeed = -0.8;
  double rearDownSpeed = -0.8;
  final int frontBottom = 0;
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
    int frontPosition = Robot.frontLift.getPosition();
    if (frontPosition > frontBottom) {
      Robot.frontLift.setPower(frontDownSpeed);
      if (Robot.sensors.getPitch() > 5) Robot.rearLift.setPower(rearDownSpeed);
    }
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
