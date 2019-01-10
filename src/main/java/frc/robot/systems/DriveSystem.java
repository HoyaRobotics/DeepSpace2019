package frc.robot.systems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.ValueMap;

public class DriveSystem{

    //Variables connecting to the physical robot.
    private Joystick joystick;
    private VictorSP leftMotor;
    private VictorSP rightMotor;

    //Abstraction for the driving system.
    private DifferentialDrive drive;
    private boolean reversedFront;
    private int lastReversedUpdate;

    //Variables controlling the sensitivity of the joystick.
    private double speedSensitivity;
    private double rotationSensitivity;
    private boolean editingSpeed;
    private int lastSensUpdate;

    public DriveSystem(){
        joystick = new Joystick(ValueMap.JOYSTICK_PORT);
        leftMotor = new VictorSP(ValueMap.LEFT_MOTOR_PORT);
        rightMotor = new VictorSP(ValueMap.RIGHT_MOTOR_PORT);

        drive = new DifferentialDrive(leftMotor, rightMotor);
        reversedFront = false;

        speedSensitivity = rotationSensitivity = 1D;
        editingSpeed = true;
    }

    public void update(){
        int joystickPOV = joystick.getPOV();
        if(joystickPOV > -1 && lastSensUpdate++ >= 5){
            lastSensUpdate = 0;
            updateSensitivity(joystickPOV);
        }

        if(joystick.getRawButton(3) && lastReversedUpdate++ >= 5){
            lastReversedUpdate = 0;
            reversedFront = !reversedFront;
        }

        SmartDashboard.putNumber("speedSens", speedSensitivity);
        SmartDashboard.putNumber("rotSens", rotationSensitivity);
        SmartDashboard.putBoolean("reversedFront", reversedFront);

        double speed = joystick.getRawAxis(1) * speedSensitivity;
        if(reversedFront)
            speed *= -1;
        double rotation = joystick.getRawAxis(0) * rotationSensitivity;
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
}