/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.*;
import frc.robot.subsystems.FrontLiftMotors;
import frc.robot.utilities.XboxTrigger;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  public XboxController driver = new XboxController(0);
  public XboxController operator = new XboxController(1);
  XboxTrigger lowGear = new XboxTrigger(driver, XboxTrigger.A);
  XboxTrigger highGear = new XboxTrigger(driver, XboxTrigger.Y);
  //XboxTrigger lowFront = new XboxTrigger(driver, XboxTrigger.B);
  // XboxTrigger highFront = new XboxTrigger(driver, XboxTrigger.X);
  XboxTrigger driveToTarget = new XboxTrigger(driver, XboxTrigger.RB);
  XboxTrigger endgame = new XboxTrigger(driver, XboxTrigger.ENDGAME);
  XboxTrigger teleop = new XboxTrigger(driver, XboxTrigger.TELEOP);
  XboxTrigger raiseLift = new XboxTrigger(driver, XboxTrigger.RT);
  XboxTrigger driveToTargetOff = new XboxTrigger(driver, XboxTrigger.LB);
    
  //XboxTrigger panelHolderStateAdvance = new XboxTrigger(operator, XboxTrigger.B);
  XboxTrigger panelCollect = new XboxTrigger(operator, XboxTrigger.DPADUP);
  XboxTrigger panelPlace = new XboxTrigger(operator, XboxTrigger.DPADDOWN);
  XboxTrigger rotateOut = new XboxTrigger(operator, XboxTrigger.LB);
  XboxTrigger rotateIn = new XboxTrigger(operator, XboxTrigger.RB);
  XboxTrigger modeCargo = new XboxTrigger(operator, XboxTrigger.DPADLEFT);
  XboxTrigger modePanel = new XboxTrigger(operator, XboxTrigger.DPADRIGHT);
  XboxTrigger level1 = new XboxTrigger(operator, XboxTrigger.A);
  XboxTrigger level2 = new XboxTrigger(operator, XboxTrigger.X);
  XboxTrigger level3 = new XboxTrigger(operator, XboxTrigger.Y);
 // XboxTrigger latch = new XboxTrigger(operator, XboxTrigger.LT);

   public OI() {
    
    lowGear.whenActive(new Shift(true));
    highGear.whenActive(new Shift(false));
    //lowFront.whenActive(new FrontShift(true));
    //highFront.whenActive(new FrontShift(false));
    driveToTarget.whenActive(new NewDriveToTarget());
    driveToTargetOff.whenActive(new DriveToTargetOff());;
  
    endgame.whenActive(new ChangeGameState(GameState.ENDGAME));
    teleop.whenActive(new ChangeGameState(GameState.TELEOP));
    raiseLift.toggleWhenActive(new RaiseRearLift());

    //panelHolderStateAdvance.whenActive(new AdvancePanelHolder());
    panelCollect.whenActive(new ExtendPusher(false));
    panelPlace.whenActive(new ExtendPusher(true));
    rotateOut.whenActive(new ChangeIntakeRotateState(IntakeRotateCommand.OUT));
    rotateIn.whenActive(new ChangeIntakeRotateState(IntakeRotateCommand.IN));
    modeCargo.whenActive(new SetFrontLiftMode(FrontLiftMotors.CARGO_MODE));
    modePanel.whenActive(new SetFrontLiftMode(FrontLiftMotors.PANEL_MODE));
    level1.whenActive(new SetFrontLiftLevel(RobotMap.mode, FrontLiftMotors.LEVEL_1));
    level2.whenActive(new SetFrontLiftLevel(RobotMap.mode, FrontLiftMotors.LEVEL_2));
    level3.whenActive(new SetFrontLiftLevel(RobotMap.mode, FrontLiftMotors.LEVEL_3));

  }

}
