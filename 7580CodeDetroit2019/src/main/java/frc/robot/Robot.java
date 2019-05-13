/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import  edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Compressor;
//import edu.wpi.first.wpilibj.UsbCamera;
//import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import jdk.nashorn.internal.objects.annotations.Where;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */




public class Robot extends TimedRobot {

  private int accelValue;
  private PWMVictorSPX m_left_1 = new PWMVictorSPX(0);
  private PWMVictorSPX m_left_2 = new PWMVictorSPX(1);
  private PWMVictorSPX m_right_1 = new PWMVictorSPX(2);
  private PWMVictorSPX m_right_2 = new PWMVictorSPX(3);

  private final SpeedControllerGroup m_left = new SpeedControllerGroup(m_left_1, m_left_2);
  private final SpeedControllerGroup m_right = new SpeedControllerGroup(m_right_1, m_right_2);
  private final DifferentialDrive m_robotDrive
     = new DifferentialDrive(m_left, m_right);

     //private final DifferentialDrive m_robotDrive2
     //= new DifferentialDrive(new PWMVictorSPX(2), new PWMVictorSPX(2));

  private final Joystick m_stick = new Joystick(0);

  // private final Gamepad joystick310 = new Gamepad(0);

  private final Timer m_timer = new Timer();

  private final Compressor c = new Compressor(0);
  //private final DoubleSolenoid s = new DoubleSolenoid(1,2);
  private Solenoid s1 = new Solenoid(0);
  private Solenoid s2 = new Solenoid(1);
  private Solenoid s3 = new Solenoid(2);
  
  

    //RobotDrive myDrive;
    Victor frontLeft, frontRight, rearLeft, rearRight;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

     CameraServer.getInstance().startAutomaticCapture();
     m_left_1.setInverted(true);
     m_left_2.setInverted(false);
     m_right_1.setInverted(false);
     m_right_2.setInverted(true);
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // // Drive for 2 seconds
    // if (m_timer.get() < 2.0) {
    //   m_robotDrive.arcadeDrive(0.0, 0.0); // drive forwards half speed
    // } else {
    //   m_robotDrive.stopMotor(); // stop robot
    // }
    teleopPeriodic();
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {

    boolean button6press; //used for accel, RB button
    boolean button4press; //used to start compressor, LB button 
    boolean button1press; //used to engage solenoid
    boolean button5press; //used to decel, LB button
    boolean leftTriggerPress;
    
    button5press = m_stick.getRawButtonPressed(5);
    button4press = m_stick.getRawButtonPressed(4);
    button1press = m_stick.getRawButtonPressed(1);
    button6press = m_stick.getRawButtonPressed(6);
    leftTriggerPress = Math.abs(m_stick.getRawAxis(2)) > 0.5;

    double leftSpeed = m_stick.getRawAxis(1);
    double rightSpeed = m_stick.getRawAxis(5);

    if (button6press)
    {
       ++accelValue;
       if (accelValue > 3)
       {
         accelValue = 3;
       }    
    }
    if (button5press)
    {
       
       if (accelValue > 0)
       {
         --accelValue;
       }    
    }

    //if (button2press)
    
    c.setClosedLoopControl(true);
    
    if (button1press)
    {
      s1.set(true);
      s2.set(false);
      //try {
      //  wait(5);
      //} catch (InterruptedException e) {
      // 
      //}
      //s1.set(false);
    }
    
    if (button4press)
    {
      s1.set(false);
      s2.set(true);
      //try {
      //  wait(5);
      //} catch (InterruptedException e) {
      //
      //}
      //s2.set(false);
    }
    if(leftTriggerPress)
    {
      s3.set(true);
    }
    if(!leftTriggerPress)
    {
      s3.set(false);
    }
  
  
    //control left side motors
    //m_robotDrive.arcadeDrive(joystick310.getLeftY(), joystick310.getLeftX());
    

    //triggerValue = m_stick.getRawButton(4);
  
    //button6press = m_stick.getRawButtonPressed(4);

    if (accelValue == 3)
    {
      // m_robotDrive.arcadeDrive(m_stick.getY()/-1, m_stick.getX()/-1);
      m_robotDrive.tankDrive(leftSpeed/-1, rightSpeed/-1);
    }
    else if (accelValue == 2)
    {
      // m_robotDrive.arcadeDrive(m_stick.getY()/-1.5, m_stick.getX()/-1.5);
      m_robotDrive.tankDrive(leftSpeed/-1.5, rightSpeed/-1.5);
    }
    else if (accelValue == 1)
    {
      // m_robotDrive.arcadeDrive(m_stick.getY()/-1.75, m_stick.getX()/-1.75);
      m_robotDrive.tankDrive(leftSpeed/-1.75, rightSpeed/-1.75);
    }
    else
    {
      // m_robotDrive.arcadeDrive(m_stick.getY()/-2, m_stick.getX()/-2);
      m_robotDrive.tankDrive(leftSpeed/-1.75, rightSpeed/-1.75);
    
    }
    
    // //control right side motors
    
    // if (accelValue == 3)
    // {
    //   m_robotDrive2.arcadeDrive(joystick310.getRightY()/-1, joystick310.getRightX()/-1);
    // }
    // else if (accelValue == 2)
    // {
    //   m_robotDrive2.arcadeDrive(joystick310.getRightY()/-1.5, joystick310.getRightX()/-1.5);

    // }
    // else if (accelValue == 1)
    // {
    //   m_robotDrive2.arcadeDrive(joystick310.getRightY()/-1.75, joystick310.getRightX()/-1.75);

    // }
    // else
    // {
    //   m_robotDrive2.arcadeDrive(joystick310.getRightY()/-2, joystick310.getRightX()/-2);
    // }

  
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
