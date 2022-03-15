// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "Shoot Only";
  private static final String kNoAuto = "Do Nothing";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private DifferentialDrive driveBase;
  private Joystick driverStick1;
  private Joystick driverStick2;
  private Joystick operatorStick;
  private static final int leftLeaderDeviceID = 10;
  private static final int leftFollowerDeviceID = 11;
  private static final int rightLeaderDeviceID = 12;
  private static final int rightFollowerDeviceID = 13;
  private CANSparkMax leftLeader, leftFollower, rightLeader, rightFollower;
  private Spark winchMotor;
  private Spark tunnelMotor;
  private Spark feederMotor;
  private Spark feederBarMotor;
  private CANSparkMax highShooterMotor;
  private CANSparkMax lowShooterMotor;
  private static final int highShooterMotorDeviceID = 20;
  private static final int lowShooterMotorDeviceID = 21;
  private Timer timer;
  private double shooterSpeed;
  private static final int climberMotorDeviceID = 22;
  private CANSparkMax climberMotor;
  private double climberSpeed;



  /**
   *
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    CameraServer.startAutomaticCapture();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Shoot Only", kCustomAuto);
    m_chooser.addOption("Do Nothing", kNoAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    leftLeader = new CANSparkMax(leftLeaderDeviceID, MotorType.kBrushless);
    leftFollower = new CANSparkMax(leftFollowerDeviceID, MotorType.kBrushless);
    rightLeader = new CANSparkMax(rightLeaderDeviceID, MotorType.kBrushless);
    rightFollower = new CANSparkMax(rightFollowerDeviceID, MotorType.kBrushless);

    leftFollower.follow(leftLeader);
    rightFollower.follow(rightLeader);

    highShooterMotor = new CANSparkMax(highShooterMotorDeviceID, MotorType.kBrushless);
    lowShooterMotor = new CANSparkMax(lowShooterMotorDeviceID, MotorType.kBrushless);

    climberMotor = new CANSparkMax(climberMotorDeviceID, MotorType.kBrushless);

    winchMotor = new Spark(0);
    tunnelMotor = new Spark(1);
    feederMotor = new Spark(2);
    feederBarMotor = new Spark(3);

    driveBase = new DifferentialDrive(leftLeader, rightLeader);

    driverStick1 = new Joystick(0);
    driverStick2 = new Joystick(1);
    operatorStick = new Joystick (2);

    timer = new Timer();

    shooterSpeed = 0.9;
    climberSpeed = operatorStick.getRawAxis(1);



  /* if(leftLeader.setOpenLoopRampRate(.2) !=REVLibError.kOk) {
      SmartDashboard.putString("Ramp Rate", "Error");
    }

    if(leftFollower.setOpenLoopRampRate(.2) !=REVLibError.kOk) {
      SmartDashboard.putString("Ramp Rate", "Error");
    }

    if(rightLeader.setOpenLoopRampRate(.2) !=REVLibError.kOk) {
      SmartDashboard.putString("Ramp Rate", "Error");
    }

    if(rightFollower.setOpenLoopRampRate(.2) !=REVLibError.kOk) {
      SmartDashboard.putString("Ramp Rate", "Error");
    }
*/
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    timer.reset();
    timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        if (timer.get() < 3) {
          highShooterMotor.set(shooterSpeed);
          lowShooterMotor.set(-shooterSpeed);
          tunnelMotor.set(-.25);
        } else {
          highShooterMotor.set(0);
          lowShooterMotor.set(0);
          tunnelMotor.set(0);
        }
        break;
      case kNoAuto:
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        if (timer.get() < 3) {
          highShooterMotor.set(shooterSpeed);
          lowShooterMotor.set(-shooterSpeed);
          tunnelMotor.set(-.25);
        } else if (timer.get() < 6) {
          highShooterMotor.set(0);
          lowShooterMotor.set(0);
          tunnelMotor.set(0);
          driveBase.tankDrive(0.5,-0.5);
        } else {
          driveBase.stopMotor();
        }
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    driveBase.tankDrive(LeftThrottle(), RightThrottle());
    setShooterSpeed();
    winch();
    tunnel();
    feeder();
    shooter();
    climber();

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  public void climber() {
    if (operatorStick.getRawAxis(1) > .5) {
      climberMotor.set(climberSpeed);
    }
    else if (operatorStick.getRawAxis(1) > -.5) {
      climberMotor.set(climberSpeed);
    }
    else {
      climberMotor.set(0);
    }
  }

  public void shooter() {
    if (operatorStick.getRawButton(1)) {
      highShooterMotor.set(shooterSpeed);
      lowShooterMotor.set(-shooterSpeed);
    }
    else {
      highShooterMotor.set(0);
      lowShooterMotor.set(0);
    }
  }

  public void feeder() {
    if (operatorStick.getRawButton(5)) {
      feederMotor.set(1);
      feederBarMotor.set(1);
    }
    else if (operatorStick.getRawButton(4)) {
      feederMotor.set(-1);
      feederBarMotor.set(-1);
    }
    else {
      feederMotor.set(0);
    }
  }

  public void tunnel() {
    if (operatorStick.getRawButton(3)) {
      tunnelMotor.set(-.25);
    }
    else if (operatorStick.getRawButton(2)) {
      tunnelMotor.set(.25);
    }
    else {
      tunnelMotor.set(0);
    }
  }

  public void winch() {
    if (operatorStick.getRawButton(11)) {
      winchMotor.set(1);
    } else if (operatorStick.getRawButton(10)) {
      winchMotor.set(-1);
    }
    else {
      winchMotor.set(0);
    }
  }

  public void setShooterSpeed() {
    shooterSpeed = .75 + operatorStick.getRawAxis(2)*0.25;
  }

  public double LeftThrottle() {
    double throttleInput = -driverStick1.getRawAxis(1);
    return (Math.pow(throttleInput,5))/3  + (Math.pow(throttleInput,3))/3 +throttleInput/3;
  }

  public double RightThrottle() {
    double throttleInput = driverStick2.getRawAxis(1);
    return (Math.pow(throttleInput,5))/3  + (Math.pow(throttleInput,3))/3 +throttleInput/3;
  }

}
