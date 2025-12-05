package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Indexer;

import com.acmerobotics.dashboard.FtcDashboard;

@TeleOp
public class RemoteControlCharlie extends OpMode {

    boolean intakeOn = false;
    boolean shooterOn = false;
    boolean indexerOn = false;
    boolean indexerAuto = false;
    boolean lastXState;
    boolean lastAState;
    boolean lastYState;
    int indexerZero;

    int color1 = 0;
    int color2 = 0;
    int color3 = 0;

    int[] colors = {0,0,0};

    double indexerPose;
    MecanumDrive drive;
    Shooter shoot = new Shooter();
    Indexer index = new Indexer();

    FtcDashboard dashboard = FtcDashboard.getInstance();

    @Override
    public void init(){
        drive = new MecanumDrive(hardwareMap, PoseStorage.currentPose);
        shoot.init(hardwareMap);
        index.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    @Override
    public void loop(){

        index.secondStageOn();

        if (gamepad1.dpad_up){
            index.flapUp();
        }
        else {
            index.flapDown();
        }

        if (gamepad1.a && !lastAState) {
            if (shooterOn) {
                shoot.setShooterSpeed(0);
                shooterOn = false;
            } else {
                shoot.setShooterSpeed(2300);
                shooterOn = true;
            }
        }
        lastAState = gamepad1.a;


        if (gamepad1.left_bumper){
            index.runSpinny(-179);
            indexerAuto= false;
        }
        else if (gamepad1.right_bumper){
            index.runSpinny(179);
            indexerAuto = false;
        }
        else if (!indexerAuto) {
            index.runSpinny(0);
        }


        if (gamepad1.y && !lastYState){
            if (intakeOn){
                index.intakeOff();
                intakeOn = false;
            }
            else {
                index.intakeOn();
                intakeOn = true;
            }
        }
        lastYState = gamepad1.y;


        if (!index.getMagnetState()){
            indexerZero = index.getSpinnyPose();
        }

        if (gamepad2.a){
            colors[0] = 0;
            colors[1] = 0;
            colors[2] = 0;
        }



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
        if (gamepad1.x && !lastXState){
            if (indexerOn){
                index.runSpinny(0);
                indexerOn = false;
            }
            else {
                index.runSpinny(179);
                indexerOn = true;
            }
        }
        lastXState = gamepad1.x;

        indexerPose = (((index.getSpinnyPose()-indexerZero)/537.7) % 1);
        index.readIndexer(0, colors);
        // Load Green
        // Checks each slot for a green
        // Rotates to that slot then vibrates controller
        if (gamepad1.dpad_left){
            indexerAuto = true;
            if (colors[0]==1){
                index.runSpinnyToPose(1, indexerZero);
            }
            else if (colors[1]==1){
                index.runSpinnyToPose(2, indexerZero);
            }
            else if (colors[2]==1){
                index.runSpinnyToPose(3, indexerZero);
            }
            else{
                telemetry.addData("ERROR","NO GREEN");
            }
        }

        // Load Purple
        if (gamepad1.dpad_right){
            indexerAuto = true;
            if (colors[0]==2){
                index.runSpinnyToPose(1, indexerZero);
            }
            else if (colors[1]==2){
                index.runSpinnyToPose(2, indexerZero);
            }
            else if (colors[2]==2){
                index.runSpinnyToPose(3, indexerZero);
            }
            else{
                telemetry.addData("ERROR","NO PURPLE");
            }
        }

        telemetry.addData("color1",colors[0]);
        telemetry.addData("color2",colors[1]);
        telemetry.addData("color3",colors[2]);
        telemetry.addData("full", (index.full()));
        telemetry.addData("IndexPosition", indexerPose);

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.left_stick_y, //switch left and right to switch joystick directions
                        -gamepad1.left_stick_x //right for fifi, left for charlie
                ),
                -gamepad1.right_stick_x
        ));

        drive.updatePoseEstimate();

        Pose2d pose = drive.localizer.getPose();
        telemetry.addData("Shooter Speed",shoot.getShooterSpeed());

        telemetry.addData("Spinny Speed",index.getSpinnySpeed());
        telemetry.addData("Green",index.getGreen());
        telemetry.addData("Red",index.getRed());
        telemetry.addData("Blue",index.getBlue());
        telemetry.addData("Magnet", index.getMagnetState());

        telemetry.addData("x", pose.position.x);
        telemetry.addData("y", pose.position.y);
        telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
        telemetry.update();
    }

}
