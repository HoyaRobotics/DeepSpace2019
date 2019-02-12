package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;

public class Input{

    private static final Input instance = new Input();

    private Joystick joystick;

    private Input(){
        joystick = new Joystick(ValueMap.JOYSTICK_PORT);
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