
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.commands.DriveTrainCommand;

/**
 * Add your docs here.
 */
public class DeepSpaceDriveTrain extends Subsystem {
  CANSparkMax leftSlave1, leftMaster, leftSlave2, rightSlave1, rightMaster, rightSlave2;
  CANEncoder leftEncoder, rightEncoder;
  double maxSpeed = 1;
  boolean switched = false;
  public DeepSpaceDriveTrain() {
    rightSlave1 = new CANSparkMax(4,MotorType.kBrushless);
    rightMaster = new CANSparkMax(3,MotorType.kBrushless);
    rightSlave2 = new CANSparkMax(6,MotorType.kBrushless);
    leftSlave1 = new CANSparkMax(1,MotorType.kBrushless);
    leftMaster = new CANSparkMax(2, MotorType.kBrushless);
    leftSlave2 = new CANSparkMax(9,MotorType.kBrushless);
    leftSlave1.setInverted(true);
    rightMaster.setInverted(true);
    rightSlave2.setInverted(true);
    
    leftEncoder = leftMaster.getEncoder();
    rightEncoder = rightMaster.getEncoder();
    /*
    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);

    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    */

    leftMaster.setIdleMode(IdleMode.kBrake);
    leftSlave1.setIdleMode(IdleMode.kBrake);
    leftSlave2.setIdleMode(IdleMode.kBrake);
    rightMaster.setIdleMode(IdleMode.kBrake);
    rightSlave1.setIdleMode(IdleMode.kBrake);
    rightSlave2.setIdleMode(IdleMode.kBrake);
    
    leftSlave1.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    leftMaster.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    leftSlave2.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    rightSlave1.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    rightMaster.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    rightSlave2.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
  }

  public void setMaxSpeed(double speed) {
    maxSpeed = speed;
  }

  public void setPower(double left, double right) {
    if (switched) {
      left *= -1;
      right *= -1;
    }
    /*
    SmartDashboard.putNumber("rightMaster", rightMaster.getOutputCurrent());
    SmartDashboard.putNumber("rightSlave1", rightSlave1.getOutputCurrent());
    SmartDashboard.putNumber("rightSlave2", rightSlave2.getOutputCurrent());
    SmartDashboard.putNumber("leftMaster", leftMaster.getOutputCurrent());
    SmartDashboard.putNumber("leftSlave1", leftSlave1.getOutputCurrent());
    SmartDashboard.putNumber("leftSlave2", leftSlave2.getOutputCurrent());
    */

    leftMaster.set(left);
    leftSlave1.set(left);
    leftSlave2.set(left);
    
    rightMaster.set(right);
    rightSlave1.set(right);
    rightSlave2.set(right);
  }

  double xLimit = 1;
  double yLimit = 1;
  public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs) {

    double leftMotorSpeed;
    double rightMotorSpeed;
    moveValue *= yLimit;
    rotateValue *= xLimit;

    if (squaredInputs) {
      // square the inputs (while preserving the sign) to increase fine control
      // while permitting full power
      if (moveValue >= 0.0) {
        moveValue = moveValue * moveValue;
      } else {
        moveValue = -(moveValue * moveValue);
      }
      if (rotateValue >= 0.0) {
        rotateValue = rotateValue * rotateValue;
      } else {
        rotateValue = -(rotateValue * rotateValue);
      }
    }

    if (moveValue > 0.0) {
      if (rotateValue > 0.0) {
        leftMotorSpeed = moveValue - rotateValue;
        rightMotorSpeed = Math.max(moveValue, rotateValue);
      } else {
        leftMotorSpeed = Math.max(moveValue, -rotateValue);
        rightMotorSpeed = moveValue + rotateValue;
      }
    } else {
      if (rotateValue > 0.0) {
        leftMotorSpeed = -Math.max(-moveValue, rotateValue);
        rightMotorSpeed = moveValue + rotateValue;
      } else {
        leftMotorSpeed = moveValue - rotateValue;
        rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
      }
    }
    setPower(leftMotorSpeed, rightMotorSpeed);
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
    System.out.println("SWITCHING DRIVE TRAIN");
    CANSparkMax tmp1 = leftSlave1;
    CANSparkMax tmp2 = leftMaster;
    CANSparkMax tmp3 = leftSlave2;
    leftSlave1 = rightSlave1;
    leftMaster = rightMaster;
    leftSlave2 = rightSlave2;
    rightSlave1 = tmp1;
    rightMaster = tmp2;
    rightSlave2 = tmp3;
    switched = !switched;
    Robot.sensors.resetDriveEncoders();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new ArcadeDriveCommand());
  }
}
