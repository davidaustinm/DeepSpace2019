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

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.ArcadeDriveCommand;

/**
 * Add your docs here.
 */
public class PowerUpDriveTrain extends Subsystem {
  private TalonSRX leftMaster, leftSlave, rightMaster, rightSlave;
  boolean switched = false;
	
	public PowerUpDriveTrain() {
		leftMaster = new TalonSRX(2);
		leftSlave = new TalonSRX(1);
		rightMaster = new TalonSRX(5);
		rightSlave = new TalonSRX(4);

		leftSlave.follow(leftMaster);
		rightSlave.follow(rightMaster);
		
		rightMaster.setInverted(true);
		rightSlave.setInverted(true);
		
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		
		leftMaster.setSensorPhase(true);
		rightMaster.setSensorPhase(true);

	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new ArcadeDriveCommand());
    }

	public void setPower(double left, double right) {
    if (switched) {
      left *= -1;
      right *= -1;
    }

		if (Math.abs(left) < 0.05) left = 0;
		if (Math.abs(right) < 0.05) right = 0;
		leftMaster.set(ControlMode.PercentOutput, left);
		rightMaster.set(ControlMode.PercentOutput, right);
  }
  
  public double[] getDriveEncoders() {
    double switchFactor = switched ? -1 : 1;
    return new double[] {
      switchFactor*leftMaster.getSelectedSensorPosition(0), switchFactor*rightMaster.getSelectedSensorPosition(0)
    };
  }
	
	public int getLeftPosition() {
		return -leftMaster.getSelectedSensorPosition(0);
	}
	
	public int getRightPosition() {
		return -rightMaster.getSelectedSensorPosition(0);
  }

  public boolean isSwitched() {
    return switched;
  }
  
  public void switchDirection() {
    TalonSRX tmp1 = leftMaster;
    leftMaster = rightMaster;
    rightMaster = tmp1;
    switched = !switched;
    Robot.sensors.resetDriveEncoders();
  }
}
