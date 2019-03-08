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

  double alpha = 0.45; //0.74;
  double turnAlpha = .7;
  double lastTurn = 0;
  double turnAlpham1 = 1-turnAlpha;
  double alpham1 = 1-alpha;
  double lastThrottle = 0;
  double lastSteering = 0;
  // Called repeatedly when this Command is scheduled to run

  public void run() {
    execute();
  }
  @Override
  protected void execute() {
    if (Robot.gameState.isEndGame()) {
      double power = -0.2*Robot.oi.driver.getY(Hand.kLeft);
      Robot.driveTrain.arcadeDrive(power, 0, false);
      return;
    }
    double throttle = -Robot.oi.driver.getY(Hand.kLeft);
    double steering = -0.6*Robot.oi.driver.getX(Hand.kRight);
    double power = (alpha * throttle) + (alpham1 * lastThrottle);
    if (Math.abs(throttle) < Math.abs(lastThrottle)) power = throttle;
    double turn = (turnAlpha * steering) + turnAlpham1 * lastSteering;
    if (Robot.gameState.isEndGame() && Math.abs(turn) < 0.2) turn = 0;
    	
    if (Robot.oi.driver.getTriggerAxis(Hand.kLeft) > 0.5) {
    	Robot.driveTrain.setMaxSpeed(0.6);
    } else {
    	Robot.driveTrain.setMaxSpeed(1.0);
    }
    
    Robot.driveTrain.arcadeDrive(power, turn, true);
    lastThrottle = power;
    lastSteering = steering;
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
