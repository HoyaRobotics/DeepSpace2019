package frc.robot.util;

//This class maps all previously hard-coded values in terms of ports, buttons,
//etc into variable names.
public class ValueMap{

    //Joystick port.
    public static final int JOYSTICK_PORT = 0;

    //Joystick buttons.
    public static final int ZERO_ENCODER = 1;              //A
    public static final int TOGGLE_HATCH_MANIPULATOR = 2;  //B
    public static final int TOGGLE_ROTATION_DAMPENING = 3; //X
    public static final int REVERSE_Y = 4;                 //Y
    public static final int TOGGLE_HATCH_LIFTER = 6;       //Right Bumper

    //Joystick axis.
    public static final int DRIVE_LEFT_RIGHT = 0;          //Left Joystick X
    public static final int DRIVE_FRONT_BACK = 1;          //Left Joystick Y
    public static final int CARGO_INTAKE_OUT = 2;          //Left Trigger
    public static final int CARGO_INTAKE_IN = 3;           //Right Trigger

    //Joystick POV.
    public static final int RAISE_ELEVATOR = 0;            //Up
    public static final int LOWER_ELEVATOR = 180;          //Down
    public static final int STOP_ELEVATOR_1 = 270;         //Left
    public static final int STOP_ELEVATOR_2 = 90;          //Right

    //PWM ports.
    public static final int FRONT_LEFT_MOTOR_PORT = 3;
    public static final int FRONT_RIGHT_MOTOR_PORT = 4;
    public static final int REAR_LEFT_MOTOR_PORT = 2;
    public static final int REAR_RIGHT_MOTOR_PORT = 1;
    public static final int CARGO_INTAKE_RIGHT = 0;

    //CAN bus ids.
    public static final int CARGO_INTAKE_ROLLER = 13;
    public static final int CARGO_LEFT_SHOOTER = 17;
    public static final int CARGO_RIGHT_SHOOTER = 16;
    public static final int ELEVATOR = 15;

    //Pneumatics.
    public static final int COMPRESSOR_PORT = 0;
    public static final int HATCH_MANIP_PORT = 4;
    public static final int HATCH_LIFTER_PORT = 5;
}