/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.RearLiftCommand;

/**
 * Add your docs here.
 */
public class RearLiftMotors extends Subsystem {
  TalonSRX lift1 = new TalonSRX(RobotMap.REAR_LIFT1);
  TalonSRX lift2 = new TalonSRX(RobotMap.REAR_LIFT2);

  int encoderOffset = 0;

  public RearLiftMotors(){
    lift1.follow(lift2);
    lift1.setNeutralMode(NeutralMode.Brake);
    lift2.setNeutralMode(NeutralMode.Brake);
    lift2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
  }

  public void setPower(double power) {
    //System.out.println(power);
    if (Math.abs(power) < 0.1) power = 0;
    if (power > 0 && getPosition() > 0) power = 0;
    if (power < 0 && getPosition() < -17800) power = 0;
    lift2.set(ControlMode.PercentOutput, power);
    
  }

  public void resetEncoder() {
    encoderOffset = lift2.getSelectedSensorPosition(0);
  }

  public double getPosition() {
    return lift2.getSelectedSensorPosition(0) - encoderOffset;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new RearLiftCommand());
  }
}
