package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@TeleOp
public class RemoteControl extends OpMode {

    MecanumDrive drive;
    Shooter shoot = new Shooter();
    @Override
    public void init(){
        drive = new MecanumDrive(hardwareMap, PoseStorage.currentPose);
        shoot.init(hardwareMap);
    }

    @Override
    public void loop(){
        if (gamepad1.a){
            shoot.feedServoOn();
            shoot.setShooterSpeed(3600);
        }

        if (gamepad1.b){
            shoot.feedServoOff();
            shoot.setShooterSpeed(0);
        }

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x
                ),
                -gamepad1.right_stick_x
        ));

        drive.updatePoseEstimate();

        Pose2d pose = drive.localizer.getPose();
        telemetry.addData("Shooter Speed",shoot.getShooterSpeed());
        telemetry.addData("x", pose.position.x);
        telemetry.addData("y", pose.position.y);
        telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
        telemetry.update();
    }

}
