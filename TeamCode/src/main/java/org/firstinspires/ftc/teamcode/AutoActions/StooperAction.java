package org.firstinspires.ftc.teamcode.AutoActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class StooperAction {
        private Servo stopper;
        public static double downPose = 0.65;
        public static double upPose = 0.75;

        public void init(HardwareMap hardwareMap ){
            stopper = hardwareMap.get(Servo.class, "stopper");
        }

        public Action up (){
            return new Action() {
                @Override
                public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                    if (stopper != null) {
                        stopper.setPosition(upPose);
                    }
                    return false;
                }
            };
        }


    public Action Down (){
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (stopper != null) {
                    stopper.setPosition(downPose);
                }
                return false;
            }
        };
    }

    public void setPosition(double position) {
        if (stopper != null) {
            stopper.setPosition(position);
        }
    }


}
