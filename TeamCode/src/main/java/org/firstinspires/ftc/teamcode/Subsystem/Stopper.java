package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
@Config
public class Stopper extends SubsystemBase {
    private static Servo servo;
    public static double down = 0.65;
    public static double up = 0.8;

    public void init(HardwareMap hardwareMap ){
        servo = hardwareMap.get(Servo.class, "stopper");
    }

    public void up (){
        servo.setPosition(up);
    }

    public static class Up implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            servo.setPosition(up);
            return false;
        }
            public Action up(){
            return new Up();
        }
    }

    public void down(){
        servo.setPosition(down);
    }

    public static class Down implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            servo.setPosition(down);
            return false;
        }
        public Action down(){
            return new Down();
        }
    }



}
