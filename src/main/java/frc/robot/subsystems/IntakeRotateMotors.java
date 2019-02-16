/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.IntakeRotateCommand;

/**
 * Add your docs here.
 */
public class IntakeRotateMotors extends Subsystem {
  VictorSPX rotateMotor = new VictorSPX(RobotMap.ROTATE_MOTOR);
  int state = IntakeRotateCommand.IN;

  public void setPower(double power) {
    if (Math.abs(power) < 0.05) power = 0;
    rotateMotor.set(ControlMode.PercentOutput, power);
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
    // setDefaultCommand(new MySpecialCommand());
  }
}
