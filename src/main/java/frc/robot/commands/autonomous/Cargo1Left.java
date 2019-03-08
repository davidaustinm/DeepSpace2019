/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.*;

public class Cargo1Left extends CommandGroup {
  /**
   * Add your docs here.
   */
  public Cargo1Left() {
    addParallel(new ReadyIntake());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/cargo1.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(true));
    addSequential(new SwitchDirection());
    addSequential(new DriveForwardForDistance(48, 0.5, 90));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(175, 0.6, 0.6));
    addSequential(new DriveForwardForDistance(100, 0.5, 175));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(false));
    addSequential(new DriveForwardForTime(1000, -0.5));
    
  }
}
