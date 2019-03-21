/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.systems.CargoSystem;
import frc.robot.systems.ClimbSystem;
import frc.robot.systems.DriveSystem;
import frc.robot.systems.ElevatorSystem;
import frc.robot.systems.HatchSystem;
import frc.robot.systems.RobotSystem;
import frc.robot.systems.VisionSystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    
    public static DriveSystem driveSystem;
    public static HatchSystem hatchSystem;
    public static CargoSystem cargoSystem;
    public static ElevatorSystem elevatorSystem;
    public static VisionSystem visionSystem;
    public static ClimbSystem climbSystem;

    private long lastPeriodic = System.currentTimeMillis();
    private int timeInDisabled = 0;

    //Runs when the robot is first started up.
    @Override
    public void robotInit(){
        driveSystem = new DriveSystem();
        hatchSystem = new HatchSystem();
        cargoSystem = new CargoSystem();
        elevatorSystem = new ElevatorSystem();
        visionSystem = new VisionSystem();
        climbSystem = new ClimbSystem();
    }

    //Runs periodically, in any mode.
    @Override
    public void robotPeriodic(){
        long startOutput = System.currentTimeMillis();

        for(RobotSystem system : RobotSystem.allSystems())
            system.alwaysPeriodic();

        SmartDashboard.putNumber("timeForOutput", System.currentTimeMillis() - startOutput);

        SmartDashboard.putNumber("timeSinceLastPacket", System.currentTimeMillis() - lastPeriodic);
        lastPeriodic = System.currentTimeMillis();
    }

    //Runs periodically when robot is disabled.
    @Override
    public void disabledPeriodic(){
        timeInDisabled++;
        if(timeInDisabled < 50)
            return;

        for(RobotSystem system : RobotSystem.allSystems())
            system.disabledPeriodic();
    }

    //Runs periodically during autonomous.
    @Override
    public void autonomousPeriodic(){
        timeInDisabled = 0;
        long startLogic = System.currentTimeMillis();

        for(RobotSystem system : RobotSystem.allSystems())
            system.enabledPeriodic();

        SmartDashboard.putNumber("timeForLogic", System.currentTimeMillis() - startLogic);
    }

    //Runs periodically during tele-op.
    @Override
    public void teleopPeriodic(){
        timeInDisabled = 100;
        long startLogic = System.currentTimeMillis();

        for(RobotSystem system : RobotSystem.allSystems())
            system.enabledPeriodic();
        
        SmartDashboard.putNumber("timeForLogic", System.currentTimeMillis() - startLogic);
    }
}