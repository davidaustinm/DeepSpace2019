/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  public static boolean DEBUG = true;
  public static int mode;

  // SPARK MAX
  public static final int LEFT_MASTER = 1;
  public static final int LEFT_SLAVE1 = 2;
  public static final int LEFT_SLAVE2 = 3;
  public static final int RIGHT_MASTER = 4;
  public static final int RIGHT_SLAVE1 = 5;
  public static final int RIGHT_SLAVE2 = 6;

  //TALON SRX
  public static final int FRONT_LIFT1 = 7;
  public static final int FRONT_LIFT2 = 9;
  public static final int REAR_LIFT1 = 8;
  public static final int REAR_LIFT2 = 10;
  public static final int ROTATE_MOTOR = 11;

  //VICTOR SPX
  public static final int ROLLER_MOTOR = 12;
  public static final int REAR_LIFT_DRIVE_MOTOR = 13;

  // DIO
  public static final int LIDAR = 0;
  public static final int AUTOSWITCH0 = 10;
  public static final int AUTOSWITCH1 = 12;
  public static final int AUTOSWITCH2 = 11;
  public static final int AUTOSWITCH3 = 13;
  public static final int AUTOSWITCH4 = 18;
  public static final int AUTOSWITCH5 = 19;

  //public static final int VAC_ON = 7;
  // public static final int VAC_RELEASE = 8;
  //public static final int VAC_SENSE = 9;

  // PCM
  public static final int SHIFT = 1;
  public static final int FRONT_LIFT_SHIFT = 0;
  public static final int RF_LATCH = 2;
  //public static final int NOTHING = 3;
  public static final int PUSHER = 5;

  public static final int VAC_POWER = 5;
  public static final int VAC_ON = 6;
  public static final int VAC_OFF = 7;
  public static void setMode(int m) {
    mode = m;
  }
}
