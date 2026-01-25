package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class Indexer2 {

    private DcMotorEx Spinny;
    private DcMotorEx Intake;
    private Servo Flappy;
    private Servo Flappy2;
    private CRServo Intake2;
    private RevColorSensorV3 Lighty;
    private RevColorSensorV3 Lighty2;
    private RevColorSensorV3 Lighty3;
    private OctoQuad Octoquad;
    private final OctoQuad.EncoderDataBlock block = new OctoQuad.EncoderDataBlock();
    int nearestGreen;
    int nearestPurple;

    public static double kp = 0.001;
    public static double ki = 0.0015;
    public static double kd = 0.0001;

    private int targetPosition = 1;
    private int indexerOffset = 31;
    private double integral = 0;
    private double lastError = 0;
    public static double maxIntegral = 100;
    public AnalogInput FlappyFeedback;
    private ElapsedTime timer = new ElapsedTime();



    public void init(HardwareMap hwMap){
        Spinny = hwMap.get(DcMotorEx.class, "indexer");
        Spinny.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        Intake = hwMap.get(DcMotorEx.class, "intake");
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Flappy = hwMap.get(Servo.class, "indexerServo");
        Flappy2 = hwMap.get(Servo.class, "indexerServo2");
        FlappyFeedback = hwMap.get(AnalogInput.class,"indexerServoSensor");

        Intake2 = hwMap.get(CRServo.class, "intakeServo");

        Lighty = hwMap.get(RevColorSensorV3.class, "colorSensor");
        Lighty2 = hwMap.get(RevColorSensorV3.class, "colorSensor2");
        Lighty3= hwMap.get(RevColorSensorV3.class, "colorSensor3");

        Octoquad = hwMap.get(OctoQuad.class, "Octoquad");
        Octoquad.setChannelBankConfig(OctoQuad.ChannelBankConfig.ALL_PULSE_WIDTH);
        Octoquad.setSingleChannelPulseWidthParams(0,1,1024);

        timer.reset();
    }
    public void initIndexerMode2(HardwareMap hwMap){
        Spinny = hwMap.get(DcMotorEx.class, "indexer");
        Spinny.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Intake = hwMap.get(DcMotorEx.class, "intake");
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Flappy = hwMap.get(Servo.class, "indexerServo");
        Flappy2 = hwMap.get(Servo.class, "indexerServo2");
        FlappyFeedback = hwMap.get(AnalogInput.class,"indexerServoSensor");

        Intake2 = hwMap.get(CRServo.class, "intakeServo");

        Lighty = hwMap.get(RevColorSensorV3.class, "colorSensor");
        Lighty2 = hwMap.get(RevColorSensorV3.class, "colorSensor2");
        Lighty3= hwMap.get(RevColorSensorV3.class, "colorSensor3");

        Octoquad = hwMap.get(OctoQuad.class, "Octoquad");
        Octoquad.setChannelBankConfig(OctoQuad.ChannelBankConfig.ALL_PULSE_WIDTH);
        Octoquad.setSingleChannelPulseWidthParams(0,1,1024);

        timer.reset();
    }
    public void runIndexerMode2(){
        double distance = targetPosition-getIndexerPosition();
        int convDistance;
        if (distance>512){
            convDistance = (int)Math.round(537.7*(distance - 1024)/1024);
        } else if (distance<-512) {
            convDistance = (int)Math.round(537.7*(distance + 1024)/1024);
        } else{
            convDistance = (int)Math.round(537.7*(distance)/1024);
        }
        Spinny.setTargetPosition(convDistance + Spinny.getCurrentPosition());
        Spinny.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Spinny.setPower(.25); //increase and decrease to see which feels most snap to position-y
    }
    public int checkIndexerErrorMode2(){
        return Math.abs(Spinny.getTargetPosition()- Spinny.getCurrentPosition());
    }

    public double checkIndexerVelocityMode2(){
        return Math.abs(Spinny.getVelocity());
    }
    public void indexerShoot1Mode2(){
        targetPosition = 1+indexerOffset;
    }
    public void indexerShoot2Mode2(){
        targetPosition = 344+indexerOffset;
    }
    public void indexerShoot3Mode2(){
        targetPosition = 687+indexerOffset;
    }
    public void  indexerLoad1Mode2(){
        targetPosition = 516+indexerOffset;
    }
    public void indexerLoad2Mode2(){
        targetPosition = 859+indexerOffset;
    }
    public void indexerLoad3Mode2(){
        targetPosition = 173+indexerOffset;
    }

    // HAMBURGER INDEXER
    public int getIndexerPosition(){
        Octoquad.readAllEncoderData(block);
        return block.positions[0];
    }

    public void setTarget(int target){
        targetPosition = target;
        integral = 0;
    }
    public void indexerShoot1(){
        targetPosition = 1+indexerOffset;
        integral = 0;
    }
    public void indexerShoot2(){
        targetPosition = 344+indexerOffset;
        integral = 0;
    }
    public void indexerShoot3(){
        targetPosition = 687+indexerOffset;
        integral = 0;
    }
    public void  indexerLoad1(){
        targetPosition = 516+indexerOffset;
        integral = 0;
    }
    public void indexerLoad2(){
        targetPosition = 859+indexerOffset;
        integral = 0;
    }
    public void indexerLoad3(){
        targetPosition = 173+indexerOffset;
        integral = 0;
    }
    public void indexerRead1(){
        targetPosition = 859+indexerOffset;
        integral = 0;
    }
    public void indexerRead2(){
        targetPosition = 173+indexerOffset;
        integral = 0;
    }
    public void indexerRead3(){
        targetPosition = 516+indexerOffset;
        integral = 0;
    }

    public double updateIndexer(){
        Octoquad.readAllEncoderData(block);
        double currentPosition = block.positions[0];
        double error = targetPosition-currentPosition;
        if (block.positions[0]<1 || block.positions[0]>1030){
            Spinny.setPower(0);
            return lastError;
        }
        double dt = timer.seconds();
        if (dt==0){
            dt = 0.001;
        }
        timer.reset();
        if (error>512){
            error = error - 1024;
        } else if (error<-512) {
            error = error + 1024;
        }
        integral += error*dt;
        if (Math.abs(integral)> maxIntegral){
            integral = maxIntegral;
        }
        double derivative = (error - lastError)/(dt);
        double power = kp*error + ki*integral + kd*derivative;
        power = Math.max(-1, Math.min(1, power));
        Spinny.setPower(power);
        lastError=error;
        return error;
    }

    public boolean updateIndexerAuto(){
        Octoquad.readAllEncoderData(block);
        double currentPosition = block.positions[0];
        double error = targetPosition-currentPosition;
        if (block.positions[0]<1 || block.positions[0]>1030){
            Spinny.setPower(0);
            return true;
        }
        double dt = timer.seconds();
        if (dt==0){
            dt = 0.001;
        }
        timer.reset();
        if (error>512){
            error = error - 1024;
        } else if (error<-512) {
            error = error + 1024;
        }
        integral += error*dt;
        if (Math.abs(integral)> maxIntegral){
            integral = maxIntegral;
        }
        double derivative = (error - lastError)/(dt);
        double power = kp*error + ki*integral + kd*derivative;
        power = Math.max(-1, Math.min(1, power));
        Spinny.setPower(power);
        boolean runAgain = true;
        if(Math.abs(lastError)<8 && Math.abs(error)<8){
            runAgain = false;
        }
        lastError=error;
        return runAgain;
    }

    public int getTargetPosition(){
        return targetPosition;
    }

    public int readColor(){
        if (Lighty.green()+Lighty.blue()>2000){
            if (Lighty.green()>Lighty.blue()){
                return 1;
                //green
            }
            else{
                return 2;
                //purple
            }
        }
        else {
            return 0;
            //empty
        }
    }
    public int readColor2(){
        if (Lighty2.green()+Lighty2.blue()>2000){
            if (Lighty2.green()>Lighty2.blue()){
                return 1;
                //green
            }
            else{
                return 2;
                //purple
            }
        }
        else {
            return 0;
            //empty
        }
    }
    public int indexerColors(){
        int sensor1 = readColor();
        int sensor2 = readColor2();
        if ( sensor1 != 0 || sensor2 != 0 ){
            if ((sensor1 == 1 && sensor2 ==2) || (sensor1 == 2 && sensor2 ==1)){
                return 0;

            }
            else if(sensor1==1 || sensor2==1){
                return 1;
                //green
            }
            else{
                return 2;
                //purple
            }
        }
        else {
            return 0;
            //empty
        }
    }

    public int updateIndexerColors(int color){
        int sensor1 = readColor();
        int sensor2 = readColor2();
        if ( sensor1 != 0 || sensor2 != 0 ){
            if ((sensor1 == 1 && sensor2 ==2) || (sensor1 == 2 && sensor2 ==1)){
                return color;

            }
            else if(sensor1==1 || sensor2==1){
                return 1;
                //green
            }
            else{
                return 2;
                //purple
            }
        }
        else {
            return color;
            //empty
        }
    }

    public int[] readIndexerColors( int[] colors) {
        int indexerPose=getIndexerPosition();
        if (773-indexerOffset < indexerPose && indexerPose < 945-indexerOffset) {
            if (colors[0] == 0) {
                colors[0] = indexerColors();
            } else {
                colors[0] = updateIndexerColors(colors[0]);
            }
        } else if (87-indexerOffset < indexerPose && indexerPose < 259-indexerOffset) {
            if (colors[1] == 0) {
                colors[1] = indexerColors();
            } else {
                colors[1] = updateIndexerColors(colors[1]);
            }
            ;
        } else if (430-indexerOffset < indexerPose && indexerPose < 602-indexerOffset) {
            if (colors[2] == 0) {
                colors[2] = indexerColors();
            } else {
                colors[2] = updateIndexerColors(colors[2]);
            }
        }
        return colors;
    }

    public void flapUp (){
        Flappy.setPosition(.25);
        Flappy2.setPosition(1);
    }
    public void flapDown() {
        Flappy.setPosition(.99);
        Flappy2.setPosition(.03);
    }
    public double getFlappyPose(){
        return FlappyFeedback.getVoltage();
    }
    public int nearestArtifact(int[] colors) {
        int indexerPose = getIndexerPosition();
        int slot1Distance = Math.abs(516+indexerOffset-indexerPose);
        int slot2Distance = Math.abs(859+indexerOffset-indexerPose);
        int slot3Distance = Math.abs(173+indexerOffset-indexerPose);
        if (slot1Distance<slot2Distance && slot1Distance<slot3Distance){
            if (colors[0]!=0){
                return 1;
            }
        }
        if (slot2Distance<slot3Distance){
            if (colors[1]!=0){
                return 2;
            }
        }

        if (colors[2]!=0){
            return 3;
        }
        else if (colors[1]!=0){
            return 2;
        }
        else if (colors[0]!=0){
            return 1;
        }

        return 0;

    }
    public int nearestGreen(int[] colors) {
        int indexerPose = getIndexerPosition();
        int slot1Distance = Math.abs(516+indexerOffset-indexerPose);
        int slot2Distance = Math.abs(859+indexerOffset-indexerPose);
        int slot3Distance = Math.abs(173+indexerOffset-indexerPose);
        if (slot1Distance<slot2Distance && slot1Distance<slot3Distance){
            if (colors[0]==1){
                return 1;
            }
        }
        if (slot2Distance<slot3Distance){
            if (colors[1]==1){
                return 2;
            }
        }

        if (colors[2]==1){
            return 3;
        }
        else if (colors[1]==1){
            return 2;
        }
        else if (colors[0]==1){
            return 1;
        }

        return 0;

    }
    public int nearestPurple(int[] colors) {
        int indexerPose = getIndexerPosition();
        int slot1Distance = Math.abs(516+indexerOffset-indexerPose);
        int slot2Distance = Math.abs(859+indexerOffset-indexerPose);
        int slot3Distance = Math.abs(173+indexerOffset-indexerPose);
        if (slot1Distance<slot2Distance && slot1Distance<slot3Distance){
            if (colors[0]==2){
                return 1;
            }
        }
        if (slot2Distance<slot3Distance){
            if (colors[1]==2){
                return 2;
            }
        }

        if (colors[2]==2){
            return 3;
        }
        else if (colors[1]==2){
            return 2;
        }
        else if (colors[0]==2){
            return 1;
        }

        return 0;

    }

    // HAMBURGER INTAKE
    public void intakeOn(){
        Intake.setVelocity(1000);
        Intake2.setPower(-1);
    }
    public  void intakeReverse (){
        Intake.setVelocity(-750);
        Intake2.setPower(1);
    }
    public void secondIntakeStageOn(){
        Intake2.setPower(-1);
    }
    public void secondIntakeStageOff(){
        Intake2.setPower(0);
    }
    public void intakeOff(){
        Intake.setVelocity(0);
        //Intake2.setPower(0);

    }
}
