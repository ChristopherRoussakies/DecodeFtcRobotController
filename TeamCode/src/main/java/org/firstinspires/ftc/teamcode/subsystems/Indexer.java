package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Objects;

public class Indexer {
    private DcMotorEx Spinny;
    private Servo Flappy;
    private DigitalChannel Switchy;
    private RevColorSensorV3 Lighty;

    public void init(HardwareMap hwMap){
        Spinny = hwMap.get(DcMotorEx.class, "indexer");
        Spinny.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Flappy = hwMap.get(Servo.class, "indexerServo");
        Switchy = hwMap.get(DigitalChannel.class, "magneticLimitSwitch");
        Switchy.setMode(DigitalChannel.Mode.INPUT);
        Lighty = hwMap.get(RevColorSensorV3.class, "colorSensor");
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
    public Action runSpinnyAuto(){
            Spinny.setPower(.3);
            return Objects::nonNull;
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

    public void flapUp (){
        Flappy.setPosition(.145);
    }

    public Action flapUpAuto() {
            Flappy.setPosition(.145);
            return Objects::nonNull;
    }

    public void flapDown(){
        Flappy.setPosition(.25);
    }

    public Action flapDownAuto() {
            Flappy.setPosition(.25);
            return Objects::nonNull;
    }

    public boolean full(){
        if ( Lighty.getDistance(DistanceUnit.CM)<5){
           return true;
        }
        else {
            return false;
        }
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
    public int updateColor(int color){
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
            return color;
            //empty
        }
    }
}
