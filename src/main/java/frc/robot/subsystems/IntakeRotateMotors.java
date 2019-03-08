/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.IntakeRotateCommand;

/**
 * Add your docs here.
 */
public class IntakeRotateMotors extends Subsystem {
  TalonSRX rotateMotor = new TalonSRX(RobotMap.ROTATE_MOTOR);
  int state = IntakeRotateCommand.IN;
  int encoderOffset = 0;
  boolean manual = false;
  public IntakeRotateMotors() {
    rotateMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
  }

  public void setPower(double power) {
    if (Math.abs(power) < 0.05) power = 0;
    rotateMotor.set(ControlMode.PercentOutput, power);
  }

  public void setManual(boolean b) {
    manual = b;
  }

  public boolean getManual() {
    return manual;
  }

  public int getPosition() {
    return rotateMotor.getSelectedSensorPosition(0) - encoderOffset;
  }

  public void resetOffset() {
    encoderOffset = rotateMotor.getSelectedSensorPosition(0);
  }

  public void setState(int state) {
    this.state = state;
  }

  public int getState() {
    return state;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new IntakeRotateCommand());
  }
}
