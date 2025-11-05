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

public final class BlueClose extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Indexer indexer = new Indexer();
        Shooter shooter = new Shooter();
        Limelight limelight = new Limelight();


        TrajectoryActionBuilder traj1 = drive.actionBuilder(beginPose)
                .lineToXConstantHeading(-19) //-x=backwards
                .waitSeconds(5);
                //.stopAndAdd(shoot.setShooterSpeed(2300));
                        //.stopAndAdd(index.)
                //.splineTo(new Vector2d(0, 24), Math.PI);

        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh()
                .strafeTo(new Vector2d(-19, 23)); //-y=right
               // .stopAndAdd(shoot.setShooterSpeed(0));
               // .strafeTo(new Vector2d(0, 0));

        Action act1=traj1.build();
        Action act2=traj2.build();

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        shooter.setShooterSpeedAuto(2300),
                        indexer.flapUpAuto(),
                        shooter.feedServoOnAuto(),
                        act1,
                        indexer.runSpinnyAuto(.1),
                        new SleepAction(5),
                        shooter.setShooterSpeedAuto(0),
                        indexer.flapDownAuto(),
                        shooter.feedServoOffAuto(),
                        act2
                )
        );

    }
}