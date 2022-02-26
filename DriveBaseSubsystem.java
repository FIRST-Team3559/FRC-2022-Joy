package java.frc.robot;

import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DifferentialDriveOdometry;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class DriveBaseSubsystem extends SubsystemBase {
   // Motor controllers Left
  public CANSparkMax mc_leftFront = new CANSparkMax(Constants.FL_MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  public CANSparkMax mc_leftRear = new CANSparkMax(Constants.RL_MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  public MotorControllerGroup mcg_left = new MotorControllerGroup(mc_leftFront, mc_leftRear);
  // Motor controllers Right
  public CANSparkMax mc_rightFront = new CANSparkMax(Constants.FR_MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  public CANSparkMax mc_rightRear = new CANSparkMax(Constants.RR_MOTOR_CONTROLLER_ID, MotorType.kBrushless);
  public MotorControllerGroup mcg_right = new MotorControllerGroup(mc_rightFront, mc_rightRear);
  
  public DifferentialDrive drivetrain = new DifferentialDrive(mcg_left, mcg_right);
  private final SparkMaxRelativeEncoder m_leftEncoder = new SparkMaxRelativeEncoder;
  private final SparkMaxRelativeEncoder m_rightEncoder = new SparkMaxRelativeEncoder;
  public final ADXRS450_Gyro gyro;
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(Constants.kTrackwidthMeters);
  public var gyroAngle;
  public DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(gyroAngle, 0);
  public Pose2d m_pose;
  
  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    // Sets the distance per pulse for the encoders
    m_leftEncoder.setDistancePerPulse(DriveConstants.kEncoderDistancePerPulse);
    m_rightEncoder.setDistancePerPulse(DriveConstants.kEncoderDistancePerPulse);
    
    resetEncoders();
  }
   
  @Override
public void periodic() {
  // Get my gyro angle. We are negating the value because gyros return positive
  // values as the robot turns clockwise. This is not standard convention that is
  // used by the WPILib classes.
  gyroAngle = Rotation2d.fromDegrees(-m_gyro.getAngle());

  // Update the pose
  m_pose = m_odometry.update(gyroAngle, m_leftEncoder.getDistance(), m_rightEncoder.getDistance());
}
  
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getRate(), m_rightEncoder.getRate());
  }

  // Resets the drive encoders to currently read a position of 0.
  public void resetEncoders() {
    m_leftEncoder.reset();
    m_rightEncoder.reset();
  }

  // Gets the average distance of the two encoders.
  public double getAverageEncoderDistance() {
    return (m_leftEncoder.getDistance() + m_rightEncoder.getDistance()) / 2.0;
  }

  public Encoder getLeftEncoder() {
    return m_leftEncoder;
  }

  public Encoder getRightEncoder() {
    return m_rightEncoder;
  }
  
  public void manualDrive(double getLeftStick, double getRightStick) {
    drivetrain.tankDrive(getLeftStick, getRightStick);
  }
}
