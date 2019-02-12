package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class ElevatorSystem extends RobotSystem{

    private static final int TIMEOUT = 30;

    private TalonSRX elevator;
    private int encoderOffset;

    public void init(){
        elevator = new TalonSRX(ValueMap.ELEVATOR);

        elevator.configFactoryDefault();
        zeroEncoder();
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TIMEOUT);
    }

    public void updateAutonomous(){}

    public void updateTeleop(){
        if(Input.getRawButtonPressed(ValueMap.ZERO_ENCODER))
            zeroEncoder();
        
        if(Input.getPOV() == ValueMap.RAISE_ELEVATOR)
            elevator.set(ControlMode.PercentOutput, 0.5);

        int position = elevator.getSensorCollection().getPulseWidthPosition();
        SmartDashboard.putNumber("encoderPosition", (position - encoderOffset));
    }

    private void zeroEncoder(){
        elevator.getSensorCollection().setPulseWidthPosition(0, TIMEOUT);
        encoderOffset = elevator.getSensorCollection().getPulseWidthPosition();
    }
}