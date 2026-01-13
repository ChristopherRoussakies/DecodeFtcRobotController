package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.subsystems.Indexer2;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@TeleOp
public class Borp extends OpMode {

    enum State{
        DRIVE,
        INTAKE,
        INTAKE_REVERSE,
        SORT,
        SORT_ONE,
        SORT_TWO,
        SORT_THREE,
        LOAD,
        FEED_ONE,
        FEED_TWO,
        FEED_THREE,
        SHOOT,
        DEBUG
    }
    MecanumDrive drive;
    Shooter shoot = new Shooter();
    Indexer2 index = new Indexer2();
    FtcDashboard dashboard = FtcDashboard.getInstance();
    State state = State.DRIVE;
    private ElapsedTime timer = new ElapsedTime();
    int[] colors = {0,0,0};

    int intakeSlot = 0;
    int shootSlot = 0;
    int lastFailsafeSlot = 1;

    boolean lastLBState;



    @Override
    public void init(){
        index.init(hardwareMap);
        drive = new MecanumDrive(hardwareMap, PoseStorage.currentPose);
        shoot.init(hardwareMap);
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

    }

    @Override
    public void loop(){
        switch (state){
            case DRIVE:
                index.intakeOff();
                index.secondIntakeStageOn();
                index.flapDown();
                shoot.setShooterSpeed(0);
                break;

            case INTAKE:
                index.intakeOn();
                index.secondIntakeStageOn();
                index.flapDown();
                index.updateIndexer();
                shoot.setShooterSpeed(0);
                if (gamepad1.left_bumper && !lastLBState){
                    if (intakeSlot==1){
                        intakeSlot=2;
                        index.indexerLoad2();
                    }
                    else if (intakeSlot==2){
                        intakeSlot=3;
                        index.indexerLoad3();
                    }
                    else if (intakeSlot==3){
                        intakeSlot=1;
                        index.indexerLoad1();
                    }
                }
                lastLBState=gamepad1.left_bumper;
                break;

            case INTAKE_REVERSE:
                index.intakeReverse();
                index.flapDown();
                shoot.setShooterSpeed(0);
                break;

            case SORT:
                index.secondIntakeStageOn();
                index.flapDown();
                break;

            case LOAD:
                index.intakeOff();
                index.secondIntakeStageOn();
                index.updateIndexer();
                index.flapDown();
                shoot.setShooterSpeed(2100);
                if (gamepad1.dpad_right){
                    shootSlot=index.nearestArtifact(colors);
                    if (shootSlot==0){
                        if (lastFailsafeSlot==1){
                            lastFailsafeSlot=2;
                            shootSlot=2;
                        }
                        else if (lastFailsafeSlot==2){
                            lastFailsafeSlot=3;
                            shootSlot=3;
                        }
                        else if (lastFailsafeSlot==3){
                            lastFailsafeSlot=1;
                            shootSlot=1;
                        }
                    }
                }
                else if (gamepad1.dpad_up){
                    shootSlot=index.nearestGreen(colors);
                }
                else if (gamepad1.dpad_down){
                    shootSlot=index.nearestPurple(colors);
                }

                if (shootSlot==1){
                    state = State.FEED_ONE;
                    index.indexerShoot1();
                    shootSlot=0;
                }
                else if (shootSlot==2){
                    state = State.FEED_TWO;
                    index.indexerShoot2();
                    shootSlot=0;
                }
                else if (shootSlot==3){
                    state = State.FEED_THREE;
                    index.indexerShoot3();
                    shootSlot=0;
                }
                break;

            case FEED_ONE:
                index.secondIntakeStageOn();
                //index.updateIndexer();
                if (Math.abs(index.updateIndexer())<10 && gamepad1.right_bumper){
                    index.flapUp();
                    state=State.SHOOT;
                    timer.reset();
                    colors[0]=0;
                }
                break;

            case FEED_TWO:
                index.secondIntakeStageOn();
                //index.updateIndexer();
                if (Math.abs(index.updateIndexer())<10 && gamepad1.right_bumper){
                    index.flapUp();
                    state=State.SHOOT;
                    timer.reset();
                    colors[1]=0;
                }
                break;

            case FEED_THREE:
                index.secondIntakeStageOn();
                //index.updateIndexer();
                if (Math.abs(index.updateIndexer())<10 && gamepad1.right_bumper){
                    index.flapUp();
                    state=State.SHOOT;
                    timer.reset();
                    colors[2]=0;
                }
                break;

            case SHOOT:
                index.secondIntakeStageOn();
                index.updateIndexer();
                if(index.getFlappyPose()>2){
                    index.flapDown();
                }
                if (index.getFlappyPose()<.95){
                    state=State.LOAD;
                }
                break;
            case DEBUG:
                if (gamepad2.right_bumper){
                    index.flapUp();
                }
                if (gamepad2.left_bumper){
                    index.flapDown();
                }
                if (gamepad2.a){
                    index.indexerLoad1();
                }
                if (gamepad2.b){
                    index.indexerLoad2();
                }
                if (gamepad2.x){
                    index.indexerLoad3();
                }
                if (gamepad2.dpad_left){
                    index.indexerShoot1();
                }
                if (gamepad2.dpad_up){
                    index.indexerShoot2();
                }
                if (gamepad2.dpad_right){
                    index.indexerShoot3();
                }
                index.updateIndexer();
                break;
        }
        if (gamepad1.dpad_left){
            state=State.LOAD;
        }
        else if (gamepad1.a){
            state=State.INTAKE;
            intakeSlot=1;
        }
        else if (gamepad1.b){
            state=State.INTAKE_REVERSE;
        } else if (gamepad1.x) {
            state=State.DRIVE;
        }
        if (gamepad2.touchpad_finger_2){
            state=State.DEBUG;
        }

        /*

*/

        colors = index.readIndexerColors(colors);

        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -gamepad1.right_stick_y -gamepad1.left_trigger +gamepad1.right_trigger, //switch left and right to switch joystick directions
                        -gamepad1.right_stick_x //right for fifi, left for charlie
                ),
                -gamepad1.left_stick_x
        ));
        drive.updatePoseEstimate();
        Pose2d pose = drive.localizer.getPose();

        telemetry.addData("Shooter Speed",shoot.getShooterSpeed());
        telemetry.addData("x", pose.position.x);
        telemetry.addData("y", pose.position.y);
        telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));

        telemetry.addData("Indexer Slot 1:", colors[0]);
        telemetry.addData("Indexer Slot 1:", colors[1]);
        telemetry.addData("Indexer Slot 1:", colors[2]);
        telemetry.addData("Indexer Position:",index.getIndexerPosition());
        telemetry.addData("Indexer Target", index.getTargetPosition());
        //telemetry.addData("Indexer Error", index.updateIndexer());
        telemetry.addData("Shooter Slot", shootSlot);
        telemetry.addData("Current State", state);
        telemetry.addData("Hamburger Flipper Position", index.getFlappyPose());
        telemetry.update();
    }
}
