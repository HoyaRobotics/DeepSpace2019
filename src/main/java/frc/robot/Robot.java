/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.systems.DriveSystem;
import frc.robot.systems.HatchSystem;
import frc.robot.systems.RobotSystem;
import frc.robot.util.ValueMap;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  //Runs when the robot is first started up.
  @Override
  public void robotInit() {
    RobotSystem.registerSystem("drive", new DriveSystem());
    RobotSystem.registerSystem("hatch", new HatchSystem());

    Compressor compressor = new Compressor(ValueMap.COMPRESSOR_PORT);
    compressor.start();
    compressor.setClosedLoopControl(true);
  }

  private long lastPeriodic = System.currentTimeMillis();
  //Runs periodically, in any mode.
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("bchmk_packetperiod", System.currentTimeMillis() - lastPeriodic);
    lastPeriodic = System.currentTimeMillis();
  }

  //Runs when autonomous is started.
  @Override
  public void autonomousInit() {
  }

  //Runs periodically during autonomous.
  @Override
  public void autonomousPeriodic() {
    long startTime = System.currentTimeMillis();
    
    for(RobotSystem system : RobotSystem.allSystems())
      system.updateAutonomous();

    SmartDashboard.putNumber("bchmk_auto", System.currentTimeMillis() - startTime);
  }

  //Runs periodically during tele-op.
  @Override
  public void teleopPeriodic() {
    long startTime = System.currentTimeMillis();

    for(RobotSystem system : RobotSystem.allSystems())
      system.updateTeleop();

    SmartDashboard.putNumber("bchmk_teleop", (System.currentTimeMillis() - startTime));
  }

  //Runs periodically during test.
  @Override
  public void testPeriodic() {
  }
}