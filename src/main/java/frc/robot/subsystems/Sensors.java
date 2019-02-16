/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import frc.robot.Robot;
import frc.robot.RobotMap;
  
public class Sensors extends Subsystem {
  AHRS navx;
  double gyroOffset = 0;
  double cutPoint = 180;
  double[] driveEncoderOffsets = new double[] {0,0};
  Encoder intakeRotateEncoder = new Encoder(RobotMap.INTAKE_ROTATE_ENCODER_A, RobotMap.INTAKE_ROTATE_ENCODER_B);
  Accelerometer accelerometer = new BuiltInAccelerometer(Accelerometer.Range.k4G);
  //public final double ENCODERCOUNTSPERINCH = 0.8; // wooden robot
  public final double ENCODER_COUNTS_PER_INCH_HIGH_GEAR = 0.44444; // new Drive train no extras
  public final double ENCODER_COUNTS_PER_INCH_LOW_GEAR = .63;
  public static final double ENCODER_TICKS_PER_INCH = 23.8; // powerup robot
  double positionX = 0;
  double positionY = 0;
  double[] lastDriveEncoder = new double[] {0,0};
        
  public Sensors() {
    navx = new AHRS(I2C.Port.kMXP);
  }

  public double getPitch() {
    return accelerometer.getY();
  }

  public int getIntakeRotatePosition() {
    return intakeRotateEncoder.get();
  }

  public double readGyro() {
    return -navx.getAngle();
  }

  public void resetGyro() {
    gyroOffset = readGyro();
  }

  public double getHeading() {
    double heading = readGyro() - gyroOffset;
    if (Robot.driveTrain.isSwitched()) heading += 180;
    while (heading > cutPoint) heading -= 360;
    while (heading < cutPoint - 360) heading += 360;
    return heading;
  }

  public void setCutpoint(double cp) {
    cutPoint = cp;
  }

  public double[] readDriveEncoders() {
    return Robot.driveTrain.getDriveEncoders();
  }

  public void resetDriveEncoders() {
    driveEncoderOffsets = readDriveEncoders();
    lastDriveEncoder = new double[] {0,0};
  }

  public double[] getDriveEncoders() {
    double[] encoderReadings = readDriveEncoders();
    return new double[] {encoderReadings[0] - driveEncoderOffsets[0],
                      encoderReadings[1]-driveEncoderOffsets[1]};
  }

  public double getLeftDriveEncoder() {
    double[] encoderReadings = readDriveEncoders();
    return encoderReadings[0];
  }
  public double getRightDriveEncoder() {
    double[] encoderReadings = readDriveEncoders();
    return encoderReadings[1];
  }
  public void resetPosition() {
    positionX = 0;
    positionY = 0;
  }
  public void updatePosition() {
    double[] driveEncoders = getDriveEncoders();
    double changeInLeft = driveEncoders[0] - lastDriveEncoder[0];
    double changeInRight = driveEncoders[1] - lastDriveEncoder[1];
    double distance = (changeInLeft + changeInRight) / 2;
    double heading = Math.toRadians(Robot.sensors.getHeading());
    positionX += distance * Math.cos(heading);
    positionY += distance * Math.sin(heading);
    lastDriveEncoder = driveEncoders;
  }
  public double[] getPosition() {
    return new double[] {positionX / ENCODER_COUNTS_PER_INCH_LOW_GEAR, positionY / ENCODER_COUNTS_PER_INCH_LOW_GEAR };
  }

  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
