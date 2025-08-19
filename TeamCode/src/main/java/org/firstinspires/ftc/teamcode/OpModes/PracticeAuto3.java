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
import org.firstinspires.ftc.teamcode.PoseStorage;

@Autonomous
public final class PracticeAuto3 extends LinearOpMode {
    enum State {
        TRAJECTORY_1,
        TRAJECTORY_2
    }
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        TrajectoryActionBuilder traj1 = drive.actionBuilder(beginPose)
                .lineToX(-35)
                .splineToLinearHeading(new Pose2d(-52, 14,Math.PI/2), Math.PI / 2)
                .splineTo(new Vector2d(-61, 27), Math.PI)
                .splineTo(new Vector2d(-74,38), Math.PI / 2)
                .splineTo(new Vector2d(-86,49), Math.PI)
                .lineToX(-112)
                .splineTo(new Vector2d(-133,52), Math.PI);

        TrajectoryActionBuilder traj2 = traj1.endTrajectory().fresh()
                .strafeTo(new Vector2d(18, 18))
                .strafeTo(new Vector2d(0, 0));

        Action act1=traj1.build();
        Action act2=traj2.build();

        State state = State.TRAJECTORY_1;
        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(act1);

        switch (state){
            case TRAJECTORY_1:

            break;

            case TRAJECTORY_2:

            break;

        }


        Actions.runBlocking(
                new SequentialAction(
                        act1,
                        act2
                )
        );

    }

}
