/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.ExecuteDriveProfile;

public class LeftRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LeftRocketFront() {
    CommandGroup auto = new CommandGroup();
    // System.out.println("test 1");
    auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/left-rocket-45.profile.csv"));
    // System.out.println("test 2");
    auto.addSequential(new DriveToTarget(0.4));
    // System.out.println("test 3");
    //auto.addSequential(new SwitchDirection());
    //auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    //auto.addSequential(new SwitchDirection());
    //auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    //auto.addSequential(new DriveToTarget(0.4));
   
    /*
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/left-rocket-front.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/backaway-from-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(-75, 0.6, 0.6));
    addSequential(new Wait(50));
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/return-to-rocket.profile.csv"));
    */
  }
}
