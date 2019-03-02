package frc.robot.systems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class ClimbSystem extends RobotSystem {

    private DoubleSolenoid frontLift;
    private DoubleSolenoid rearLift;

    public void init(){
        frontLift = new DoubleSolenoid(4, ValueMap.FRONT_LIFT_FORWARD, ValueMap.FRONT_LIFT_REVERSE);
        rearLift = new DoubleSolenoid(4, ValueMap.REAR_LIFT_FORWARD, ValueMap.REAR_LIFT_REVERSE);
    }

    public void disabledPeriodic(){
        frontLift.set(Value.kOff);
        rearLift.set(Value.kOff);
    }

    public void enabledPeriodic(){
        if(Input.getRawButton(ValueMap.FRONT_LIFT_CONTROL)){
            if(Input.getRawButton(ValueMap.DOWNWARDS)){
                frontLift.set(Value.kReverse);
            }else{
                frontLift.set(Value.kForward);
            }
        }
        if(Input.getRawButton(ValueMap.REAR_LIFT_CONTROL)){
            if(Input.getRawButton(ValueMap.DOWNWARDS)){
                rearLift.set(Value.kReverse);
            }else{
                rearLift.set(Value.kForward);
            }
        }
    }
}