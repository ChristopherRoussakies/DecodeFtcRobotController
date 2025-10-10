package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;
@TeleOp

public class Limelight extends OpMode {
    private Limelight3A limelight;

    LLResult result;

    @Override
    public void init(){
        initLimelight(hardwareMap);
    }

    @Override
    public void loop(){
        result = limelight.getLatestResult();
        telemetry.addData("Y",result.getTy());
        telemetry.addData("X",result.getTx());
    }
    public void initLimelight(HardwareMap hwMap){
        limelight = hwMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }
}
