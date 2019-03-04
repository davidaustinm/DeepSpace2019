/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class LidarCommand extends Command {
  int numAvg = 5;
  double[] readings = new double[numAvg];
  int count = 0;
  public LidarCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.lidar);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Counter counter = Robot.lidar.getCounter();
    double cm;
    double distance = 0;
    if (counter.get() < 1) {
      System.out.println("waiting for distance");
    } else{ 
      cm = (counter.getPeriod() * 100000.0);
      distance= cm/2.54;
    }
    readings[count] = distance;
    count ++;
    if (count >= numAvg) count = 0;
    double sum = 0;
    for (int i = 0; i < numAvg; i++) {
      sum += readings[i];
    }
    Robot.lidar.setDistance(sum/numAvg);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}