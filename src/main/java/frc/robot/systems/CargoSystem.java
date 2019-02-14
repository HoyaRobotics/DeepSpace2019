package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class CargoSystem extends RobotSystem{

    private static final double MAX_SPEED = 0.5;

    private VictorSPX roller;
    private VictorSPX leftShooter;
    private TalonSRX rightShooter;

    public void init(){
        roller = new VictorSPX(ValueMap.CARGO_INTAKE_ROLLER);
        leftShooter = new VictorSPX(ValueMap.CARGO_LEFT_SHOOTER);
        rightShooter = new TalonSRX(ValueMap.CARGO_RIGHT_SHOOTER);
        roller.follow(rightShooter);
    }

    public void update(){
        if(Input.getRawAxis(ValueMap.CARGO_INTAKE_IN) > 0.15){
            leftShooter.set(ControlMode.PercentOutput, MAX_SPEED);
            rightShooter.set(ControlMode.PercentOutput, -MAX_SPEED);
        }else if(Input.getRawAxis(ValueMap.CARGO_INTAKE_OUT) > 0.15){
            leftShooter.set(ControlMode.PercentOutput, -MAX_SPEED);
            rightShooter.set(ControlMode.PercentOutput, MAX_SPEED);
        }else{
            leftShooter.set(ControlMode.PercentOutput, 0);
            rightShooter.set(ControlMode.PercentOutput, 0);
        }
    }
}