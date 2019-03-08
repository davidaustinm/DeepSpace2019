/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.AutoActivatePusher;
import frc.robot.commands.DriveForwardForDistance;
import frc.robot.commands.DriveForwardForTime;
import frc.robot.commands.DriveToTarget;
import frc.robot.commands.DriveToTarget2;
import frc.robot.commands.ExecuteDriveProfile;
import frc.robot.commands.ReadyIntake;
import frc.robot.commands.RotateToHeading;
import frc.robot.commands.SwitchDirection;
import frc.robot.commands.Wait;

public class RightRocketFront extends CommandGroup {
  /**
   * Add your docs here.
   */
  public RightRocketFront() {
    addParallel(new ReadyIntake());
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(true));
    addSequential(new DriveForwardForTime(350, -0.5));
    addSequential(new RotateToHeading(-135, 0.5, 0.5));
    addSequential(new DriveForwardForDistance(60, 0.4, -135, true));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    // addSequential(new SwitchDirection());
    // addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(false));
    addSequential(new SwitchDirection());
    addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/right-backaway-from-portal.profile.csv"));
    addSequential(new SwitchDirection());
    addSequential(new RotateToHeading(20, 0.5, 0.5));
    addSequential(new Wait(50));
    //addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/return-to-rocket.profile.csv"));
    addSequential(new DriveToTarget());
    addSequential(new AutoActivatePusher(true));
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
