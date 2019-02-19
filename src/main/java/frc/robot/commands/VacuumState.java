/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

/**
 * Add your docs here.
 */
public class VacuumState {
  public static final int REST = 0;
  public static final int ON = 1;
  public static final int RELEASE = 2;

  int state = REST;
  boolean blocking = false;
  long endBlockTime = 0;

  public void setState(int newState) {
    state = newState;
  }

  public int getState() {
      return state;
  }
  
}
