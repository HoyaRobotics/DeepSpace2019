package frc.robot.systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.ValueMap;

public class DriveSystem extends RobotSystem {

    //Driving constants.
    private static final double ACCEL_INC = 0.05;
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
        joystick = new Joystick(ValueMap.JOYSTICK_PORT);
        frontLeftMotor = new VictorSP(ValueMap.FRONT_LEFT_MOTOR_PORT);
        frontRightMotor = new VictorSP(ValueMap.FRONT_RIGHT_MOTOR_PORT);
        rearLeftMotor = new VictorSP(ValueMap.REAR_LEFT_MOTOR_PORT);
        rearRightMotor = new VictorSP(ValueMap.REAR_RIGHT_MOTOR_PORT);
        reversedFront = false;

        lastSpeed = 0;
        speedSensitivity = rotationSensitivity = 1D;
        editingSpeed = true;
    }

    public void updateAutonomous(){}

    public void updateTeleop(){
        int joystickPOV = joystick.getPOV();
        if(joystickPOV > -1 && lastSensUpdate++ >= 5)
            updateSensitivity(joystickPOV);
        if(joystick.getRawButtonPressed(ValueMap.REVERSE_Y_BUTTON))
            reversedFront = !reversedFront;

        double speed = smoothAcceleration(joystick.getRawAxis(1) * speedSensitivity);
        double rotation = joystick.getRawAxis(0) * rotationSensitivity;

        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("rotation", rotation);
        SmartDashboard.putNumber("speedSens", speedSensitivity);
        SmartDashboard.putNumber("rotSens", rotationSensitivity);
        SmartDashboard.putBoolean("reversedFront", reversedFront);

        drive(speed, rotation);
    }

    public void drive(double speed, double rotation){
        speed = applyDeadband(speed);
        rotation = applyDeadband(rotation);

        double leftSide = limit((speed * -1) + rotation);
        double rightSide = limit(speed + rotation);

        if(reversedFront){
            leftSide *= -1;
            rightSide *= -1;
        }

        frontLeftMotor.set(leftSide);
        rearLeftMotor.set(leftSide);
        frontRightMotor.set(rightSide);
        rearRightMotor.set(rightSide);
    }

    private void updateSensitivity(int joystickPOV){
        lastSensUpdate = 0;

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

        if(speedSensitivity < 0.1)
            speedSensitivity = 0.1;
        else if(speedSensitivity > 2)
            speedSensitivity = 2;

        if(rotationSensitivity < 0.1)
            rotationSensitivity = 0.1;
        else if(rotationSensitivity > 2)
            rotationSensitivity = 2;

        if(joystickPOV == 270)
            editingSpeed = true;
        else if(joystickPOV == 90)
            editingSpeed = false;
    }

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

    private double applyDeadband(double value){
        if(Math.abs(value) < DEAD_BAND)
            return 0.0;
        else
            return value;
    }

    private double limit(double value){
        if(value > 1.0)
            return 1.0;
        else if(value < -1.0)
            return -1.0;
        else
            return value;
    }
}