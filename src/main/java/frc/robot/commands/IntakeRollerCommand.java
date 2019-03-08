/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class IntakeRollerCommand extends Command {
  public IntakeRollerCommand() {
    requires(Robot.intakeRoller);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double leftTrigger = Robot.oi.operator.getTriggerAxis(Hand.kLeft);
    double rightTrigger = Robot.oi.operator.getTriggerAxis(Hand.kRight);
    double power = 0;
    if (leftTrigger > 0.2) power = -leftTrigger;
    else power = rightTrigger;
    //SmartDashboard.putNumber("Intake Roller Power", power);
    Robot.intakeRoller.setPower(power);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
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
