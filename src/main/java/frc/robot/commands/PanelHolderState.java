/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.Pneumatics;

/**
 * Add your docs here.
 */
public class PanelHolderState {
    public static final int COLLECT = 0;
    public static final int PLACE = 1;
    int mode = COLLECT;
    public static final int REST = 0;
    public static final int EXTEND = 1;
    public static final int HANDLE = 2;
    public static final int RETRACT = 3;
    int state = REST;

    public void advanceState() {
        switch(state) {
            case REST: {
                Robot.pneumatics.setState(Pneumatics.PUSHER, true);
                state = EXTEND;
                break;
            }
            case EXTEND: {
                if (mode == COLLECT) Robot.pneumatics.setState(Pneumatics.VAC_SYS, true);
                else Robot.pneumatics.setState(Pneumatics.VAC_SYS, false);
                state = HANDLE;
                break;
            }
            case HANDLE: {
                Robot.pneumatics.setState(Pneumatics.PUSHER, false);
                state = REST;
                break;
            }

        }
    }
}
