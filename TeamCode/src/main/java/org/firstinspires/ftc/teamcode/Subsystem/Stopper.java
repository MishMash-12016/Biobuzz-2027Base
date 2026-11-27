package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class Stopper {
    private static Servo servo;
    private FtcDashboard Dashboard;

    public static double down = 0.6;
    public static double up = 0.8;

    public Stopper() {}

    public Stopper(HardwareMap hardwareMap) {
        init(hardwareMap);
    }

    public void init(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "stopper");
        servo.setDirection(Servo.Direction.FORWARD);
        servo.scaleRange(0.0, 1.0);
    }

    public void up() {
        servo.setPosition(up);
    }

    public void down() {
        servo.setPosition(down);
    }

    /** Sends servo position telemetry to FTC Dashboard. */
    public void printServoPosition() {
        if (servo == null) {
            return;
        }
        TelemetryPacket servoPacket = new TelemetryPacket();
        servoPacket.put("Servo position", servo.getPosition());
        FtcDashboard.getInstance().sendTelemetryPacket(servoPacket);
    }

    // ==================== Actions (Road Runner) ====================

    public class UpAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            up();
            packet.put("stopperPosition", servo != null ? servo.getPosition() : up);
            return false;
        }
    }

    public class DownAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            down();
            packet.put("stopperPosition", servo != null ? servo.getPosition() : down);
            return false;
        }
    }

    public Action upAction() {
        return new UpAction();
    }

    public Action downAction() {
        return new DownAction();
    }
}
