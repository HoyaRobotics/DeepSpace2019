package frc.robot.systems;

import java.util.Collection;
import java.util.HashMap;

public abstract class RobotSystem {

    //Global list of all robot systems.
    private static HashMap<String, RobotSystem> systems = new HashMap<>();

    //Call init() in constructor.
    public RobotSystem(){
        init();
    }

    //Should essentially replace constructor in child classes.
    //Put all initialization code here.
    public abstract void init();

    //Called every robot packet during autonomous and tele-op.
    //Should be used to update robot values.
    public abstract void update();

    //Called every robot packet.
    //Should be used for value output to the SmartDashboard or another output.
    public abstract void output();

    //Adds a system to the global list.
    public static void registerSystem(String name, RobotSystem system){
        systems.put(name, system);
    }

    //Get a system by name from the global list.
    public static RobotSystem getSystem(String name){
        return systems.get(name);
    }

    //Get all systems.
    public static Collection<RobotSystem> allSystems(){
        return systems.values();
    }

}