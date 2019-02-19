package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class ElevatorSystem extends RobotSystem{

    private static final int TIMEOUT = 30;
    private static final double BASE_HOVER_SPEED = 0.25;
    private static final double NUDGE_HOVER_SPEED = 0.01;
    private static final double NUDGE_RAISE_SPEED = 0.03;
    private static final double NUDGE_LOWER_SPEED = 0.03;

    private TalonSRX elevator;
    private double speed;
    private double velocity;
    private boolean hover;

    public void init(){
        elevator = new TalonSRX(ValueMap.ELEVATOR);
        
        elevator.configFactoryDefault();
        zeroEncoder();
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TIMEOUT);
        elevator.set(ControlMode.PercentOutput, 0);
    }

    public void disabledPeriodic(){
        speed = 0;
        velocity = 0;
        hover = false;
        elevator.set(ControlMode.PercentOutput, 0);
    }

    public void enabledPeriodic(){
        if(Input.getPOV() == ValueMap.RAISE_ELEVATOR){
            speed += NUDGE_RAISE_SPEED;
            hover = false;
        }else if(Input.getPOV() == ValueMap.LOWER_ELEVATOR){
            speed -= NUDGE_LOWER_SPEED;
            hover = false;
        }else if(Input.getRawButtonPressed(ValueMap.ZERO_ENCODER)){
            speed = 0;
            hover = false;
        }

        velocity = elevator.getSensorCollection().getPulseWidthVelocity();
        if(hover){
            //Maintain height by monitoring velocity and adjusting motor speed accordingly.
            //(if we want to hover)
            if(velocity > 0)
                speed -= NUDGE_HOVER_SPEED;
            else if(velocity < 0)
                speed += NUDGE_HOVER_SPEED;
        }

        if(Input.getPOV() == ValueMap.STOP_ELEVATOR_1 || Input.getPOV() == ValueMap.STOP_ELEVATOR_2){
            hover = true;
            speed = BASE_HOVER_SPEED; //Start hovering at 25% motor capacity to quicken slow speed.
        }

        if(speed > 1.0)
            speed = 1.0;
        else if(speed < -1.0)
            speed = -1.0;

        elevator.set(ControlMode.PercentOutput, speed);
    }

    public void alwaysPeriodic(){
        SmartDashboard.putNumber("elevatorPercentCode", speed);
        SmartDashboard.putNumber("elevatorPercentActual", elevator.getMotorOutputPercent());
        SmartDashboard.putNumber("elevatorVoltage", elevator.getMotorOutputVoltage());
        SmartDashboard.putNumber("elevatorCurrent", elevator.getOutputCurrent());
        SmartDashboard.putNumber("elevatorVelocity", velocity);
    }

    private void zeroEncoder(){
        elevator.set(ControlMode.PercentOutput, 0);
        elevator.getSensorCollection().setPulseWidthPosition(0, TIMEOUT);
        elevator.setSelectedSensorPosition(0, 0 ,0);
    }
}