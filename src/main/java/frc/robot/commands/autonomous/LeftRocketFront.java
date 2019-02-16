/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.DriveForwardForDistance;
import frc.robot.commands.DriveForwardForTime;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.ExecuteDriveProfile;
import frc.robot.commands.RotateToHeading;
import frc.robot.commands.SwitchDirection;
import frc.robot.commands.Wait;

public class LeftRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LeftRocketFront() {
    //addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/left-rocket-45.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new DriveForwardForTime(350, -0.5));
    addSequential(new RotateToHeading(135, 0.5, 0.5));
    addSequential(new DriveForwardForDistance(60, 0.4, 135, true));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/backaway-from-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(-20, 0.5, 0.5));
    addSequential(new Wait(50));
    //addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/return-to-rocket.profile.csv"));
    addSequential(new DriveToTarget());
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