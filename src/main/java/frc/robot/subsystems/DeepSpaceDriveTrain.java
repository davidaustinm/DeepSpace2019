
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.DriveTrainCommand;

/**
 * Add your docs here.
 */
public class DeepSpaceDriveTrain extends Subsystem {
  CANSparkMax leftSlave1, leftMaster, leftSlave2, rightSlave1, rightMaster, rightSlave2;
  CANEncoder leftEncoder, rightEncoder;
  boolean switched = false;
  public DeepSpaceDriveTrain() {
    leftSlave1 = new CANSparkMax(3,MotorType.kBrushless);
    leftMaster = new CANSparkMax(4,MotorType.kBrushless);
    leftSlave2 = new CANSparkMax(6,MotorType.kBrushless);
    rightSlave1 = new CANSparkMax(1,MotorType.kBrushless);
    rightMaster = new CANSparkMax(2, MotorType.kBrushless);
    rightSlave2 = new CANSparkMax(9,MotorType.kBrushless);
    rightSlave1.setInverted(true);
    rightMaster.setInverted(true);
    rightSlave2.setInverted(true);
    //leftSlave1.setInverted(true);
    // leftMaster.setInverted(true);
    // leftSlave2.setInverted(true);
    leftEncoder = leftMaster.getEncoder();
    rightEncoder = rightMaster.getEncoder();

    leftSlave1.setIdleMode(IdleMode.kBrake);
    leftMaster.setIdleMode(IdleMode.kBrake);
    leftSlave2.setIdleMode(IdleMode.kBrake);
    rightSlave1.setIdleMode(IdleMode.kBrake);
    rightMaster.setIdleMode(IdleMode.kBrake);
    rightSlave2.setIdleMode(IdleMode.kBrake);
    
    leftSlave1.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    leftMaster.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    leftSlave2.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    rightSlave1.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    rightMaster.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    rightSlave2.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
  }

  public void setPower(double left, double right) {
    if (switched) {
      left *= -1;
      right *= -1;
    }
    leftSlave1.set(left);
    leftMaster.set(left);
    leftSlave2.set(left);
    rightSlave1.set(right);
    rightMaster.set(right);
    rightSlave2.set(right);
  }

  public double[] getDriveEncoders() {
    if (switched) {
      return new double[] {
        rightEncoder.getPosition(),
        -leftEncoder.getPosition()
      };
    } else {
      return new double[] {
        leftEncoder.getPosition(),
        -rightEncoder.getPosition()
      };
    }
  }

  public boolean isSwitched() {
    return switched;
  }

  public void switchDirection() {
    CANSparkMax tmp1 = leftSlave1;
    CANSparkMax tmp2 = leftMaster;
    leftSlave1 = rightSlave1;
    leftMaster = rightMaster;
    rightSlave1 = tmp1;
    rightMaster = tmp2;
    switched = !switched;
    Robot.sensors.resetDriveEncoders();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new ArcadeDriveCommand());
  }
}
