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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotMap;
import frc.robot.commands.IntakeRollerCommand;

/**
 * Add your docs here.
 */
public class IntakeRollerMotors extends Subsystem {
  VictorSPX rollerMotor = new VictorSPX(RobotMap.ROLLER_MOTOR);

  public void setPower(double power) {
    if (Math.abs(power) < 0.05) power = 0;
    rollerMotor.set(ControlMode.PercentOutput, power);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new IntakeRollerCommand());
  }
}
