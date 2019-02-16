/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.*;
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
  XboxTrigger driveToTarget = new XboxTrigger(driver, XboxTrigger.RB);

  XboxTrigger panelHolderStateAdvance = new XboxTrigger(operator, XboxTrigger.B);
  XboxTrigger panelCollect = new XboxTrigger(operator, XboxTrigger.DPADUP);
  XboxTrigger panelPlace = new XboxTrigger(operator, XboxTrigger.DPADDOWN);
  public OI() {
    lowGear.whenActive(new Shift(false));
    highGear.whenActive(new Shift(true));
    driveToTarget.whileActive(new DriveToTarget());

    panelHolderStateAdvance.whenActive(new AdvancePanelHolder());
    panelCollect.whenActive(new ChangePanelState(PanelHolderState.COLLECT));
    panelPlace.whenActive(new ChangePanelState(PanelHolderState.PLACE));
  }

}
