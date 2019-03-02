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
    public static final int VAC_ON = 2;
    public static final int VAC_OFF = 3;
    int state = REST;

    public void setMode(int mode) {
        if(this.mode == mode) return;
        this.mode = mode;
        if(state == EXTEND) setVacuumState();
    }

    public void setVacuumState() {
        if (mode == COLLECT) {
            
        }
        else {
        }
    }

    public void advanceState() {
        /*
        System.out.println(state + " " + mode);
        switch(state) {
            case REST: {
                Robot.pneumatics.setState(Pneumatics.PUSHER, true);
                state = EXTEND;
                break;
            }
            case EXTEND: {
                Robot.vacState.setState(VacuumState.ON);
                state = VAC_ON;
                break;
            }
            case VAC_ON: {
                Robot.vacState.setState(VacuumState.RELEASE);
                state = VAC_OFF;
                break;
            }
            case VAC_OFF: {
                Robot.pneumatics.setState(Pneumatics.PUSHER, false);
                state = REST;
                break;
            }

        }
        */
    }
}
