package frc.robot.systems;

import java.util.ArrayList;

// This class describes a system for use on the robot.
public abstract class RobotSystem {

    // Global list of all robot systems.
    private static ArrayList<RobotSystem> systems = new ArrayList<>();

    public RobotSystem(){
        systems.add(this);
        init();
    }

    // Should replace constructor in child classes.
    // Put all initialization code here.
    public abstract void init();

    // Called every robot packet while enabled.
    // Should be used to reset robot values.
    public abstract void disabledPeriodic();

    // Called every robot packet while enabled.
    // Should be used to update robot values.
    public abstract void enabledPeriodic();

    // Called every robot packet.
    // Should be used for value output to the SmartDashboard or another output.
    public abstract void alwaysPeriodic();

    // Get all systems.
    public static ArrayList<RobotSystem> allSystems(){
        return systems;
    }

}