/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.DeepSpaceDriveTrain;
import frc.robot.subsystems.ExampleSubsystem;
//import frc.robot.subsystems.PowerUpDriveTrain;
import frc.robot.subsystems.Sensors;
import frc.robot.subsystems.Shifter;
import frc.robot.subsystems.SparkDriveTrain;
import frc.robot.utilities.TargetCamera;
import frc.robot.utilities.TCPClient;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static OI m_oi;
  public static DeepSpaceDriveTrain driveTrain = new DeepSpaceDriveTrain();
  public static Sensors sensors = new Sensors();
  public static TargetCamera camera;
  public static TCPClient client = null;
  public static Shifter shifter = new Shifter();

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    //client = new TCPClient();
    //client.start();

    m_oi = new OI();

    m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
    // chooser.addOption("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);

    //camera = new TargetCamera();
    //camera.start();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();
    //m_autonomousCommand = new LeftRocketFront();
    //m_autonomousCommand = new DriveToTarget(0.4);
    CommandGroup auto = new CommandGroup();
    auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/left-rocket-45.profile.csv"));
    auto.addSequential(new SwitchDirection());
    auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/turn-left-rocket-front.profile.csv"));
    auto.addSequential(new SwitchDirection());
    auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/drive-to-portal.profile.csv"));
    auto.addSequential(new SwitchDirection());
    auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/backaway-from-portal.profile.csv"));
    //auto.addSequential(new ExecuteDriveProfile("/home/lvuser/profiles/.profile.csv"));
    //auto.addSequential(new Wait(500));
    //auto.addSequential(new DriveToTarget(0.4));
    m_autonomousCommand = auto;
    sensors.resetGyro();
    sensors.resetDriveEncoders();
    sensors.resetPosition();
    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    sensors.updatePosition();
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    shifter.setState(false);
    sensors.resetPosition();
    sensors.resetDriveEncoders();
    sensors.resetGyro();
  }

  /**
   * This function is called periodically during operator control.
   */
  long lastTime = 0;
  double lastAverage = 0;
  @Override
  public void teleopPeriodic() {
    long time = System.currentTimeMillis();
    long elapsed = time - lastTime;
    double[] encoders = sensors.getDriveEncoders();
    double average = (encoders[0] + encoders[1])/2.0;
    double distance = (average - lastAverage)/sensors.ENCODERCOUNTSPERINCH;
    double speed = distance/elapsed * 1000;
    //System.out.println("speed = " + speed);
    lastAverage = average;
    lastTime = time;
    Scheduler.getInstance().run();
    sensors.updatePosition();
    double[] position = sensors.getPosition();
    //.out.println(position[0] + " " + position[1]);
    //double[] targetInfo = client.getTargetInfo();
    //System.out.println(targetInfo[0]-160 + " " + targetInfo[1] + " " + targetInfo[2]);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
