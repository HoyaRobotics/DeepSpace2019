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
    private boolean reversedFront;
    private boolean dampenRotation;
    private double speed;
    private double rotation;

    public void init(){
        //Create joystick and motor objects with values obtained from ValueMap class.
        frontLeftMotor = new VictorSP(ValueMap.FRONT_LEFT_MOTOR_PORT);
        frontRightMotor = new VictorSP(ValueMap.FRONT_RIGHT_MOTOR_PORT);
        rearLeftMotor = new VictorSP(ValueMap.REAR_LEFT_MOTOR_PORT);
        rearRightMotor = new VictorSP(ValueMap.REAR_RIGHT_MOTOR_PORT);

        //Set all robot control variables to default value.
        reversedFront = false;
        dampenRotation = true;
    }

    public void update(){
        /*un-comment when elevator sensors are installed:
        double speedMod = 1.0 - (currentElevatorHeight / maxElevatorHeight);
        if(speedMod < someMinSpeed)
            speed *= someMinSpeed;
        else
            speed *= speedMod;

        maybe add a deadband for the bottom of the elevator
        (if it's below some point just set mod to 1)
        just so that there isn't any weirdness when the elevator
        is near but not quite the bottom

        also maybe feed mod into another exponential function
        instead of just multiplying it directly
        (for example if the damping is only really needed after the
        elevator is like halfway up or something)
        ex: speed *= PoewrLookup.lookup(speedMod);
        */

        if(Input.getRawButtonPressed(ValueMap.TOGGLE_ROTATION_DAMPENING))
            dampenRotation = !dampenRotation;
        if(Input.getRawButtonPressed(ValueMap.REVERSE_Y))
            reversedFront = !reversedFront;

        //Calculate robot's speed and rotation based on joystick and mods.
        speed = Input.getRawAxis(ValueMap.DRIVE_FRONT_BACK);
        rotation = Input.getRawAxis(ValueMap.DRIVE_LEFT_RIGHT);

        speed = applyDeadband(speed);
        rotation = applyDeadband(rotation);

        if(dampenRotation)
            rotation = Dampen.lookup(Math.abs(rotation)) * (rotation < 0 ? -1 : 1);
        
        //Feed values into drive method.
        drive(speed, rotation);
    }

    public void output(){
        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("rotation", rotation);
        SmartDashboard.putBoolean("reversedFront", reversedFront);
        SmartDashboard.putBoolean("dampenRotation", dampenRotation);
    }

    //Drives the robot with a certain speed and rotation.
    public void drive(double speed, double rotation){
        //Calculate values for left and right motors, and limit those values
        //to between -1.0 and 1.0.
        double leftSide = limit((speed * -1) + rotation);
        double rightSide = limit(speed + rotation);

        //Reverse each side if necessary by multiplying by -1.
        if(reversedFront){
            leftSide *= -1;
            rightSide *= -1;
        }

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