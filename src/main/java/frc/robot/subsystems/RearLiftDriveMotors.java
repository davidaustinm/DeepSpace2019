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
import frc.robot.commands.RearDriveCommand;

/**
 * Add your docs here.
 */
public class RearLiftDriveMotors extends Subsystem {
  VictorSPX liftDriveMotor = new VictorSPX(RobotMap.REAR_LIFT_DRIVE_MOTOR);

  public void setPower(double power) {
    if (Math.abs(power) < 0.1) power = 0;
    liftDriveMotor.set(ControlMode.PercentOutput, power);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new RearDriveCommand());
  }
}
