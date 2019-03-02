package frc.robot.systems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class ElevatorSystem extends RobotSystem{

    private static final double BASE_HOVER_SPEED = 0.01;
    private static final double NUDGE_HOVER_SPEED = 0.001;
    private static final double NUDGE_RAISE_SPEED = 0.03;
    private static final double NUDGE_LOWER_SPEED = 0.03;

    private CANSparkMax elevator;
    private double speed;
    private double velocity;
    private boolean hover;

    public void init(){
        elevator = new CANSparkMax(ValueMap.ELEVATOR, MotorType.kBrushless);
        
        zeroEncoder();
    }

    public void disabledPeriodic(){
        speed = 0;
        velocity = 0;
        hover = false;
        elevator.set(0);
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
        velocity = elevator.getEncoder().getVelocity();
       SmartDashboard.putNumber("elevatorVelocity", velocity);
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
        
        elevator.set(speed);
    }

    public void alwaysPeriodic(){
        SmartDashboard.putNumber("elevatorVelocity", velocity);
        SmartDashboard.putNumber("elevator", elevator.getEncoder().getPosition());
    }

    private void zeroEncoder(){
        elevator.set(0);
        elevator.getEncoder().setPosition(0);
    }
}