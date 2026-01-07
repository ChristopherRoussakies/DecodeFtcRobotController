package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Indexer2;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.List;

@Autonomous
public class BorpAutoBlue extends LinearOpMode {
    private Limelight3A limelight;
    LLResult result;
    int pattern = 1;
    double runtime;
    double intakeTime;
    int[] colors = {1, 2, 2};
    boolean limelightStarted = false;
    boolean intakeStarted = false;
    boolean restartTimer = true;
    int lastLoadSlot=1;
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Indexer2 index = new Indexer2();
        Shooter shoot = new Shooter();

        index.init(hardwareMap);
        shoot.init(hardwareMap);
        initLimelight(hardwareMap);


        TrajectoryActionBuilder scan = drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(-25, 0), -Math.PI / 2);//-x=backwards was -19

        TrajectoryActionBuilder shoot1 = scan.endTrajectory().fresh()
                .turnTo(Math.toRadians(10));

        TrajectoryActionBuilder preEat1 = shoot1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-45, 25), Math.PI / 4); //(-39, 27)

        TrajectoryActionBuilder eat1 = preEat1.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(-23, 37));
        new TranslationalVelConstraint(15);

        TrajectoryActionBuilder shoot2 = eat1.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-19, 0), 0);

        TrajectoryActionBuilder preEat2 = shoot2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-53, 43), Math.PI / 4);

        TrajectoryActionBuilder eat2 = preEat2.endTrajectory().fresh()
                .strafeToConstantHeading(new Vector2d(-41, 54));

        TrajectoryActionBuilder shoot3 = eat2.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-19, 0), 0);

        TrajectoryActionBuilder park = shoot1.endTrajectory().fresh()
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

        Action startShooter = packet -> {
            shoot.setShooterSpeed(2300);
            return false;
        };

        Action stopShooter = packet -> {
            shoot.setShooterSpeed(0);
            return false;
        };

        Action indexerLoad1 = packet -> {
            index.indexerLoad1();
            lastLoadSlot=1;
            return false;
        };

        Action indexerLoad2 = packet -> {
            index.indexerLoad2();
            lastLoadSlot=2;
            return false;
        };

        Action indexerLoad3 = packet -> {
            index.indexerLoad3();
            lastLoadSlot=3;
            return false;
        };

        Action indexerShoot1 = packet -> {
            index.indexerShoot1();
            return false;
        };

        Action indexerShoot2 = packet -> {
            index.indexerShoot2();
            return false;
        };

        Action indexerShoot3 = packet -> {
            index.indexerShoot3();
            return false;
        };

        Action runIndexer = packet -> {
            return (index.updateIndexer() < 8);
        };

        Action flipperUp = packet -> {
            index.flapUp();
            index.updateIndexer();
            return (index.getFlappyPose()<2);
        };

        Action flipperDown = packet -> {
            index.flapDown();
            index.updateIndexer();
            return (index.getFlappyPose()>0.95);
        };

        Action intakeOn = packet -> {
            index.intakeOn();
            return false;
        };

        Action intakeOff = packet -> {
            index.intakeOff();
            intakeStarted=false;
            return false;
        };

        Action intakeSecondStage = packet -> {
            index.secondIntakeStageOn();
            return false;
        };

        Action loadGreen = packet -> {
            if (colors[0]==1){
                index.indexerShoot1();
                packet.put("LOADING GREEN", "SLOT 1");
                colors[0]=0;
            }
            else if (colors[1]==1){
                index.indexerShoot2();
                packet.put("LOADING GREEN", "SLOT 2");
                colors[1]=0;
            }
            else if (colors[2]==1){
                index.indexerShoot3();
                packet.put("LOADING GREEN", "SLOT 3");
                colors[2]=0;
            }
            else{
                packet.put("ERROR","NO GREEN");
            }
            return false;
        };

        Action loadPurple = packet -> {
            if (colors[0]==2){
                index.indexerShoot1();
                packet.put("LOADING PURPLE", "SLOT 1");
                colors[0]=0;
            }
            else if (colors[1]==2){
                index.indexerShoot2();
                packet.put("LOADING PURPLE", "SLOT 2");
                colors[1]=0;
            }
            else if (colors[2]==2){
                index.indexerShoot3();
                packet.put("LOADING PURPLE", "SLOT 3");
                colors[2]=0;
            }
            else{
                packet.put("ERROR","NO PURPLE");
            }
            return false;
        };

        Action autoIntake = packet -> {
            if (!intakeStarted){
                intakeTime = getRuntime();
                intakeStarted=true;
            }
           if (restartTimer) {
               runtime=getRuntime();
               restartTimer=false;
           }
           // TUNE TIME IN NEXT LINE FOR TIME BETWEEN ROTATIONS
           if (getRuntime()-runtime>0.75){
               if (lastLoadSlot==1){
                   index.indexerLoad2();
                   lastLoadSlot=2;
               } else if(lastLoadSlot==2){
                   index.indexerLoad3();
                   lastLoadSlot=3;
               } else if (lastLoadSlot==3){
                   index.indexerLoad1();
                   lastLoadSlot=1;
               }
               restartTimer=true;
           }
           index.updateIndexer();
           colors=index.readIndexerColors(colors);
           //TUNE TIME IN NEXT LINE FOR MAX TIME SPENT INTAKING
           return((colors[0]==0 || colors[1]==0 || colors[2]==0) && intakeTime<5);
        };





        Action limelightStuff = packet -> {
            if (!limelightStarted){
                runtime = getRuntime();
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
            telemetry.addData("Pattern", pattern);
            telemetry.update();
            return (getRuntime()-runtime < 2);
        };
        while (colors[0]==0 || colors[1]==0 || colors[2]==0){
            colors=index.readIndexerColors(colors);
        }
        telemetry.addData("Color1", colors[0]);
        telemetry.addData("Color2", colors[1]);
        telemetry.addData("Color3", colors[2]);
        waitForStart();

        Actions.runBlocking(
                new ParallelAction(
                        startShooter,
                        intakeSecondStage
                        ,
                        new SequentialAction(
                                // Add initial wait here
                                new SleepAction(0),//_____ Adjust Delay
                                //Drives the robot off the goal and faces the obelisk
                                Scan,
                                limelightStuff


                        )
                )
        );

        if (pattern==1){
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    Shoot1,
                                    //loadGreen,
                                    //runIndexer
                                    new SequentialAction(
                                            loadGreen,
                                            runIndexer
                                    )

                            ),
                            flipperUp,
                            flipperDown,
                            loadPurple,
                            runIndexer,
                            flipperUp,
                            flipperDown,
                            loadPurple,
                            runIndexer,
                            flipperUp,
                            flipperDown
                    )

            );
        } else if (pattern==2){
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    Shoot1,
                                    new SequentialAction(
                                            loadPurple,
                                            runIndexer
                                    )

                            ),
                            flipperUp,
                            flipperDown,
                            loadGreen,
                            runIndexer,
                            flipperUp,
                            flipperDown,
                            loadPurple,
                            runIndexer,
                            flipperUp,
                            flipperDown
                    )

            );
        } else {
            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    Shoot1,
                                    new SequentialAction(
                                            loadPurple,
                                            runIndexer
                                    )

                            ),
                            flipperUp,
                            flipperDown,
                            loadPurple,
                            runIndexer,
                            flipperUp,
                            flipperDown,
                            loadGreen,
                            runIndexer,
                            flipperUp,
                            flipperDown
                    )

            );
        }

    }
    public void initLimelight(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }
}
