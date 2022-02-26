package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
  public Spark shooterMotor1 = new Spark(Constants.shooterMotor1Channel);
  public Spark shooterMotor2 = new Spark(Constants.shooterMotor2Channel);
  
  shooterMotor2.follow(shooterMotor1);
  
  public void shooter() {
    int[] motorSpeeds = {.1, .2, .4, .5, .7, .8, 1};
    
    if (operatorStick.getRawButton(2)) {
      for (i = 0; i < motorSpeeds.length; i++) {
        shooterMotor1.set(i);
        wait(715);
      }
    }
  }
}
