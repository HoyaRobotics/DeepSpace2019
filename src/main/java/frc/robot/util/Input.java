package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;

public class Input{

    private static final Input instance = new Input();

    private Joystick joystick;

    private Input(){
        for(int i = 0; i < 6; i++){
            joystick = new Joystick(i);
            if(joystick.getName().equals("Controller (Xbox One For Windows)"))
                break;
        }
    }

    public static boolean getRawButton(int id){
        return instance.joystick.getRawButton(id);
    }

    public static boolean getRawButtonPressed(int id){
        return instance.joystick.getRawButtonPressed(id);
    }

    public static double getRawAxis(int id){
        return instance.joystick.getRawAxis(id);
    }

    public static int getPOV(){
        return instance.joystick.getPOV();
    }

}