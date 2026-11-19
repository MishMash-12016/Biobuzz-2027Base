package org.firstinspires.ftc.teamcode.AutoActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Subsystem.Shooter;

public class ShooterAction {

    private static Shooter shooter = new Shooter();

    public static class PIDF implements Action{
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.applyPIDF(shooter.targetVel);
            return true;
        }

        public static Action pidf() {
            return new PIDF();
        }
    }



    public static class DisableShooter implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.spin(0);
            return false;
        }

        public static Action Disable() {
            return new DisableShooter();
        }
    }

    public static class RunShooter implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.spin(1650);
            return false;
        }

        public static Action Run() {
            return new RunShooter();
        }
    }
}
