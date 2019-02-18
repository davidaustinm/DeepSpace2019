/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.PneumaticsCommand;

/**
 * Add your docs here.
 */
public class Pneumatics extends Subsystem {
  public static final int SHIFT = 0;
  public static final int FRONT_LIFT_SHIFT = 1;
  public static final int RF_LATCH = 2;
  //public static final int NOTHING = 3;
  public static final int PUSHER = 3;
  public static final int VAC_VAC_ON = 4;
  public static final int VAC_VAC_OFF = 5;
  public static final int VAC_POWER = 6;

  int numValves = 7;
  String[] labels = new String[] {
      "RF Latch","Front Lift Shift","Vac Sys","Shift", "Pusher"
  };

  Solenoid[] solenoids = new Solenoid[7];
  boolean[] states = new boolean[7];

  public Pneumatics() {
    solenoids[SHIFT] = new Solenoid(RobotMap.SHIFT);
    solenoids[FRONT_LIFT_SHIFT] = new Solenoid(RobotMap.FRONT_LIFT_SHIFT);
    solenoids[RF_LATCH] = new Solenoid(RobotMap.RF_LATCH);
    solenoids[PUSHER] = new Solenoid(RobotMap.PUSHER);
    solenoids[VAC_VAC_ON] = new Solenoid(RobotMap.VAC_VAC_ON);
    solenoids[VAC_VAC_OFF] = new Solenoid(RobotMap.VAC_VAC_OFF);
    solenoids[VAC_POWER] = new Solenoid(RobotMap.VAC_POWER);
    for(int i = 0; i < numValves - 1; i++) states[i] = false;
    states[VAC_POWER] = true;
  }

  public void setState(int valve, boolean state) {
    System.out.println(valve + " " + state);
    states[valve] = state;
  }

  public void toggleState(int valve) {
    states[valve] = !states[valve];
  }

  public void update() {
    solenoids[SHIFT].set(states[SHIFT]);
    solenoids[FRONT_LIFT_SHIFT].set(states[FRONT_LIFT_SHIFT]);
    solenoids[RF_LATCH].set(states[RF_LATCH]);
    solenoids[PUSHER].set(states[PUSHER]);
    solenoids[VAC_POWER].set(states[VAC_POWER]);
    solenoids[VAC_VAC_ON].set(states[VAC_VAC_ON]);
    solenoids[VAC_VAC_OFF].set(states[VAC_VAC_OFF]);
  }

  public boolean getState(int valve) {
    return states[valve];
  }
    
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new PneumaticsCommand());
  }
}
