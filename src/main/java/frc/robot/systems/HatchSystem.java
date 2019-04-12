package frc.robot.systems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

// This system handles the hatch manipulator. This includes
// opening and closing the flower and raising and lowering
// arm.
public class HatchSystem extends RobotSystem{

    private Compressor compressor = new Compressor(1);
    private Solenoid hatchManipulator, hatchLifter;
    private boolean open, lifted;

    public void init(){
        hatchManipulator = new Solenoid(1, ValueMap.HATCH_MANIP_PORT);
        hatchLifter = new Solenoid(1, ValueMap.HATCH_LIFTER_PORT);
    }

    public void disabledPeriodic(){
        if(hatchManipulator.get())
            toggleManipulator();
        if(hatchLifter.get())
            toggleLift();
    }

    public void enabledPeriodic(){
        if(Input.getRawButtonPressed(ValueMap.TOGGLE_HATCH_MANIPULATOR))
            toggleManipulator();
        if(Input.getRawButtonPressed(ValueMap.TOGGLE_HATCH_LIFTER)) 
            toggleLift();
    }

    public void alwaysPeriodic(){
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

    public boolean isLifted(){
        return lifted;
    }
}