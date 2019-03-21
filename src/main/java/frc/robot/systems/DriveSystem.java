package frc.robot.systems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;
import frc.robot.util.math.Dampen;

//This class handles the drivetrain of the robot.
public class DriveSystem extends RobotSystem {

    //Driving constants.
    private static final double DEAD_BAND = 0.15;

    //Variables relating to robot control.
    private VictorSP frontLeftMotor;
    private VictorSP frontRightMotor;
    private VictorSP rearLeftMotor;
    private VictorSP rearRightMotor;
    private double speed;
    private double rotation;

    public void init(){
        //Create joystick and motor objects with values obtained from ValueMap class.
        frontLeftMotor = new VictorSP(ValueMap.FRONT_LEFT_MOTOR_PORT);
        frontRightMotor = new VictorSP(ValueMap.FRONT_RIGHT_MOTOR_PORT);
        rearLeftMotor = new VictorSP(ValueMap.REAR_LEFT_MOTOR_PORT);
        rearRightMotor = new VictorSP(ValueMap.REAR_RIGHT_MOTOR_PORT);
    }

    public void disabledPeriodic(){
        frontLeftMotor.set(0);
        frontRightMotor.set(0);
        rearLeftMotor.set(0);
        rearRightMotor.set(0);
    }

    public void enabledPeriodic(){
        //Calculate robot's speed and rotation based on joystick and mods.
        speed = Input.getRawAxis(ValueMap.DRIVE_FRONT_BACK);
        rotation = Input.getRawAxis(ValueMap.DRIVE_LEFT_RIGHT);

        speed = applyDeadband(speed);
        rotation = applyDeadband(rotation);

        rotation = Dampen.lookup(Math.abs(rotation)) * (rotation < 0 ? -1 : 1);
        
        //Feed values into drive method.
        drive(speed, rotation);
    }

    public void alwaysPeriodic(){
        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("rotation", rotation);
    }

    //Drives the robot with a certain speed and rotation.
    public void drive(double speed, double rotation){
        //Calculate values for left and right motors, and limit those values
        //to between -1.0 and 1.0.
        double leftSide = limit((speed * -1) + rotation);
        double rightSide = limit(speed + rotation);

        //Set all motors to appropriate values.
        frontLeftMotor.set(leftSide);
        rearLeftMotor.set(leftSide);
        frontRightMotor.set(rightSide);
        rearRightMotor.set(rightSide);
    }

    //Ignores values that are within the specified deadband range of 0.
    private double applyDeadband(double value){
        if(Math.abs(value) < DEAD_BAND)
            return 0.0;
        else
            return value;
    }

    //Limit values to between -1.0 and 1.0.
    private double limit(double value){
        if(value > 1.0)
            return 1.0;
        else if(value < -1.0)
            return -1.0;
        else
            return value;
    }
}