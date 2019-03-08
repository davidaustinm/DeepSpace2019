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

public class FrontLiftCommand extends Command {
  double lastError = 0;


  //TODO: change these values
  double kp = 1/6000.0;
  double kd = 1/60000.0;
  int LOWERLIMIT = 2000;
	int UPPERLIMIT = 48000;

  public FrontLiftCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.frontLift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double power = -Robot.oi.operator.getY(Hand.kRight);
    if(Math.abs(power) > 0.1) {
      Robot.frontLift.setHoldPosition(Robot.frontLift.getPosition());
      Robot.frontLift.setManual(true);
    } else {
      int position = Robot.frontLift.getPosition();
      double error = Robot.frontLift.getHoldPosition() - position;
      double changeError = lastError - error;
      
      power = kp * error + kd * changeError;
      if (Math.abs(power) > 1) {
        if (power > 1) power = 1;
        else power = -1;
      }
      
      lastError = error;
    }
    int liftPosition = Robot.frontLift.getPosition();
    /*
    if (liftPosition < LOWERLIMIT && power < 0) power = 0;
    if (liftPosition > UPPERLIMIT && power > 0) power = 0;
    */
    if (power < 0 && Robot.gameState.isEndGame() == false) {
      if (liftPosition < 10000) power *= 0.3;
      else power *= 0.6;
    }
    //SmartDashboard.putNumber("lift power", power);
    Robot.frontLift.setPower(power);
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
