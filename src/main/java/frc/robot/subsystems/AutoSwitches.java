/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.autonomous.*;

/**
 * Add your docs here.
 */
public class AutoSwitches extends Subsystem {
  public static final int ROCKET = 0;
  public static final int CARGO = 1;
  int target = ROCKET;
  public static final int FRONT = 0;
  public static final int BACK = 1;
  int side = FRONT;
  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  int direction = LEFT;

  int numSwitches = 6;
  DigitalInput[] switches = new DigitalInput[numSwitches];
  public AutoSwitches() {
    switches[0] = new DigitalInput(RobotMap.AUTOSWITCH0);
    switches[1] = new DigitalInput(RobotMap.AUTOSWITCH1);
    switches[2] = new DigitalInput(RobotMap.AUTOSWITCH2);
    switches[3] = new DigitalInput(RobotMap.AUTOSWITCH3);
    switches[4] = new DigitalInput(RobotMap.AUTOSWITCH4);
    switches[5] = new DigitalInput(RobotMap.AUTOSWITCH5);
  }

  public Command getAutonCommand() {
    if (getSwitch(2)) target = CARGO;
    else target = ROCKET;
    if (getSwitch(3)) side = BACK;
    else side = FRONT;
    if (getSwitch(0)) return getCenterCommand();
    if (getSwitch(1)) return getRightCommand();
    return getLeftCommand();
  }

  public boolean getSwitch(int s){
    return !switches[s].get();
  }

  public Command getCenterCommand() {
    direction = getSwitch(4) ? RIGHT:LEFT;
    if (target == CARGO) {      // going to cargo ship
      if (side == FRONT) return new CenterCargoFront();
      else {
        if (direction == LEFT) return new Cargo1Left();
        else return new Cargo1Right();
      }
    } else {
        if (direction == LEFT) return new CenterLeftRocketBack();
        else return new CenterRightRocketBack();
    }

  }

  public Command getLeftCommand() {
    if (target == CARGO) return new LeftLeftCargo1();
    else {
      if (side == FRONT) return new LeftRocketFrontBack();
      else return new LeftLeftRocketBack();
    }
  }

  public Command getRightCommand() {
    if (target == CARGO) return new RightRightCargo1();
    else {
      if (side == FRONT) return new RightRocketFrontBack();
      else return new RightRightRocketBack();
    }
  }

  public boolean getManualMode() {
    return getSwitch(5);
  }

  public void printState() {
    String s = "";
    for (int i = 0; i < switches.length; i++) {
      s += getSwitch(i) + " ";
    }
    //System.out.println(s);
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
