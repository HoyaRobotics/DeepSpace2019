package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.util.ValueMap;

public class CargoSystem extends RobotSystem{

    private static final double MAX_SPEED = 0.4;

    private VictorSPX leftShooter;
    private TalonSRX rightShooter;
    private Joystick joystick;

    public void init(){
        leftShooter = new VictorSPX(ValueMap.CARGO_LEFT_SHOOTER);
        rightShooter = new TalonSRX(ValueMap.CARGO_RIGHT_SHOOTER);
        joystick = new Joystick(ValueMap.JOYSTICK_PORT);
    }

    public void updateAutonomous(){}

    public void updateTeleop(){
        if(joystick.getRawButton(ValueMap.CARGO_INTAKE_OUT)){
            leftShooter.set(ControlMode.PercentOutput, MAX_SPEED);
            rightShooter.set(ControlMode.PercentOutput, -MAX_SPEED);
        }else{
            leftShooter.set(ControlMode.PercentOutput, 0);
            rightShooter.set(ControlMode.PercentOutput, 0);
        }
    }
}