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
public class GameState {
    public static int ENDGAME = 0;
    public static int TELEOP = 1;
    int state = TELEOP;
    public void setState(int state) {
        this.state = state;
    }
    public boolean isEndGame() {
        return state == ENDGAME;
    }
}
