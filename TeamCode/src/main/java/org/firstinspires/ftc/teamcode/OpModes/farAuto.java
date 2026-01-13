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

public final class farAuto extends LinearOpMode {
    private Limelight3A limelight;

    LLResult result;

    int pattern = 1;

    int indexerZero = 0;

    int spinnyTarget;
    double indexerPose;
    double runtime;
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
       // initLimelight(hardwareMap);


        TrajectoryActionBuilder scan = drive.actionBuilder(beginPose)
                .strafeTo(new Vector2d(0, -25));//-x=backwards was -19

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

        Action Park = scan.build();
        Action Shoot1 = shoot1.build();
        Action PreEat1 = preEat1.build();
        Action Eat1 = eat1.build();
        Action Shoot2 = shoot2.build();
        Action PreEat2 = preEat2.build();
        Action Eat2 = eat2.build();
        Action Shoot3 = shoot3.build();
        Action Park1 = park.build();




        waitForStart();

        indexer.secondStageOn();
        indexer.flapUp();
        indexer.flapDown();
        Actions.runBlocking(
                new SequentialAction(
                        Park
                )
        );



    };




}
