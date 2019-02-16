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
  public static final int SHIFTER_EXTEND = 3;
  public static final int SHIFTER_RETRACT = 4;
  public static final int PUSHER = 5;
  int numValves = 6;
  public static final int SHIFT = 6;
  String[] labels = new String[] {
      "Shifter Extend","Shifter Retract","RF Latch","Front Lift Shift","Vac Sys", "Pusher"
  };

  Solenoid[] solenoids = new Solenoid[5];
  boolean[] states = new boolean[5];

  public Pneumatics() {
    solenoids[RF_LATCH] = new Solenoid(RobotMap.RF_LATCH);
    solenoids[FRONT_LIFT_SHIFT] = new Solenoid(RobotMap.FRONT_LIFT_SHIFT);
    solenoids[VAC_SYS] = new Solenoid(RobotMap.VAC_SYS);
    solenoids[SHIFTER_EXTEND] = new Solenoid(RobotMap.SHIFTER_EXTEND);
    solenoids[SHIFTER_RETRACT] = new Solenoid(RobotMap.SHIFTER_RETRACT);
    solenoids[PUSHER] = new Solenoid(RobotMap.PUSHER);
    for(int i = 0; i < numValves; i++) states[i] = false;
  }

  public void setState(int valve, boolean state) {
    if(valve == SHIFT){
      states[SHIFTER_EXTEND] = state;
      states[SHIFTER_RETRACT] = !state;
    } else {
      states[valve] = state;
    } 
  }

  public void toggleState(int valve) {
    states[valve] = !states[valve];
  }

  public void update() {
    solenoids[RF_LATCH].set(states[RF_LATCH]);
    solenoids[FRONT_LIFT_SHIFT].set(states[FRONT_LIFT_SHIFT]);
    solenoids[VAC_SYS].set(states[VAC_SYS]);
    solenoids[SHIFTER_EXTEND].set(states[SHIFTER_EXTEND]);
    solenoids[SHIFTER_RETRACT].set(states[SHIFTER_RETRACT]);
    solenoids[PUSHER].set(states[PUSHER]);
  }

  public boolean getState(int valve) {
    if (valve == SHIFT) return states[SHIFTER_EXTEND];
    return states[valve];
  }
    
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new PneumaticsCommand());
  }
}
