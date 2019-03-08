/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.*;

public class LeftRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LeftRocketFront() {
    //addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/left-rocket-45.profile.csv"));
    addParallel(new ReadyIntake());
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(true));
    addSequential(new DriveForwardForTime(350, -0.5));
    addSequential(new RotateToHeading(135, 0.5, 0.5));
    addSequential(new DriveForwardForDistance(60, 0.4, 135, true));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(false));
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/backaway-from-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(-20, 0.5, 0.5));
    addSequential(new Wait(50));
    //addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/return-to-rocket.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(true));
    
    /*
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/left-rocket-front.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(newew ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/backaway-from-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(-75, 0.6, 0.6));
    addSequential(new Wait(50));
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/return-to-rocket.profile.csv"));
    */
  }
}