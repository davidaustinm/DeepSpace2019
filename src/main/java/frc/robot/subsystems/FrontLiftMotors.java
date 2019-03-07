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
import frc.robot.commands.FrontLiftCommand;

public class FrontLiftMotors extends Subsystem {
  public static final int LEVEL_GROUND = 0;
  public static final int LEVEL_1 = 1;
  public static final int LEVEL_2 = 2;
  public static final int LEVEL_3 = 3;
  

  public static final int CARGO_MODE = 0;
  public static final int PANEL_MODE = 1;

  int[][] levels = new int[2][4];


  boolean manual = false;
  int level = LEVEL_GROUND;
  int holdPosition = 0;
  int encoderOffset = 0;
  int mode = PANEL_MODE;

  TalonSRX frontLift1 = new TalonSRX(RobotMap.FRONT_LIFT1);
  TalonSRX frontLift2 = new TalonSRX(RobotMap.FRONT_LIFT2);

  public FrontLiftMotors() {
    frontLift1.setNeutralMode(NeutralMode.Brake);
    frontLift2.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    frontLift2.setNeutralMode(NeutralMode.Brake);
    frontLift1.follow(frontLift2);
    frontLift1.setInverted(true);
    frontLift2.setInverted(true);
    levels[CARGO_MODE][LEVEL_GROUND] = 0;
    levels[CARGO_MODE][LEVEL_1] = 9400;
    levels[CARGO_MODE][LEVEL_2] = 20700;
    levels[CARGO_MODE][LEVEL_3] = 30000;
    levels[PANEL_MODE][LEVEL_GROUND] = 0;
    levels[PANEL_MODE][LEVEL_1] = 2400;
    levels[PANEL_MODE][LEVEL_2] = 13900;
    levels[PANEL_MODE][LEVEL_3] = 25500;
  }

  public void setManual(boolean b) {
    manual = b;
  }

  public void setMode(int m) {
    mode = m;
  } 

  public void setLevel(int m, int l) {
    level = l;
    mode = m;
    manual = false;
    setHoldPosition(levels[mode][l]);
  }

  public void setHoldPosition(int p) {
    holdPosition = p;
  }

  public int getHoldPosition() {
    return holdPosition;
  }

  public void setPower(double power) {
    if (Math.abs(power) < 0.05) power = 0;
    //power *= 0.5;
    frontLift2.set(ControlMode.PercentOutput, power);
  }

  public void resetEncoder() {
    encoderOffset = frontLift2.getSelectedSensorPosition(0);
  }

  public int getPosition() {
		return frontLift2.getSelectedSensorPosition(0) - encoderOffset;
	}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new FrontLiftCommand());
  }
}
