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
public class RemoteControl extends OpMode {


    int indexerZero;

    int color1 = 0;
    int color2 = 0;
    int color3 = 0;

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
        if (gamepad1.a){
            shoot.feedServoOn();
            shoot.setShooterSpeed(2300); //3600 for long, 2300 for short, 5000 for shooting point by goals
            index.flapUp();

            // This updates the slot to empty when a slot is fired.
            if(0.167<indexerPose && indexerPose<0.5) {
                color1 = 0;
            } else if (0.5<indexerPose && indexerPose<0.833) {
                color2 = 0;
            } else if (0.833<indexerPose && indexerPose<1 ||0<indexerPose && indexerPose<0.167 ) {
                color3 = 0;
            }
        }

        if (gamepad1.b){
            shoot.feedServoOff();
            shoot.setShooterSpeed(0);
            index.flapDown();
        }


        if (gamepad1.right_bumper){
            index.runSpinny(179);
            //ticks per second
        }
        else {
            index.runSpinny(0);
        }

        if (!index.getMagnetState()){
            indexerZero = index.getSpinnyPose();
        }

        if (gamepad1.x){
            color1 = 0;
            color2 = 0;
            color3 = 0;
        }

        //indexerPose is where the magnet is relative to the magnet sensor in revolutions.
        // Slot one 1st after magnet
        // At color sensor: indexerPose = 0.333
        // At shooter: indexerPose = 0.5

        // Slot two 2st after magnet
        // At color sensor: indexerPose = 0.667
        // At shooter: indexerPose = 0.833

        // Slot three 3st after magnet
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
                if(0.4<indexerPose && indexerPose<0.6) {
                    index.runSpinny(50);
                    telemetry.addData("Shot Status","LOADING GREEN");
                }
                else{
                    telemetry.addData("Shot Status","GREEN READY");
                    gamepad1.rumble(500);
                }
            }
            else if (color2==1){
                if(0.733<indexerPose && indexerPose<0.933) {
                    index.runSpinny(50);
                    telemetry.addData("Shot Status","LOADING GREEN");
                }
                else{
                    telemetry.addData("Shot Status","GREEN READY");
                    gamepad1.rumble(500);
                }
            }
            else if (color3==1){
                if(0.067<indexerPose && indexerPose<0.267) {
                    index.runSpinny(50);
                    telemetry.addData("Shot Status","LOADING GREEN");
                }
                else{
                    telemetry.addData("Shot Status","GREEN READY");
                    gamepad1.rumble(500);
                }
            }
            else{
                    telemetry.addData("ERROR","NO GREEN");
                }
        }

        // Load Purple
        if (gamepad1.dpad_right){
            if (color1==2){
                if(0.4<indexerPose && indexerPose<0.6) {
                    index.runSpinny(50);
                    telemetry.addData("Shot Status","LOADING PURPLE");
                }
                else{
                    telemetry.addData("Shot Status","PURPLE READY");
                    gamepad1.rumble(500);
                }
            }
            else if (color2==2){
                if(0.733<indexerPose && indexerPose<0.933) {
                    index.runSpinny(50);
                    telemetry.addData("Shot Status","LOADING PURPLE");
                }
                else{
                    telemetry.addData("Shot Status","PURPLE READY");
                    gamepad1.rumble(500);
                }
            }
            else if (color3==2){
                if(0.067<indexerPose && indexerPose<0.267) {
                    index.runSpinny(50);
                    telemetry.addData("Shot Status","LOADING PURPLE");
                }
                else{
                    telemetry.addData("Shot Status","PURPLE READY");
                    gamepad1.rumble(500);
                }
            }
            else{
                telemetry.addData("ERROR","NO PURPLE");
            }
        }

        telemetry.addData("color1",color1);
        telemetry.addData("color2",color2);
        telemetry.addData("color3",color3);
        telemetry.addData("full", (index.full()));
        telemetry.addData("IndexPosition", indexerPose);

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
