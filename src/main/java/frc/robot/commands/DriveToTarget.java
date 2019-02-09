/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.utilities.Utilities;

public class DriveToTarget extends Command {
  double encoderTarget;
  boolean finished = false;
  boolean dontReadDistance = false;
  double speed;
  public DriveToTarget(double speed) {
    this.speed = speed;
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("initializing");
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double distance = Robot.client.getDistance();
    if (Double.isNaN(distance)) {
      System.out.println("distance = NaN");
      finished = true;
      return;
    }
    double avgEncoder = (driveEncoders[0] + driveEncoders[1])/2.0;
    encoderTarget = avgEncoder + distance*Robot.sensors.ENCODER_TICKS_PER_INCH;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double distance = Robot.client.getDistance();
    if(distance < 30) dontReadDistance = true;
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double avgEncoder = (driveEncoders[0]+driveEncoders[1])/2.0;
    double error = 0;
    if (!Double.isNaN(distance) && !dontReadDistance) {
      
      encoderTarget = avgEncoder + distance*Robot.sensors.ENCODER_TICKS_PER_INCH;
      if(distance < 0) encoderTarget = 10000;
      error = Robot.client.getAngle();

    }
    //System.out.println(avgEncoder + " " + encoderTarget + " " + distance);
    double correction = 0.005 * error;
    if(dontReadDistance) correction = 0;
    correction = Utilities.clip(correction, -0.2 * speed, 0.2 * speed);
    double ramp = 1;
    double remaining = (encoderTarget - avgEncoder)/Robot.sensors.ENCODER_TICKS_PER_INCH;
    if (remaining < 40) ramp = remaining/40;
    double leftPower = speed * ramp - correction;
    double rightPower = speed * ramp + correction;
    Robot.driveTrain.setPower(leftPower, rightPower);
    System.out.println(error + " " + leftPower + " " + rightPower);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    double[] driveEncoders = Robot.sensors.getDriveEncoders();
    double avgEncoder = (driveEncoders[0] + driveEncoders[1])/2.0;
    return finished || avgEncoder >= encoderTarget;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveTrain.setPower(0,0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
