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
  double frontDownSpeed = -0.6;
  double rearDownSpeed = -0.6;
  final int frontBottom = 100;
  final int rearBottom = -5000;
  public RaiseRearLift() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.rearLift);
    requires(Robot.frontLift);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }
  double kp = 0.02; //2; //0.02
  double ki = 0.005; // 0.005
  double totalPitch = 0;
  double alpha = 0.8;
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.gameState.isEndGame() == false) return;
    double pitch = Robot.sensors.getPitch(); 
    if (pitch > 3) state = FRONTONLY;
    if (pitch < -3) state = REARONLY;
    if (Math.abs(pitch) < 1.5) state = BOTH;

    double frontPower = frontDownSpeed;
    if (pitch > 0) frontPower += kp*pitch + ki*totalPitch;
    double rearPower = rearDownSpeed;
    if (pitch < 0) rearPower += kp*pitch + ki*totalPitch;
    //if (state == FRONTONLY) rearPower = 0;
    //if (state == REARONLY) frontPower = 0;
    Robot.frontLift.setPower(frontPower);
    Robot.rearLift.setPower(rearPower);
    totalPitch = pitch + alpha * totalPitch;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    boolean inPosition = Robot.frontLift.getPosition() < frontBottom &&
      Robot.rearLift.getPosition() < rearBottom;
    return (Robot.gameState.isEndGame() == false) || inPosition;
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
