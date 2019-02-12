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
import frc.robot.commands.RotateToHeading;
import frc.robot.commands.SwitchDirection;

public class LeftRocketFrontBack extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LeftRocketFrontBack() {
    addSequential(new DriveToTarget(0.4));
    addSequential(new DriveForwardForTime(350, -0.5));
    addSequential(new RotateToHeading(135, 0.5, 0.5));
    addSequential(new DriveForwardForDistance(60, 0.4, 135, true));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    addSequential(new DriveToTarget(0.4));
    addSequential(new SwitchDirection());
    addSequential(new DriveForwardForDistance(12*12, 0.6, -55, true));
    addSequential(new DriveForwardForDistance(144, 0.6, -51));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(90, 0.5, 0.5));
    addSequential(new DriveToTarget(0.4));
    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
