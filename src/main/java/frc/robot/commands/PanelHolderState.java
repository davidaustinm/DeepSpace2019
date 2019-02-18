/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.Pneumatics;

public class PanelHolderState {
    public static final int COLLECT = 0;
    public static final int PLACE = 1;
    int mode = COLLECT;
    public static final int REST = 0;
    public static final int EXTEND = 1;
    public static final int HANDLE = 2;
    public static final int RETRACT = 3;
    int state = REST;

    public void setMode(int mode) {
        if(this.mode == mode) return;
        this.mode = mode;
        if(state == EXTEND) setVacuumState();
    }

    public void setVacuumState() {
        if (mode == COLLECT) {
            Robot.pneumatics.setState(Pneumatics.VAC_VAC_ON, true);
            Robot.pneumatics.setState(Pneumatics.VAC_VAC_OFF, false);
        }
        else {
            Robot.pneumatics.setState(Pneumatics.VAC_VAC_ON, false);
            Robot.pneumatics.setState(Pneumatics.VAC_VAC_OFF, true);
        }
    }

    public void advanceState() {
        switch(state) {
            case REST: {
                Robot.pneumatics.setState(Pneumatics.PUSHER, true);
                state = EXTEND;
                break;
            }
            case EXTEND: {
                setVacuumState();
                state = HANDLE;
                break;
            }
            case HANDLE: {
                if(!Robot.sensors.getVacSense()){
                    return;
                }
                Robot.pneumatics.setState(Pneumatics.PUSHER, false);
                state = REST;
                break;
            }

        }
    }
}
