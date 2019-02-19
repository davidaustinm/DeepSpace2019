/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.VacuumCommand;

public class VacuumSubsystem extends Subsystem {
  DigitalOutput vacOn = new DigitalOutput(RobotMap.VAC_ON);
  DigitalOutput vacRelease = new DigitalOutput(RobotMap.VAC_RELEASE);
  DigitalInput vacSense = new DigitalInput(RobotMap.VAC_SENSE);

  

  public void setVacOn(boolean b){
    vacOn.set(b);
  }

  public void setVacRelease(boolean b){
    vacRelease.set(b);
  }

  public boolean getVacSense() {
    return vacSense.get();
  }

  public boolean getVacOn() {
    return vacOn.get();
  }

  public boolean getVacRelease() {
    return vacRelease.get();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new VacuumCommand());
  }
}
