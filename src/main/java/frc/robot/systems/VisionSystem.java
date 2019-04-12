package frc.robot.systems;

import edu.wpi.first.cameraserver.CameraServer;

// This system connects the camera to the driver station.
public class VisionSystem extends RobotSystem {

    public void init(){
        CameraServer.getInstance().startAutomaticCapture();
    }

    public void enabledPeriodic(){}
    public void disabledPeriodic(){}
    public void alwaysPeriodic(){}

}