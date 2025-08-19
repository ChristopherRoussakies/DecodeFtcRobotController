package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TestServo {
    private Servo testservo;
    public void init(HardwareMap hwMap){
        testservo = hwMap.get(Servo.class, "testServo");
    }

    public void setPosition(double position){
        testservo.setPosition(position);
    }

    public class TestAction implements Action {
        double position;

        public TestAction(double p){
            this.position = p;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket){
            testservo.setPosition(position);
            return false;
        }
    }

    public Action testAction(double position){
        return new TestAction(position);
    }
}
