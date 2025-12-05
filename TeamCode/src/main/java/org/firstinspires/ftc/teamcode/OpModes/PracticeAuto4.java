package org.firstinspires.ftc.teamcode.OpModes;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
//import org.firstinspires.ftc.teamcode.TestServo;

@Autonomous
public final class PracticeAuto4 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);
       // TestServo servo = new TestServo();
        //servo.init(hardwareMap);

        TrajectoryActionBuilder traj1 = drive.actionBuilder(beginPose)
                .splineTo(new Vector2d(-5, 5), Math.PI/2);

        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh()
                .strafeTo(new Vector2d(18, 18));

        Action act1=traj1.build();
        Action act2=traj2.build();

        waitForStart();

        Actions.runBlocking(
                new SequentialAction(
                        act1
                )
        );

    }

}

