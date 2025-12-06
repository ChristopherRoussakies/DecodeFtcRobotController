package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
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

public final class Blue9Artifact extends LinearOpMode {
    private Limelight3A limelight;

    LLResult result;

    int pattern = 1;

    int indexerZero = 0;

    int spinnyTarget;
    double indexerPose;
    int[] colors = {0, 0, 0};

    boolean limelightStarted = false;

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Indexer indexer = new Indexer();
        Shooter shooter = new Shooter();

        indexer.init(hardwareMap);
        shooter.init(hardwareMap);
        initLimelight(hardwareMap);


        TrajectoryActionBuilder scan = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(-19, 0), -Math.PI / 4);//-x=backwards

        TrajectoryActionBuilder shoot1 = scan.endTrajectory().fresh()
                .turnTo(0);

        TrajectoryActionBuilder preEat1 = shoot1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-39, 28), Math.PI / 4); //(-39, 27)

        TrajectoryActionBuilder eat1 = preEat1.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(-26, 37));
                new TranslationalVelConstraint(20);

        TrajectoryActionBuilder shoot2 = eat1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-19, 0), 0);

        TrajectoryActionBuilder preEat2 = shoot2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-53, 43), Math.PI / 4);

        TrajectoryActionBuilder eat2 = preEat2.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(-41, 54));

        TrajectoryActionBuilder shoot3 = eat2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-19, 0), 0);

        TrajectoryActionBuilder park = shoot3.endTrajectory().fresh()
                .strafeTo(new Vector2d(-19, 25)); //-y=right

        Action Scan = scan.build();
        Action Shoot1 = shoot1.build();
        Action PreEat1 = preEat1.build();
        Action Eat1 = eat1.build();
        Action Shoot2 = shoot2.build();
        Action PreEat2 = preEat2.build();
        Action Eat2 = eat2.build();
        Action Shoot3 = shoot3.build();
        Action Park = park.build();

        Action spinShooterUp = packet -> {
            shooter.setShooterSpeed(2300);
            return false;
        };

        Action stopShooter = packet -> {
            shooter.setShooterSpeed(2300);
            return false;
        };

        Action shooterFlapUp = packet -> {
            indexer.flapUp();
            if (0.333 < indexerPose && indexerPose < 0.667) {
                colors[0] = 0;
            } else if (0.667 < indexerPose && indexerPose < 1) {
                colors[1] = 0;
            } else {
                colors[2] = 0;
            }
            return false;
        };

        Action shooterFlapDown = packet -> {
            indexer.flapDown();
            return false;
        };

        Action sortIndexer = packet -> {
            indexer.runSpinny(179);
            if (!indexer.getMagnetState()){
                indexerZero = indexer.getSpinnyPose();
            }
            colors = indexer.readIndexer(indexerZero, colors);
            packet.put("Colors", colors);
            return (colors[0]==0 || colors[1]==0 || colors[2]==0);
        };

        Action stopIndexer = packet -> {
            indexer.runSpinny(0);
            return false;
        };

        Action limelightStuff = packet -> {
            if (!limelightStarted){
                double time = getRuntime();
                limelightStarted=true;
            }
            result = limelight.getLatestResult();
            //This is a list that stores data of type LLResultTypes.FiducialResult
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();

            for (LLResultTypes.FiducialResult fr : fiducialResults) {

                packet.put("April Tag ID:", fr.getFiducialId());

                if (fr.getFiducialId() == 21) {
                    pattern = 1;
                    break;
                } else if (fr.getFiducialId() == 22) {
                    pattern = 2;
                    break;
                } else if (fr.getFiducialId() == 23) {
                    pattern = 3;
                    break;
                }
            }
            return (getRuntime()-time < 2);
        };

        Action loadGreen = packet -> {
            if (colors[0]==1){
                indexer.runSpinnyToPose(1, indexerZero);
                spinnyTarget=indexer.getSpinnyTargetPose(1, indexerZero);
                packet.put("LOADING GREEN", "SLOT 1");
            }
            else if (colors[1]==1){
                indexer.runSpinnyToPose(2, indexerZero);
                spinnyTarget=indexer.getSpinnyTargetPose(2, indexerZero);
                packet.put("LOADING GREEN", "SLOT 2");
            }
            else if (colors[2]==1){
                indexer.runSpinnyToPose(3, indexerZero);
                spinnyTarget=indexer.getSpinnyTargetPose(3, indexerZero);
                packet.put("LOADING GREEN", "SLOT 3");
            }
            else{
                packet.put("ERROR","NO GREEN");
            }
            return Math.abs(spinnyTarget-indexer.getSpinnyPose())<15;
        };

        Action loadPurple = packet -> {
            if (colors[0]==2){
                indexer.runSpinnyToPose(1, indexerZero);
                spinnyTarget=indexer.getSpinnyTargetPose(1, indexerZero);
                packet.put("LOADING PURPLE", "SLOT 1");
            }
            else if (colors[1]==2){
                indexer.runSpinnyToPose(2, indexerZero);
                spinnyTarget=indexer.getSpinnyTargetPose(2, indexerZero);
                packet.put("LOADING PURPLE", "SLOT 2");
            }
            else if (colors[2]==2){
                indexer.runSpinnyToPose(3, indexerZero);
                spinnyTarget=indexer.getSpinnyTargetPose(3, indexerZero);
                packet.put("LOADING PURPLE", "SLOT 3");
            }
            else{
                packet.put("ERROR","NO PURPLE");
            }
            return Math.abs(spinnyTarget-indexer.getSpinnyPose())<15;
        };


        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        new SequentialAction(
                                // Add initial wait here
                                new SleepAction(0),//_____ Adjust Delay
                                //Drives the robot off the goal and faces the obelisk
                                Scan,
                                limelightStuff
                        )
                        ,
                        new SequentialAction(
                                spinShooterUp,
                                sortIndexer,
                                stopIndexer
                        )
                )
        );

        if (pattern==1){
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                Shoot1
                                ,
                                loadGreen
                            ),
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25),
                            loadPurple,
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25),
                            loadPurple,
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25)
                    )

            );
        } else if (pattern==2){
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    Shoot1
                                    ,
                                    loadPurple
                            ),
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25),
                            loadGreen,
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25),
                            loadPurple,
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25)
                    )

            );
        } else {
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    Shoot1
                                    ,
                                    loadPurple
                            ),
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25),
                            loadPurple,
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25),
                            loadGreen,
                            shooterFlapUp,
                            new SleepAction(0.25),
                            shooterFlapDown,
                            new SleepAction(0.25)
                    )

            );
        }
        /*

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
                        Shoot1

                )
        );
 */
        indexer.runSpinny(179);
        indexer.intakeOn();
        indexer.secondStageOn();

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        PreEat1

                )
        );

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        Eat1

                )
        );

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        Shoot2

                )
        );

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        PreEat2

                )
        );

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        Eat2

                )
        );

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        Shoot3

                )
        );

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0),
                        Park

                )
        );

    };



    public void initLimelight(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }

}
