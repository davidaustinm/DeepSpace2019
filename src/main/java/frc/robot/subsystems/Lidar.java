/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.LidarCommand;

/**
 * Add your docs here.
 */
public class Lidar extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  Counter counter;
  double distance;
  public Lidar() {
    counter = new Counter(new DigitalInput(RobotMap.LIDAR));
    counter.setMaxPeriod(1.0);
    counter.setSemiPeriodMode(true);
    counter.reset();
  }

  public double getReading() {
    double cm;
    double reading = 0;
    if (counter.get() < 1) {
      System.out.println("waiting for lidar");
    } else{ 
      cm = (counter.getPeriod() * 100000.0);
      reading= cm/2.54;
    }
    return reading;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public double getDistance() {
    return distance;
    
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new LidarCommand());
  }
}
