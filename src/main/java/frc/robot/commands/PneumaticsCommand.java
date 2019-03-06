/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.Pneumatics;
import frc.robot.subsystems.Sensors;

public class PneumaticsCommand extends Command {
  public PneumaticsCommand() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.pneumatics);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  long lastTime = 0;
  int count = 0;
  double lastLeftEncoder, lastRightEncoder;
  @Override
  protected void execute() {
    Robot.pneumatics.update();

    long currentTime = System.currentTimeMillis();
    double[] encoder = Robot.sensors.getDriveEncoders();
    double encoderCountsPerInch = Robot.pneumatics.getState(Pneumatics.SHIFT) ? 
      Robot.sensors.ENCODER_COUNTS_PER_INCH_HIGH_GEAR : Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
      
    /*  
    if (lastTime != 0) {
    	long elapsedTime = currentTime - lastTime;
    	double changeLeftEncoder = encoder[0] - lastLeftEncoder;
    	double changeRightEncoder = encoder[1] - lastRightEncoder;
    	double distance = (changeLeftEncoder + changeRightEncoder)/2.0/encoderCountsPerInch;
      double velocity = distance / elapsedTime * 1000;
      SmartDashboard.putNumber("velocity", velocity);
      //TODO: Change shift velocities
      if(!Robot.pneumatics.getState(Pneumatics.SHIFT) && (Math.abs(velocity) > 120)){
        count += 1;
        if (count > 5) Robot.pneumatics.setState(Pneumatics.SHIFT, true);
      } else count = 0;
      if((Robot.pneumatics.getState(Pneumatics.SHIFT)) && (Math.abs(velocity) < 80)){
        Robot.pneumatics.setState(Pneumatics.SHIFT, false);
      }
    }*/

    lastTime = currentTime;
    lastLeftEncoder = encoder[0];
    lastRightEncoder = encoder[1];
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
