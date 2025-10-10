package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Indexer {
    private DcMotorEx Spinny;
    private Servo Flappy;
    private DigitalChannel Switchy;
    private ColorSensor Lighty;

    public void init(HardwareMap hwMap){
        Spinny = hwMap.get(DcMotorEx.class, "indexer");
        Spinny.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Flappy = hwMap.get(Servo.class, "indexerServo");
        Switchy = hwMap.get(DigitalChannel.class, "magneticLimitSwitch");
        Switchy.setMode(DigitalChannel.Mode.INPUT);
        Lighty = hwMap.get(ColorSensor.class, "colorSensor");
    }

    public boolean getMagnetState(){
        return Switchy.getState();
    }

    public void runSpinny(double speed){
        Spinny.setVelocity(speed);
        //Set Velocity accepts input in ticks per second
        // 312 rpm motor 537.7 ticks per rotation
        // shooting 1/3 rotation per second or 179 ticks per second
    }

    public int getSpinnyPose(){
        return Spinny.getCurrentPosition();
    }

    public double getSpinnySpeed(){
        return(Spinny.getVelocity());
    }

    public int getRed(){
        return Lighty.red();
    }
    public int getGreen(){
        return Lighty.green();
    }
    public int getBlue(){
        return Lighty.blue();
    }

    public int readColor(){
        if (Lighty.green()+Lighty.blue()>2000){
            if (Lighty.green()>Lighty.blue()){
                return 1;
                //green
            }
            else{
                return 2;
                //purple
            }
        }
        else {
            return 0;
            //empty
        }
    }
}
