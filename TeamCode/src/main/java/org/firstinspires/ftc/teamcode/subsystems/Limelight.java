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
    public void init() {
        initLimelight(hardwareMap);
    }

    @Override
    public void loop() {
        result = limelight.getLatestResult();
        telemetry.addData("Y", result.getTy());
        telemetry.addData("X", result.getTx());
        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fr : fiducialResults) {
            telemetry.addData("Fiducial", "ID: %d,", fr.getFiducialId());
        }
    }

    public void initLimelight(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        limelight.start();
    }
}

 /*       public int limelightId(){
    result = limelight.getLatestResult();
    int atNumber
    telemetry.addData("Y",result.getTy());
    telemetry.addData("X",result.getTx());
    List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
    for (LLResultTypes.FiducialResult fr : fiducialResults) {
        //telemetry.addData("Fiducial", "ID: %d,", fr.getFiducialId());
        atNumber = fr.getFiducialId();
    }
}


    }
}*/
