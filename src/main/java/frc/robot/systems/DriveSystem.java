package frc.robot.systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.ValueMap;

public class DriveSystem extends RobotSystem {

    //Acceleration increment.
    private static final double ACCEL_INC = 0.05;

    //Variables connecting to the physical robot.
    private Joystick joystick;
    private VictorSP frontLeftMotor;
    private VictorSP frontRightMotor;
    private VictorSP rearLeftMotor;
    private VictorSP rearRightMotor;

    //Abstraction for the driving system.
    private DifferentialDrive drive;
    private SpeedControllerGroup leftMotors;
    private SpeedControllerGroup rightMotors;
    private boolean reversedFront;

    //Variables controlling the sensitivity of the joystick.
    private double speed;
    private double rotation;
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

        leftMotors = new SpeedControllerGroup(frontLeftMotor, rearLeftMotor);
        rightMotors = new SpeedControllerGroup(frontRightMotor, rearRightMotor);
        drive = new DifferentialDrive(leftMotors, rightMotors);
        reversedFront = false;

        speed = rotation = 0D;
        speedSensitivity = rotationSensitivity = 1D;
        editingSpeed = true;
    }

    public void updateAutonomous(){}

    public void updateTeleop(){
        int joystickPOV = joystick.getPOV();
        if(joystickPOV > -1 && lastSensUpdate++ >= 5){
            lastSensUpdate = 0;
            updateSensitivity(joystickPOV);
        }

        if(joystick.getRawButtonPressed(ValueMap.REVERSE_Y_BUTTON))
            reversedFront = !reversedFront;

        smoothAcceleration(joystick.getRawAxis(1) * speedSensitivity);

        if(reversedFront)
            speed *= -1;
        rotation = joystick.getRawAxis(0) * rotationSensitivity;

        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("rotation", rotation);
        SmartDashboard.putNumber("speedSens", speedSensitivity);
        SmartDashboard.putNumber("rotSens", rotationSensitivity);
        SmartDashboard.putBoolean("reversedFront", reversedFront);

        drive(speed, rotation);
    }

    public void drive(double speed, double rotation){
        drive.arcadeDrive(speed, rotation);
    }

    private void updateSensitivity(int joystickPOV){
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

    private void smoothAcceleration(double targetSpeed){
        if(speed == targetSpeed)
            return;

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
    }
}