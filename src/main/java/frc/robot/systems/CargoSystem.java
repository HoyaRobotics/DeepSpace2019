package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.VictorSP;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

// This is the system that handles everything relating to
// cargo manipulation. This inludes the roller and the
// horizontal wheels.
public class CargoSystem extends RobotSystem{

    // Motor speeds (in percent):
    private static final double ROLLER_OUT_SPEED = 0.7;
    private static final double ROLLER_IN_SPEED = 0.7;
    private static final double INTAKE_SPEED = 0.5;
    private static final double SHOOTER_SPEED = 1.0;
    private static final double SLIDE_IN_SPEED = 0.65;
    private static final double SLIDE_OUT_SPEED = 0.8;

    // All motors used by the system:
    private VictorSP roller;
    private VictorSPX leftShooter;
    private TalonSRX rightShooter;
    private VictorSPX leftSlide, rightSlide;

    public void init(){
        roller = new VictorSP(ValueMap.CARGO_INTAKE_ROLLER);
        leftShooter = new VictorSPX(ValueMap.CARGO_LEFT_SHOOTER);
        rightShooter = new TalonSRX(ValueMap.CARGO_RIGHT_SHOOTER);
        leftSlide = new VictorSPX(ValueMap.CARGO_LEFT_SLIDE);
        rightSlide = new VictorSPX(ValueMap.CARGO_RIGHT_SLIDE);
    }

    public void disabledPeriodic(){
        leftShooter.set(ControlMode.PercentOutput, 0);
        rightShooter.set(ControlMode.PercentOutput, 0);
        roller.set(0);
    }

    public void enabledPeriodic(){
        if(Input.getRawAxis(ValueMap.CARGO_INTAKE_SLIDE) < -0.15){
            // Slide out.
            leftSlide.set(ControlMode.PercentOutput, -SLIDE_OUT_SPEED);
            rightSlide.set(ControlMode.PercentOutput, SLIDE_OUT_SPEED);
        }else if(Input.getRawAxis(ValueMap.CARGO_INTAKE_SLIDE) > 0.15){
            // Slide in.
            leftSlide.set(ControlMode.PercentOutput, SLIDE_IN_SPEED);
            rightSlide.set(ControlMode.PercentOutput, -SLIDE_IN_SPEED);
        }else{
            leftSlide.set(ControlMode.PercentOutput, 0);
            rightSlide.set(ControlMode.PercentOutput, 0);
        }
        
        if(Input.getRawAxis(ValueMap.CARGO_INTAKE_IN) > 0.15){
            // Activate proper motors to intake cargo.
            leftShooter.set(ControlMode.PercentOutput, INTAKE_SPEED);
            rightShooter.set(ControlMode.PercentOutput, -INTAKE_SPEED);
            roller.set(-ROLLER_IN_SPEED);
        }else if(Input.getRawAxis(ValueMap.CARGO_INTAKE_OUT) > 0.15){
            // Activate proper motors to shoot cargo.
            leftShooter.set(ControlMode.PercentOutput, -SHOOTER_SPEED);
            rightShooter.set(ControlMode.PercentOutput, SHOOTER_SPEED);
            roller.set(ROLLER_OUT_SPEED);
        }else{
            leftShooter.set(ControlMode.PercentOutput, 0);
            rightShooter.set(ControlMode.PercentOutput, 0);
            roller.set(0);
        }
    }

    public void alwaysPeriodic(){}
}