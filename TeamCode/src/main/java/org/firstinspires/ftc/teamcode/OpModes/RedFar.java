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
@Config
@Autonomous

public final class RedFar extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Indexer indexer = new Indexer();
        Shooter shooter = new Shooter();
        //Limelight limelight = new Limelight();

        indexer.init(hardwareMap);
        shooter.init(hardwareMap);

        //If you guys need to make changes to the program, read the comments on lines 52, 55, 64, 70, and 78; -x=backwards, -y=right
        //Do not make changes unless absolutely necessary

        TrajectoryActionBuilder traj1 = drive.actionBuilder(beginPose)
                .splineTo(new Vector2d(96, -22), Math.PI/-4); //-x=backwards <This is the position you shoot from. Change this x if you need to shoot closer or farther from the goal, change this y if you need to move farther right or left relative to the goal to shoot

        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh()
                .strafeTo(new Vector2d(94, -45)); //-y=right <This is where you end. The x value should remain the same unless you need to change how far back from the goal you end. The number you would most likely have to change is the y value depending on where your alliance partners end

        Action act1=traj1.build();
        Action act2=traj2.build();

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(0), //this is where you can add time to wait before you start driving. Just replace the 0 with however many seconds you need to wait. You should have about 10 extra seconds to work with
                        act1

                )
        );

        //This is where the shooting actions occur, after you've lined up in position. You should have to change any of these unless timing or distance is off. If you find that you need to shoot farther from the goal, adjust the shooter rpm as necessary
        shooter.setShooterSpeed(2300);
        indexer.flapUp();
        shooter.feedServoOn();
        indexer.runSpinny(50); //Indexer speed. Change if ball is getting stuck or if it takes too long to shoot.

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(13), //this is the amount of time it takes to shoot the balls. If you find that you need more time to shoot, make this time longer
                        act2

                )
        );


    }
}