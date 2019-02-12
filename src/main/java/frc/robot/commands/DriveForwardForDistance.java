/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class DriveForwardForDistance extends Command {
  double encoderTarget;
  double distance;
  double speed;
  double heading;
  boolean coast;
  public DriveForwardForDistance(double distance, double speed, double heading, boolean coast) {
    this.distance = distance;
    this.speed = speed;
    this.heading = heading;
    this.coast = coast;
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }
  public DriveForwardForDistance(double distance, double speed, double heading) {
    this(distance, speed, heading, false);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    double[] encoderReadings = Robot.sensors.getDriveEncoders();
    double averageEncoder = (encoderReadings[0] + encoderReadings[1]) / 2.0;
    encoderTarget = averageEncoder + distance * Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double[] encoderReadings = Robot.sensors.getDriveEncoders();
    double averageEncoder = (encoderReadings[0] + encoderReadings[1]) / 2.0;
    double ramp = 0.01 * (encoderTarget - averageEncoder);
    if(ramp > 1) ramp = 1;
    ramp = 1;
    double error = heading - Robot.sensors.getHeading();
    double correction = 0.02 * error;
    Robot.driveTrain.setPower((speed - correction) * ramp, (speed + correction) * ramp);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    double[] encoderReadings = Robot.sensors.getDriveEncoders();
    double averageEncoder = (encoderReadings[0] + encoderReadings[1]) / 2.0;
    return averageEncoder > encoderTarget;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    if(!coast) {
      Robot.driveTrain.setPower(0, 0);
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
