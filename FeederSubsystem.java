package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class FeederSubsystem {
  public Spark feederMotor = new Spark (Constants.feederChannel);
  
  public void feeder() {
    if (operatorStick.getRawButton(5)) {
      feederMotor.set(0.5);
    } else {
    if (operatorStick.getRawButton(4)) {
      feederMotor.set(-.5);
    } else {
      feederMotor.set(0);
    }
  }
}
