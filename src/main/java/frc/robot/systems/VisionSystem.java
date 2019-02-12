package frc.robot.systems;

import edu.wpi.first.cameraserver.CameraServer;

public class VisionSystem extends RobotSystem {

    public void init(){
        CameraServer.getInstance().startAutomaticCapture(0);
    }

    public void update(){}

}