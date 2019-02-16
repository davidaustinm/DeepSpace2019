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
  public static final int RF_LATCH = 0;
  public static final int FRONT_LIFT_SHIFT = 1;
  public static final int VAC_SYS = 2;
  public static final int SHIFT = 3;
  public static final int PUSHER = 4;
  int numValves = 5;
  String[] labels = new String[] {
      "RF Latch","Front Lift Shift","Vac Sys","Shift", "Pusher"
  };

  Solenoid[] solenoids = new Solenoid[5];
  boolean[] states = new boolean[5];

  public Pneumatics() {
    solenoids[RF_LATCH] = new Solenoid(RobotMap.RF_LATCH);
    solenoids[FRONT_LIFT_SHIFT] = new Solenoid(RobotMap.FRONT_LIFT_SHIFT);
    solenoids[VAC_SYS] = new Solenoid(RobotMap.VAC_SYS);
    solenoids[SHIFT] = new Solenoid(RobotMap.SHIFTER_EXTEND);
    solenoids[PUSHER] = new Solenoid(RobotMap.PUSHER);
    for(int i = 0; i < numValves; i++) states[i] = false;
  }

  public void setState(int valve, boolean state) {
    states[valve] = state;
  }

  public void toggleState(int valve) {
    states[valve] = !states[valve];
  }

  public void update() {
    solenoids[RF_LATCH].set(states[RF_LATCH]);
    solenoids[FRONT_LIFT_SHIFT].set(states[FRONT_LIFT_SHIFT]);
    solenoids[VAC_SYS].set(states[VAC_SYS]);
    solenoids[SHIFT].set(states[SHIFT]);
    solenoids[PUSHER].set(states[PUSHER]);
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
