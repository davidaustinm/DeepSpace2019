/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Sensors;

public class PneumaticsCommand extends Command {
  public PneumaticsCommand() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  long lastTime = 0;
  double lastLeftEncoder, lastRightEncoder;
  @Override
  protected void execute() {
    Robot.pneumatics.update();

    long currentTime = System.currentTimeMillis();
    double[] Encoder = Robot.sensors.getDriveEncoders();
      
    if (lastTime != 0) {
    	long elapsedTime = currentTime - lastTime;
    	double changeLeftEncoder = Encoder[0] - lastLeftEncoder;
    	double changeRightEncoder = Encoder[1] - lastRightEncoder;
    	double distance = (changeLeftEncoder + changeRightEncoder)/2.0;
      double velocity = distance / elapsedTime;
      //TODO: Change shift velocities
      if(Robot.pneumatics.getState(Robot.pneumatics.SHIFTER_EXTEND) && (velocity > 1.8)){
          Robot.pneumatics.setState(Robot.pneumatics.SHIFT, true);
      }
      if((!Robot.pneumatics.getState(Robot.pneumatics.SHIFTER_EXTEND)) && (velocity < 0.8)){
        Robot.pneumatics.setState(Robot.pneumatics.SHIFT, false);
      }
    }

    lastTime = currentTime;
    lastLeftEncoder = Encoder[0];
    lastRightEncoder = Encoder[1];
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
