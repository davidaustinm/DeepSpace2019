/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class IntakeRotateCommand extends Command {
  public static int OUT = 0;
  public static int IN = 1;
  int state = IN;
  double encoderOut = 0;
  double encoderIn = 100;
  double[] encoderStops = new double[] {encoderOut, encoderIn};
  public IntakeRotateCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.intakeRotate);
  }

  public void setState(int state) {
    this.state = state;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }
  double Kp = 0.0001;
  double Kd = 0.0001;
  double lastError = 0;
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double error = encoderStops[Robot.intakeRotate.getState()] - 
      Robot.sensors.getIntakeRotatePosition();
    double changeInError = error - lastError;
    double power = Kp*error + Kd*changeInError;
    Robot.intakeRotate.setPower(power);
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
