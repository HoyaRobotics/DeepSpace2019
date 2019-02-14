package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class ElevatorSystem extends RobotSystem{

    private static final int TIMEOUT = 30;
    private static final int[] POSITIONS = {0, 17530};
    private static final double BASE_MIN_VELOCITY = 0.2;
    private static final double BASE_VELOCITY_UP = 0.3;

    private TalonSRX elevator;
    private DigitalInput limitSwitch;
    private int position;
    private double speed;
    private boolean hover;

    private int encoderOffset;
    private int targetPositionIndex;
    private int travelStart, travelEnd;
    //private double velocity = 0;
    private double minVelocity;
    private double previousPosition = 0;
    private double desiredPosition = 0;
    private double error = 0;
    private double bias = 0;
    private double priorError = 0;
    private double kp = 0.25;
    private double ki = 0.02;
    private double kd = 0.002;
    private double integral;
    Timer loopTime = new Timer();
    private double derivative;
    private double timePerLoop;
    Joystick joy = new Joystick(1);
    private double distPerRotation = 4096/4.11;
    private double restingHeight = 15;

    public void init(){
        desiredPosition = 0;
        elevator = new TalonSRX(ValueMap.ELEVATOR);
        limitSwitch = new DigitalInput(0);
        
        elevator.configFactoryDefault();
        zeroEncoder();
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TIMEOUT);
        //elevator.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                                                //LimitSwitchNormal.NormallyOpen,
                                                //elevator.getDeviceID(),
                                                //TIMEOUT);
        elevator.set(ControlMode.PercentOutput, 0);
    }

    public void update(){
        if(Input.getPOV() == ValueMap.RAISE_ELEVATOR){
            speed += 0.03;
            hover = false;
        }else if(Input.getPOV() == ValueMap.LOWER_ELEVATOR){
            speed -= 0.01;
            hover = false;
        }else if(Input.getRawButtonPressed(ValueMap.ZERO_ENCODER)){
            speed = 0;
            hover = false;
        }

        //Maintain height by monitoring velocity and adjusting motor speed accordingly.
        if(hover){
            double velocity = elevator.getSensorCollection().getPulseWidthVelocity();
            if(velocity > 0)
                speed -= 0.01;
            else if(velocity < 0)
                speed += 0.01;
        }

        if(Input.getPOV() == ValueMap.STOP_ELEVATOR_1 || Input.getPOV() == ValueMap.STOP_ELEVATOR_2){
            hover = true;
            speed = 0.25; //Start hovering at 25% motor capacity to quicken slow speed.
        }

        elevator.set(ControlMode.PercentOutput, speed);

        /*if(loopTime.get() == 0)
        {
            loopTime.start();
        }
        timePerLoop = loopTime.get();
        loopTime.reset();
        error = (desiredPosition - elevator.getSelectedSensorPosition()/distPerRotation);
        integral = integral + (error * timePerLoop);
        derivative = (error - priorError)/timePerLoop;
        velocity = (kp *error + ki * integral + kd *derivative + bias);

        if(joy.getRawButton(4))
        {
            kp += joy.getRawAxis(2) ;
        }
        if(joy.getRawButton(3))
        {
            ki += joy.getRawAxis(2) ;
        }
        if(joy.getRawButton(5))
        {
            kd += joy.getRawAxis(2);
        }
        
        if(Input.getPOV() == ValueMap.RAISE_ELEVATOR)
            velocity += 0.01;
        else if(Input.getPOV() == ValueMap.LOWER_ELEVATOR)
            velocity -= 0.01;
        else if(Input.getRawButtonPressed(ValueMap.ZERO_ENCODER))
            velocity = 0;

        if(velocity > 1.0)
            velocity = 1.0;
        else if(velocity < -1.0)
            velocity = -1.0;
        if(Input.getRawButton(5)) desiredPosition = 0;
        elevator.set(ControlMode.PercentOutput, velocity);
        if(Input.getRawButtonPressed(7))
        {
            desiredPosition = (56-restingHeight);
            while(elevator.getSelectedSensorPosition()/4096 < desiredPosition)
            {
                elevator.set(ControlMode.PercentOutput, 1);
            }
            elevator.set(ControlMode.PercentOutput, 0);
        }

        previousPosition = elevator.getSelectedSensorPosition()/distPerRotation;
        
        priorError = error;*/
    }

    public void output(){
        SmartDashboard.putNumber("elevatorPercentCode", speed);
        SmartDashboard.putNumber("elevatorPercentActual", elevator.getMotorOutputPercent());
        SmartDashboard.putNumber("elevatorVoltage", elevator.getMotorOutputVoltage());
        SmartDashboard.putNumber("elevatorCurrent", elevator.getOutputCurrent());

        /*
        SmartDashboard.putNumber("error", error);

        SmartDashboard.putNumber("elevatorSpeed", velocity);
        
        SmartDashboard.putNumber("elevator", elevator.getSelectedSensorPosition(0)/distPerRotation);
        SmartDashboard.putNumber("kp", kp);
        SmartDashboard.putNumber("ki", ki);
        SmartDashboard.putNumber("kd", kd);
        SmartDashboard.putNumber("desired height", desiredPosition);
        SmartDashboard.putNumber("timeperloop", timePerLoop);*/
    }

    private void zeroEncoder(){
        elevator.set(ControlMode.PercentOutput, 0);
        elevator.getSensorCollection().setPulseWidthPosition(0, TIMEOUT);
        elevator.setSelectedSensorPosition(0, 0 ,0);
        desiredPosition = 0;
        //velocity = 0;
        encoderOffset = elevator.getSensorCollection().getPulseWidthPosition();
    }

    private boolean almostEqual(int pos1, int pos2){
        return Math.abs(pos2 - pos1) <= 10;
    }

    private void startNewTravel(){
        minVelocity = BASE_MIN_VELOCITY;
        travelStart = position;
        travelEnd = POSITIONS[targetPositionIndex];
    }
}