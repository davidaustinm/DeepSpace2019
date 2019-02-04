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
public class SparkDriveTrain extends Subsystem {
  CANSparkMax left1, left2, right1, right2;
  CANEncoder leftEncoder, rightEncoder;
  boolean switched = false;
  public SparkDriveTrain() {
    left1 = new CANSparkMax(3,MotorType.kBrushless);
    left2 = new CANSparkMax(4,MotorType.kBrushless);
    right1 = new CANSparkMax(1,MotorType.kBrushless);
    right2 = new CANSparkMax(2, MotorType.kBrushless);
    right1.setInverted(true);
    right2.setInverted(true);
   // left1.setInverted(true);
   // left2.setInverted(true);
    leftEncoder = left2.getEncoder();
    rightEncoder = right2.getEncoder();

    left1.setIdleMode(IdleMode.kBrake);
    left2.setIdleMode(IdleMode.kBrake);
    right1.setIdleMode(IdleMode.kBrake);
    right2.setIdleMode(IdleMode.kBrake);
    
    left1.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    left2.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    right1.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
    right2.setPeriodicFramePeriod(PeriodicFrame.kStatus2,10);
  }

  public void setPower(double left, double right) {
    if (switched) {
      left *= -1;
      right *= -1;
    }
    left1.set(left);
    left2.set(left);
    right1.set(right);
    right2.set(right);
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
    CANSparkMax tmp1 = left1;
    CANSparkMax tmp2 = left2;
    left1 = right1;
    left2 = right2;
    right1 = tmp1;
    right2 = tmp2;
    switched = !switched;
    Robot.sensors.resetDriveEncoders();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new ArcadeDriveCommand());
  }
}
