package frc.robot.systems;

import java.util.Collection;
import java.util.HashMap;

public abstract class RobotSystem {

    private static HashMap<String, RobotSystem> systems = new HashMap<>();

    public RobotSystem(){
        init();
    }

    public abstract void init();
    public abstract void updateAutonomous();
    public abstract void updateTeleop();

    public static void registerSystem(String name, RobotSystem system){
        systems.put(name, system);
    }

    public static RobotSystem getSystem(String name){
        return systems.get(name);
    }

    public static Collection<RobotSystem> allSystems(){
        return systems.values();
    }

}