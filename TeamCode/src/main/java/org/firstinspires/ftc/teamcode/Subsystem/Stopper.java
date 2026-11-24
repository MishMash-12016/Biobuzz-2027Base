package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Stopper  {
    private static Servo servo;
    private FtcDashboard Dashboard;

    public static double down = 1;
    public static double up = 0.79;

    public void init(HardwareMap hardwareMap ){
        servo = hardwareMap.get(Servo.class, "stopper");
        servo.setDirection(Servo.Direction.REVERSE);
        servo.scaleRange(0.0, 1.0);
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
            public static Action up(){
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
        public static Action down(){
            return new Down();
        }
    }

    public void PosePacket(){
        TelemetryPacket posepacket = new TelemetryPacket();
        posepacket.put("Servo Position" , Stopper.servo.getPosition());
        posepacket.put("Servo Current" , Stopper.servo.getDirection());

        Dashboard.sendTelemetryPacket(posepacket);
        Dashboard.updateConfig();
    }






}
