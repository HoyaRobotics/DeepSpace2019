package frc.robot.systems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

public class HatchSystem extends RobotSystem{

    private Solenoid hatchManipulator, hatchLifter;
    private boolean open, lifted;

    public void init(){
        hatchManipulator = new Solenoid(ValueMap.HATCH_MANIP_PORT);
        hatchLifter = new Solenoid(ValueMap.HATCH_LIFTER_PORT);
    }

    public void update(){
        if(Input.getRawButtonPressed(ValueMap.TOGGLE_HATCH_MANIPULATOR))
            toggleManipulator();
        if(Input.getRawButtonPressed(ValueMap.TOGGLE_HATCH_LIFTER)) 
            toggleLift();
    }

    public void output(){
        SmartDashboard.putBoolean("hatchManipulatorOpen", !hatchManipulator.get());
        SmartDashboard.putBoolean("hatchManipulatorDown", hatchLifter.get());
    }

    private void toggleManipulator(){
        open = !open;
        hatchManipulator.set(open);
    }

    private void toggleLift(){
        lifted = !lifted;
        hatchLifter.set(lifted);
    }
}