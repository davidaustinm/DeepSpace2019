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

public class ArcadeDriveCommand extends Command {
  public ArcadeDriveCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  double alpha = 0.5; //0.74;
  double turnAlpha = .7;
  double lastTurn = 0;
  double turnAlpham1 = 1-turnAlpha;
  double alpham1 = 1-alpha;
  double lastThrottle = 0;
  double lastSteering = 0;
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double throttle = -Robot.m_oi.driver.getY(Hand.kLeft);
    double steering = -0.5*Robot.m_oi.driver.getX(Hand.kRight);
    double power = (alpha * throttle) + (alpham1 * lastThrottle);
    double turn = (turnAlpha * steering) + turnAlpham1 * lastSteering;
    	
    if (Robot.m_oi.driver.getTriggerAxis(Hand.kLeft) > 0.5) {
    	Robot.driveTrain.setMaxSpeed(0.6);
    } else {
    	Robot.driveTrain.setMaxSpeed(1.0);
    }
    
    Robot.driveTrain.arcadeDrive(power, turn, true);
    lastThrottle = throttle;
    lastSteering = steering;

    /*
    double throttle = -Robot.m_oi.driver.getY(Hand.kLeft);
    double turn = 0.5 * Robot.m_oi.driver.getX(Hand.kRight);
    double leftPower = throttle + turn;
    double rightPower = throttle - turn;
    if(leftPower > 1) leftPower = 1;
    if(leftPower < -1) leftPower = -1;
    if(rightPower > 1) rightPower = 1;
    if(rightPower < -1) rightPower = -1;
    if(Math.abs(leftPower) < 0.1) leftPower = 0;
    if(Math.abs(rightPower) < 0.1) rightPower = 0;
    Robot.driveTrain.setPower(leftPower, rightPower);
    */
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
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
