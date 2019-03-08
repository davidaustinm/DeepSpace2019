/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class VelocityRecord extends Subsystem {

  double velocity = 0;
  double lastEncoderAvg = 0;
  long lastTime = 0;

  public void updateVelocity() {
      double[] encoders = Robot.sensors.getDriveEncoders();
      double avgEncoder = (encoders[0] + encoders[1])/2.0;
      double change = (avgEncoder - lastEncoderAvg)/Robot.sensors.ENCODER_COUNTS_PER_INCH_LOW_GEAR;
      long currentTime = System.currentTimeMillis();
      velocity = change/(currentTime - lastTime) * 1000;
      lastEncoderAvg = avgEncoder;
      lastTime = currentTime;
      System.out.println("velocity record = " + velocity);
  }

  public double getVelocity() {
    return velocity;
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
