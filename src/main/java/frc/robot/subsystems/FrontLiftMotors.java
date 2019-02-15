/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.FrontLiftCommand;

public class FrontLiftMotors extends Subsystem {
  TalonSRX frontLift1 = new TalonSRX(RobotMap.FRONT_LIFT1);
  TalonSRX frontLift2 = new TalonSRX(RobotMap.FRONT_LIFT2);

  public FrontLiftMotors() {
    frontLift2.follow(frontLift1);
  }

  public void setPower(double power) {
    frontLift1.set(ControlMode.PercentOutput, power);
    frontLift2.set(ControlMode.PercentOutput, power);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new FrontLiftCommand());
  }
}
