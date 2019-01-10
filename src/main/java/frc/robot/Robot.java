/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.systems.DriveSystem;
import frc.robot.systems.RobotSystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private DriveSystem driveSystem;

  //Runs when the robot is first started up.
  @Override
  public void robotInit() {
    driveSystem = new DriveSystem();
  }

  //Runs periodically, in any mode.
  @Override
  public void robotPeriodic() {
  }

  //Runs when autonomous is started.
  @Override
  public void autonomousInit() {
  }

  //Runs periodically during autonomous.
  @Override
  public void autonomousPeriodic() {
  }

  //Runs periodically during tele-op.
  @Override
  public void teleopPeriodic() {
    driveSystem.update();
  }

  //Runs periodically during test.
  @Override
  public void testPeriodic() {
  }
}