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

public final class BlueCloseMama extends LinearOpMode {
 @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(0, 0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        Indexer indexer = new Indexer();
        Shooter shooter = new Shooter();
        Limelight limelight = new Limelight();
        indexer.init(hardwareMap);
        shooter.init(hardwareMap);
        limelight.initLimelight(hardwareMap);

        int indexerZero;

        int color1 = 0;
        int color2 = 0;
        int color3 = 0;
        double indexerPose;


        TrajectoryActionBuilder traj1 = drive.actionBuilder(beginPose)
                .lineToXConstantHeading(-19); //-x=backwards
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
                        new SleepAction(0),
                        act1

                )
        );
      /* int id = limelight.limelightId();
        if id == 21

        //indexerPose is where the magnet is relative to the magnet sensor in revolutions.
        // Slot one 1st after magnet
        // At color sensor: indexerPose = 0.333
        // At shooter: indexerPose = 0.5

        // Slot two 2nd after magnet
        // At color sensor: indexerPose = 0.667
        // At shooter: indexerPose = 0.833

        // Slot three 3rd after magnet
        // At color sensor: indexerPose = 0 or 1
        // At shooter: indexerPose = 0.167
        indexerPose = (((index.getSpinnyPose()-indexerZero)/537.7) % 1);
        if(0.167<indexerPose && indexerPose<0.5){
            if (color1 == 0){
                color1 = index.readColor();
            }
            else {
                color1 = index.updateColor(color1);
            }
        } else if (0.5<indexerPose && indexerPose<0.833) {
            if (color2 == 0){
                color2 = index.readColor();
            }
            else {
                color2 = index.updateColor(color2);
            };
        } else if (0.833<indexerPose && indexerPose<1 ||0<indexerPose && indexerPose<0.167 ) {
            if (color3 == 0){
                color3 = index.readColor();
            }
            else {
                color3 = index.updateColor(color3);
            }
        }
        // Load Green
        // Checks each slot for a green
        // Rotates to that slot then vibrates controller
        if (gamepad1.dpad_left){
            if (color1==1){
                if(!(0.4<indexerPose && indexerPose<0.6)) {
                    index.runSpinny(400);
                    telemetry.addData("Shot Status","LOADING GREEN");
                }
                else{
                    telemetry.addData("Shot Status","GREEN READY");
                }
            }
            else if (color2==1){
                if(!(0.733<indexerPose && indexerPose<0.933)) {
                    index.runSpinny(400);
                    telemetry.addData("Shot Status","LOADING GREEN");
                }
                else{
                    telemetry.addData("Shot Status","GREEN READY");

                }
            }
            else if (color3==1){
                if(!(0.067<indexerPose && indexerPose<0.267)) {
                    index.runSpinny(400);
                    telemetry.addData("Shot Status","LOADING GREEN");
                }
                else{
                    telemetry.addData("Shot Status","GREEN READY");

                }
            }
            else{
                telemetry.addData("ERROR","NO GREEN");
            }
        }

          shooter.setShooterSpeed(2300);
        indexer.flapUp();
        shooter.feedServoOn();
        indexer.runSpinny(50);

        Actions.runBlocking(
                new SequentialAction(
                        new SleepAction(12),
                        act2

                )
        );*/


    }
}