package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;
import java.util.Objects;


public class Shooter {
    private DcMotorEx shooter1;
    private DcMotorEx shooter2;
    private CRServo feedservo;
    private Limelight3A limelight;
    LLResult result;

    double limelightAngle=21;
    double limelightHeight=9.875;
    double targetHeight=29.5;



    public void init(HardwareMap hwMap){
        feedservo = hwMap.get(CRServo.class, "feedServo");
        shooter1 = hwMap.get(DcMotorEx.class, "shooter1");
        shooter2 = hwMap.get(DcMotorEx.class, "shooter2");

        //feedservo.setDirection(CRServo.Direction.REVERSE);

        shooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //shooter2.setDirection(DcMotorSimple.Direction.REVERSE);
        //shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        limelight = hwMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(1);
        limelight.start();
    }

    public double limelightRangeAndHeading(){
        result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        double distanceToGoal=0;
        for (LLResultTypes.FiducialResult fr : fiducialResults) {

            if (fr.getFiducialId() == 20 || fr.getFiducialId() == 24) {
                double angleToGoal=(-fr.getTargetYDegrees()+limelightAngle)*(3.14/180);
                distanceToGoal=(targetHeight-limelightHeight)/Math.tan(angleToGoal);
            }
        }
        return distanceToGoal;
    }

    public void feedServoOn(){
        feedservo.setPower(1);
    }

    public Action feedServoOnAuto(){
            feedservo.setPower(1);
            return Objects::nonNull;
    }

    public void feedServoOff(){
        feedservo.setPower(.5);
    }

    public Action feedServoOffAuto(){
            feedservo.setPower(0);
            return Objects::nonNull;
    }


    //Input TargetSpeed in RPM
    public void setShooterSpeed(double targetspeed){
        shooter1.setVelocity(targetspeed*28/60);
        shooter2.setVelocity(targetspeed*28/60);
    }

    public Action shootCloseAuto(){
            shooter1.setVelocity(1073.33);
            shooter2.setVelocity(1073.33);
            return Objects::nonNull;
    }

    public Action noShootAuto(){
        shooter1.setVelocity(0);
        shooter2.setVelocity(0);
        return Objects::nonNull;
    }

    public double getShooterSpeed(){
        return(shooter1.getVelocity()*60/28);
    }
}
