/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.*;
import frc.robot.commands.ExecuteDriveProfile;

public class Cargo2Left extends CommandGroup {
  /**
   * Add your docs here.
   */
  public Cargo2Left() {
    addParallel(new ReadyIntake());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/cargo2.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(true));
    
  }
}
