package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TunnelSubsystem extends SubsystemBase {
  public Spark ballTunnelMotor = new Spark(Constants.ballTunnelChannel);
  
  public void tunnel() {
    if (operatorStick.getRawButton(3)) {
      ballTunnelMotor.set(-.7);
    } else {
      ballTunnelMotor.set(0);
    }
  }
}
