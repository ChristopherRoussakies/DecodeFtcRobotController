package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;

import java.util.List;
/*
README
LINE 87  Adjust Start Delay
LINE 98  Adjust Limelight Delay
LINE 139 Adjust Index Delay
LINE 256 Adjust Final Shot Delay

 */
@Autonomous

public final class BlueCloseMotifAuto extends LinearOpMode {
    private Limelight3A limelight;

    LLResult result;

    int pattern = 1;

    int indexerZero = 0;
    double indexerPose;
    int color1 = 0;
    int color2 = 0;
    int color3 = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Indexer indexer = new Indexer();
        Shooter shooter = new Shooter();

        indexer.init(hardwareMap);
        shooter.init(hardwareMap);
        initLimelight(hardwareMap);


        TrajectoryActionBuilder traj1 = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(-19, 0),-Math.PI / 4);//-x=backwards

        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh()
                .turnTo(0);

        TrajectoryActionBuilder traj3 = traj2.endTrajectory().fresh()
                .strafeTo(new Vector2d(-19, 23)); //-y=right

        Action act1=traj1.build();
        Action act2=traj2.build();
        Action act3=traj3.build();

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        // Add initial wait here
                        new SleepAction(0),//_____ Adjust Delay
                        //Drives the robot off the goal and faces the obelisk
                        act1
                )
        );

        //Runs Limelight for 2 seconds
        double time = getRuntime();

        //                        |
        //Adjust Limelight Delay \|/
        while(getRuntime()-time < 2){

            result = limelight.getLatestResult();
            //This is a list that stores data of type LLResultTypes.FiducialResult
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();

            for (LLResultTypes.FiducialResult fr : fiducialResults) {

                telemetry.addData("April Tag ID:", fr.getFiducialId());

                if (fr.getFiducialId() == 21){
                    pattern =1;
                    break;
                }

                else if (fr.getFiducialId() == 22){
                    pattern =2;
                    break;
                }

                else if (fr.getFiducialId() == 23){
                    pattern =3;
                    break;
                }

            }
        }

        //Turn back to goal
        Actions.runBlocking(
                new SequentialAction(
                        //add wait time if needed
                        new SleepAction(0),//_____ Adjust Delay
                        act2

                )
        );
        indexer.runSpinny(100);
        time = getRuntime();
        //                       |
        // Adjust Index Delay   \|/
        while((getRuntime()-time<5) && (color1==0 || color2==0 || color3==0)) {
            if (!indexer.getMagnetState()) {
                indexerZero = indexer.getSpinnyPose();
            }
            indexerPose = (((indexer.getSpinnyPose() - indexerZero) / 537.7) % 1);
            if (0.167 < indexerPose && indexerPose < 0.5) {
                if (color1 == 0) {
                    color1 = indexer.readColor();
                } else {
                    color1 = indexer.updateColor(color1);
                }
            } else if (0.5 < indexerPose && indexerPose < 0.833) {
                if (color2 == 0) {
                    color2 = indexer.readColor();
                } else {
                    color2 = indexer.updateColor(color2);
                }
            } else if (0.833 < indexerPose && indexerPose < 1 || 0 < indexerPose && indexerPose < 0.167) {
                if (color3 == 0) {
                    color3 = indexer.readColor();
                } else {
                    color3 = indexer.updateColor(color3);
                }
            }
        }
        shooter.setShooterSpeed(2300);
        for (int shot=1; shot<4; shot++){
            if(shot==pattern){
                //Load Green
                if (color1==1){
                    while(!(0.4<indexerPose && indexerPose<0.6)) {
                        indexer.runSpinny(400);
                        telemetry.addData("Shot Status","LOADING GREEN");
                    }
                    indexer.runSpinny(0);
                    color1=0;
                }
                else if (color2==1){
                    while(!(0.733<indexerPose && indexerPose<0.933)) {
                        indexer.runSpinny(400);
                        telemetry.addData("Shot Status","LOADING GREEN");
                    }
                    indexer.runSpinny(0);
                    color2=0;
                }
                else if (color3==1){
                    while(!(0.067<indexerPose && indexerPose<0.267)) {
                        indexer.runSpinny(400);
                        telemetry.addData("Shot Status","LOADING GREEN");
                    }
                    indexer.runSpinny(0);
                    color3=0;
                }
                else{
                    telemetry.addData("ERROR","NO GREEN");
                }
                //Shoot Green
                indexer.flapUp();
                shooter.feedServoOn();
                indexer.runSpinny(50);
                time = getRuntime();
                //Tune this time value for how long to leave the feed system on
                while(getRuntime()-time<0.5){
                    telemetry.addData("Shot Status","SHOOTING GREEN");
                }
                indexer.flapDown();
                indexer.runSpinny(0);
            }
            else{
                //Load Purple
                if (color1==2){
                    while(!(0.4<indexerPose && indexerPose<0.6)) {
                        indexer.runSpinny(400);
                        telemetry.addData("Shot Status","LOADING PURPLE");
                    }
                    indexer.runSpinny(0);
                    color1=0;
                }
                else if (color2==2){
                    while(!(0.733<indexerPose && indexerPose<0.933)) {
                        indexer.runSpinny(400);
                        telemetry.addData("Shot Status","LOADING PURPLE");
                    }
                    indexer.runSpinny(0);
                    color2=0;
                }
                else if (color3==2){
                    while(!(0.067<indexerPose && indexerPose<0.267)) {
                        indexer.runSpinny(400);
                        telemetry.addData("Shot Status","LOADING PURPLE");
                    }
                    indexer.runSpinny(0);
                    color3=0;
                }
                else{
                    telemetry.addData("ERROR","NO PURPLE");
                }
                //Shoot Green
                indexer.flapUp();
                shooter.feedServoOn();
                indexer.runSpinny(50);
                time = getRuntime();
                //Tune this time value for how long to leave the feed system on
                while(getRuntime()-time<0.5){
                    telemetry.addData("Shot Status","SHOOTING PURPLE");
                }
                indexer.flapDown();
                indexer.runSpinny(0);
            }
        }

        indexer.flapUp();
        shooter.feedServoOn();
        indexer.runSpinny(50);
        Actions.runBlocking(
                new SequentialAction(
                        //add wait time for shooter
                        new SleepAction(3),//_____ Adjust Delay
                        act3

                )
        );



    }

    public void initLimelight(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }

}
