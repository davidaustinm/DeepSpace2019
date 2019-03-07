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

public class IntakeRotateCommand extends Command {
  public static int OUT = 0;
  public static int IN = 1;
  public static int END = 2;
  public static int MANUAL = 3;
  int state = IN;
  double encoderIn = 0;
  double encoderOut = 2250;
  double encoderEnd = 2700;
  double[] encoderStops = new double[] {encoderOut, encoderIn, encoderEnd, 0};
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
  double Kp = 0.00075;
  double Kd = 0.00075;
  double lastError = 0;
  double maxPower = 0.6;
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double operatorPower = Robot.oi.operator.getY(Hand.kLeft);
    if (Math.abs(operatorPower) > 0.2) {
      Robot.intakeRotate.setState(MANUAL);
      encoderStops[MANUAL] = Robot.sensors.getIntakeRotatePosition();
      Robot.intakeRotate.setPower(operatorPower * maxPower);
      return;
    }
    double error = encoderStops[Robot.intakeRotate.getState()] - 
      Robot.sensors.getIntakeRotatePosition();
    double changeInError = error - lastError;
    double power = Kp*error + Kd*changeInError;
    //power = 0.2 * Robot.oi.operator.getX(Hand.kLeft);
    Robot.intakeRotate.setPower(power * maxPower);
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
