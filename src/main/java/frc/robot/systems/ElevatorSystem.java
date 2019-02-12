package frc.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.ValueMap;

public class ElevatorSystem extends RobotSystem{

    private static final int TIMEOUT = 30;

    private TalonSRX elevator;
    private Joystick joystick;
    private int encoderOffset;

    public void init(){
        elevator = new TalonSRX(ValueMap.ELEVATOR);
        joystick = new Joystick(ValueMap.JOYSTICK_PORT);

        elevator.configFactoryDefault();
        zeroEncoder();
        elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TIMEOUT);
    }

    public void updateAutonomous(){}

    public void updateTeleop(){
        if(joystick.getRawButtonPressed(ValueMap.ZERO_ENCODER))
            zeroEncoder();
        
        if(joystick.getPOV() == ValueMap.RAISE_ELEVATOR)
            elevator.set(ControlMode.PercentOutput, 0.5);

        int position = elevator.getSensorCollection().getPulseWidthPosition();
        SmartDashboard.putNumber("encoderPosition", (position - encoderOffset));
    }

    private void zeroEncoder(){
        elevator.getSensorCollection().setPulseWidthPosition(0, TIMEOUT);
        encoderOffset = elevator.getSensorCollection().getPulseWidthPosition();
    }
}