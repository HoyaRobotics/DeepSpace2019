package frc.robot.systems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.util.Input;
import frc.robot.util.ValueMap;

// This system handles the the raising and lowering of the
// hatch/cargo manipulator.
// We use PID control to make the elevator snap to the
// correct position automagically.
public class ElevatorSystem extends RobotSystem{

    // Heights for hatches and cargo, represented as encoder units.
    // Currently not including rope overlap.
    private static final double[] HATCH_HEIGHTS = {0, 34, 62};
    private static final double[] CARGO_HEIGHTS = {0, 17, 35, 49, 77};

    // Minimum time in milliseconds between consecutive accepted target changes (up/down presses).
    private static final long MIN_CHANGE_WAIT_TIME = 400;

    // Elevator percentage needed to maintain height.
    private static final double BASE_HOVER_SPEED = 0.07;

    // Elevator constants.
    private static final double GEAR_RATIO = 56 / 12; 
    private static final double PULSES_PER_ROTATION = GEAR_RATIO;
    private static final double CIRCUMFERENCE = Math.PI * 1.25;
    private static final double DISTANCE_PER_PULSE = CIRCUMFERENCE / PULSES_PER_ROTATION;

    // Variables relating to the physical elevator.
    private CANSparkMax elevator;
    private double elevatorMotorSpeed;
    private double elevatorVelocity;
    private double elevatorPosition;

    // Variables used to control elevator position.
    private int targetIndex;
    private boolean falling;
    private boolean usingCargo;
    private boolean lastChangeUsingCargo;
    private long lastChangeTime;

    private double p;
    private double d;
    private double i = 0;
    private double lastError = 0;

    public void init(){
        elevator = new CANSparkMax(ValueMap.ELEVATOR, MotorType.kBrushless);
        elevator.getEncoder().setPositionConversionFactor(DISTANCE_PER_PULSE);
        elevator.getEncoder().setVelocityConversionFactor(DISTANCE_PER_PULSE);

        falling = usingCargo = lastChangeUsingCargo = true;
    }

    public void disabledPeriodic(){
        usingCargo = lastChangeUsingCargo = true;
        targetIndex = 0;
        elevatorMotorSpeed = 0;
        elevator.set(0);
    }

    public void enabledPeriodic(){
        elevatorPosition = elevator.getEncoder().getPosition();
        elevatorVelocity = elevator.getEncoder().getVelocity();

        if(falling){
            elevatorMotorSpeed = 0; // If we're falling, let gravity bring us down.
            targetIndex = 0; // Reset this so when we reach the bottom we don't immediately go back up.

            // We know we've stopped falling when velocity is zero.
            if(elevatorVelocity == 0){
                falling = false;
                elevator.getEncoder().setPosition(0); // Reset encoder at bottom of elevator.
            }
        }else{
            // Only accept raise/lower button presses if enough time has passed.
            if(System.currentTimeMillis() - lastChangeTime >= MIN_CHANGE_WAIT_TIME){
                if(Input.getPOV() == ValueMap.RAISE_ELEVATOR){
                    usingCargo = !Robot.hatchSystem.isLifted();
                    lastChangeTime = System.currentTimeMillis();

                    if(usingCargo == lastChangeUsingCargo){
                        int maxValue = (usingCargo ? CARGO_HEIGHTS.length : HATCH_HEIGHTS.length) - 1;
                        if(targetIndex < maxValue)
                            targetIndex++;
                    }else{
                        if(usingCargo){
                            // Convert from hatch index to cargo index (going up).
                            if(targetIndex == 0)
                                targetIndex += 1;
                            else
                                targetIndex += 2;
                        }else{
                            // Convert from cargo index to hatch index (going up).
                            if(targetIndex <= 2)
                                targetIndex = 1;
                            else
                                targetIndex = 2;
                        }

                        lastChangeUsingCargo = usingCargo;
                    }
                }else if(Input.getPOV() == ValueMap.LOWER_ELEVATOR){
                    usingCargo = !Robot.hatchSystem.isLifted();
                    lastChangeTime = System.currentTimeMillis();

                    if(usingCargo == lastChangeUsingCargo && targetIndex > 0){
                        targetIndex--;
                    }else{
                        if(usingCargo){
                            // Convert from hatch index to cargo index (going down).
                            if(targetIndex != 0)
                                targetIndex++;
                        }else{
                            // Convert from cargo index to hatch index (going down).
                            if(targetIndex <= 2)
                                targetIndex = 0;
                            else
                                targetIndex -= 2;
                        }

                        lastChangeUsingCargo = usingCargo;
                    }
                }
            }

            moveToPosition(usingCargo ? CARGO_HEIGHTS[targetIndex] : HATCH_HEIGHTS[targetIndex]);

            if(Input.getRawButtonPressed(ValueMap.ZERO_ENCODER) || targetIndex == 0){
                i = 0;
                falling = true;
                elevatorMotorSpeed = 0;
                System.out.println("falling");
            }
        }

        elevator.set(elevatorMotorSpeed);
    }

    public void alwaysPeriodic(){
        SmartDashboard.putNumber("elevatorPosition", elevatorPosition);
        SmartDashboard.putNumber("elevatorVelocity", elevatorVelocity);
        SmartDashboard.putNumber("elevatorMotorSpeed", elevatorMotorSpeed);
        SmartDashboard.putNumber("elevatorTargetIndex", targetIndex);
    }

    private void moveToPosition(double position){
        lastError = p;
        p = position - elevatorPosition;
        i += p;
        d = p - lastError;

        final double cp = 5;
        final double ci = 0.1;
        final double cd = 3;

        elevatorMotorSpeed = (p * cp / 83) - (d * cd / 83) 
            + (i * ci / 100000) + BASE_HOVER_SPEED;

        if(elevatorMotorSpeed > 1.0)
            elevatorMotorSpeed = 1.0;
        else if(elevatorMotorSpeed < -1.0)
            elevatorMotorSpeed = -1.0;
    }
}