package frc.robot.systems;

public abstract class RobotSystem {

    public RobotSystem(){
        init();
    }

    public abstract void init();
    public abstract void update();

}