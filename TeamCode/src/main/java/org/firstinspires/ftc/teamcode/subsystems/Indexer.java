package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Objects;

public class Indexer {
    private DcMotorEx Spinny;
    private DcMotorEx Intake;
    private Servo Flappy;
    private Servo Flappy2;
    private CRServo Intake2;
    private DigitalChannel Switchy;
    private RevColorSensorV3 Lighty;
    double indexerPose;
    int targetIndexPose;

    public void init(HardwareMap hwMap){
        Spinny = hwMap.get(DcMotorEx.class, "indexer");
        Spinny.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Intake = hwMap.get(DcMotorEx.class, "intake");
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Flappy = hwMap.get(Servo.class, "indexerServo");
        Flappy2 = hwMap.get(Servo.class, "indexerServo2");
        Intake2 = hwMap.get(CRServo.class, "intakeServo");
        Switchy = hwMap.get(DigitalChannel.class, "magneticLimitSwitch");
        Switchy.setMode(DigitalChannel.Mode.INPUT);
        Lighty = hwMap.get(RevColorSensorV3.class, "colorSensor");
    }

    public void intakeOn(){
        Intake.setVelocity(1000);
        Intake2.setPower(-1);
    }

    public  void intakeReverse (){
        Intake.setVelocity(-750);
        //Intake2.setPower(1);
    }
    public void secondStageOn(){
        Intake2.setPower(-1);
    }
    public void intakeOff(){
        Intake.setVelocity(0);
        //Intake2.setPower(0);
    }
    public void indexerReverse(double speed){
        Spinny.setVelocity(speed);
    }
    public boolean getMagnetState(){
        return Switchy.getState();
    }

    public void runSpinny(double speed){
        Spinny.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Spinny.setVelocity(speed);
        //Set Velocity accepts input in ticks per second
        // 312 rpm motor 537.7 ticks per rotation
        // shooting 1/3 rotation per second or 179 ticks per second
    }

    public void runSpinnyToPose(int targetSlot, int zeroPose){
        indexerPose = (((getSpinnyPose() - zeroPose) / 537.7) % 1);
        if (targetSlot==1){
            targetIndexPose = getSpinnyPose() + (int) Math.round((0.5 - indexerPose)*537.7);
        } else if (targetSlot==2) {
            targetIndexPose = getSpinnyPose() + (int) Math.round((0.833 - indexerPose)*537.7);
        }
        else {
            targetIndexPose = getSpinnyPose() + (int) Math.round((0.167 - indexerPose)*537.7);
        }
        Spinny.setTargetPosition(targetIndexPose);
        Spinny.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Spinny.setPower(.05);
    }

    public int getSpinnyTargetPose(int targetSlot, int zeroPose){
        indexerPose = (((getSpinnyPose() - zeroPose) / 537.7) % 1);
        if (targetSlot==1){
            targetIndexPose = getSpinnyPose() + (int) Math.round((0.5 - indexerPose)*537.7);
        } else if (targetSlot==2) {
            targetIndexPose = getSpinnyPose() + (int) Math.round((0.833 - indexerPose)*537.7);
        }
        else {
            targetIndexPose = getSpinnyPose() + (int) Math.round((0.167 - indexerPose)*537.7);
        }
        return targetIndexPose;
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
        Flappy.setPosition(.25);
        Flappy2.setPosition(1);
    }
    public void flapDown(){
        Flappy.setPosition(.99);
        Flappy2.setPosition(.03);
    }
    public Action flapUpAuto() {
            Flappy.setPosition(.145);
            return Objects::nonNull;
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

    public int[] readIndexer(int zeroPose, int[] colors) {
        indexerPose = (((getSpinnyPose() - zeroPose) / 537.7) % 1);
        if (0.167 < indexerPose && indexerPose < 0.5) {
            if (colors[0] == 0) {
                colors[0] = readColor();
            } else {
                colors[0] = updateColor(colors[0]);
            }
        } else if (0.5 < indexerPose && indexerPose < 0.833) {
            if (colors[1] == 0) {
                colors[1] = readColor();
            } else {
                colors[1] = updateColor(colors[1]);
            }
            ;
        } else if (0.833 < indexerPose && indexerPose < 1 || 0 < indexerPose && indexerPose < 0.167) {
            if (colors[2] == 0) {
                colors[2] = readColor();
            } else {
                colors[2] = updateColor(colors[2]);
            }
        }
        return colors;
    }
}
