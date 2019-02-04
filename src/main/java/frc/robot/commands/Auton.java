/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Auton extends CommandGroup {
  /**
   * Add your docs here.
   */
  public Auton() {
    double x1 = 108;
    double y1 = -119;
    addSequential(new DriveForwardForDistance(25, 0.5, 0));
    addSequential(new RotateToPoint(x1, y1, 0.6, 0.6));
    addSequential(new DriveToPoint(x1, y1, 0.6));
    addSequential(new Wait(250));
    addSequential(new DriveForwardForTime(300, -0.7));
    double x2 = 100;
    double y2 = 10;
    addSequential(new RotateToPoint(x2, y2, 0.6, 0.6));
    addSequential(new DriveToPoint(x2, y2, 0.6));
    addSequential(new DriveForwardForTime(500, -0.8));
    addSequential(new RotateToPoint(x1, y1, 0.6, 0.6));
    addSequential(new DriveToPoint(x1, y1, 0.6));

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
