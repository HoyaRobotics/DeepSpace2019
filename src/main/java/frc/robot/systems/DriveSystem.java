package frc.robot.systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.ValueMap;

//This class handles the drivetrain of the robot.
public class DriveSystem extends RobotSystem {

    //Driving constants.
    private static final double ACCEL_INC = 0.005;
    private static final double DEAD_BAND = 0.15;

    //Variables relating to robot control.
    private Joystick joystick;
    private VictorSP frontLeftMotor;
    private VictorSP frontRightMotor;
    private VictorSP rearLeftMotor;
    private VictorSP rearRightMotor;
    private boolean reversedFront;

    //Variables controlling the sensitivity of the joystick.
    private double lastSpeed;
    private double speedSensitivity;
    private double rotationSensitivity;
    private double acceleration = 0;
    private boolean editingSpeed;
    private int lastSensUpdate;

    public void init(){
        //Create joystick and motor objects with values obtained from ValueMap class.
        joystick = new Joystick(ValueMap.JOYSTICK_PORT);
        frontLeftMotor = new VictorSP(ValueMap.FRONT_LEFT_MOTOR_PORT);
        frontRightMotor = new VictorSP(ValueMap.FRONT_RIGHT_MOTOR_PORT);
        rearLeftMotor = new VictorSP(ValueMap.REAR_LEFT_MOTOR_PORT);
        rearRightMotor = new VictorSP(ValueMap.REAR_RIGHT_MOTOR_PORT);

        //Set all robot control variables to default value.
        reversedFront = false;
        lastSpeed = 0;
        speedSensitivity = rotationSensitivity = 1D;
        editingSpeed = true;
    }

    public void updateAutonomous(){}

    public void updateTeleop(){
        //Get joystick's POV value and use it to update sensitivity.
        int joystickPOV = joystick.getPOV();
        if(joystickPOV > -1 && lastSensUpdate++ >= 5)
            updateSensitivity(joystickPOV);
        //Invert the robot's y-axis if the correct joystick button has been pressed.
        if(joystick.getRawButtonPressed(ValueMap.REVERSE_Y_BUTTON))
            reversedFront = !reversedFront;

        //Calculate robot's speed and rotation based on joystick.
        double speed = smoothAcceleration(joystick.getRawAxis(1) * speedSensitivity);
        double rotation = joystick.getRawAxis(0) * rotationSensitivity;

        //Print certain values to SmartDashboard for diagnostics.
        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("rotation", rotation);
        SmartDashboard.putNumber("speedSens", speedSensitivity);
        SmartDashboard.putNumber("rotSens", rotationSensitivity);
        SmartDashboard.putBoolean("reversedFront", reversedFront);

        //Feed values into drive method.
        drive(speed, rotation);
    }

    //Drives the robot with a certain speed and rotation.
    public void drive(double speed, double rotation){
        //Ignore values if they are too small. This is in place because
        //the joystick is rarely at an even zero value, but instead slightly
        //positive or negative.
        speed = applyDeadband(speed);
        rotation = applyDeadband(rotation);

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

    //Update the speed and rotational sensitivity of the robot according to
    //the value of the joystick POV.
    private void updateSensitivity(int joystickPOV){
        lastSensUpdate = 0;

        //If the joystick's POV is straight up or down, raise and lower
        //correct value accordingly.
        if(joystickPOV == 0){
            if(editingSpeed)
                speedSensitivity *= 1.1;
            else
                rotationSensitivity *= 1.1;
        }else if(joystickPOV == 180){
            if(editingSpeed)
                speedSensitivity *= 0.9;
            else
                rotationSensitivity *= 0.9;
        }

        //Limit sensitivity values to between 0.1 and 2.
        //These values are arbitrary.
        if(speedSensitivity < 0.1)
            speedSensitivity = 0.1;
        else if(speedSensitivity > 2)
            speedSensitivity = 2;

        if(rotationSensitivity < 0.1)
            rotationSensitivity = 0.1;
        else if(rotationSensitivity > 2)
            rotationSensitivity = 2;

        //If the joystick's POV is left or right, change the value that
        //we're editing accordingly.
        if(joystickPOV == 270)
            editingSpeed = true;
        else if(joystickPOV == 90)
            editingSpeed = false;
    }

    //Smoothly increase speed.
    private double smoothAcceleration(double targetSpeed){
        double speed = lastSpeed;

        if(speed == targetSpeed)
            return lastSpeed;

        if(speed < targetSpeed){
            acceleration += ACCEL_INC;
            speed += acceleration;

            if(speed >= targetSpeed){
                speed = targetSpeed;
                acceleration = 0;
            }
        }else{
            acceleration += ACCEL_INC;
            speed -= acceleration;

            if(speed <= targetSpeed){
                speed = targetSpeed;
                acceleration = 0;
            }
        }

        lastSpeed = speed;
        return speed;
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