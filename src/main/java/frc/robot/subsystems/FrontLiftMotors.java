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
  public static final int GROUND_LEVEL = 0;
  public static final int PANEL_LEVEL1 = 1;
  public static final int CARGO_LEVEL1 = 2;
  public static final int PANEL_LEVEL2 = 3;
  public static final int CARGO_LEVEL2 = 4;
  public static final int PANEL_LEVEL3 = 5;
  public static final int CARGO_LEVEL3 = 6;

  public static final int GROUND_MODE = 0;
  public static final int CARGO_MODE = 0;

  int[][] levels = new int[3][3];


  boolean manual = false;
  int level = GROUND_LEVEL;
  int holdPosition = 0;

  TalonSRX frontLift1 = new TalonSRX(RobotMap.FRONT_LIFT1);
  TalonSRX frontLift2 = new TalonSRX(RobotMap.FRONT_LIFT2);

  public FrontLiftMotors() {
    frontLift2.follow(frontLift1);
    /*
    levels[GROUND_LEVEL] = 0;
    levels[PANEL_LEVEL1] = 0;
    levels[CARGO_LEVEL1] = 0;
    levels[PANEL_LEVEL2] = 0;
    levels[CARGO_LEVEL2] = 0;
    levels[PANEL_LEVEL3] = 0;
    levels[CARGO_LEVEL3] = 0;
    */
  }

  //public void setMode() 

  public void setLevel(int m, int l) {
    level = l;
    manual = false;
    setHoldPosition(levels[m][l]);
  }

  public void setHoldPosition(int p) {
    holdPosition = p;
  }

  public int getHoldPosition() {
    return holdPosition;
  }

  public void setPower(double power) {
    if (Math.abs(power) < 0.05) power = 0;
    frontLift1.set(ControlMode.PercentOutput, power);
  }

  public int getPosition() {
		return frontLift1.getSelectedSensorPosition(0);
	}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new FrontLiftCommand());
  }
}
