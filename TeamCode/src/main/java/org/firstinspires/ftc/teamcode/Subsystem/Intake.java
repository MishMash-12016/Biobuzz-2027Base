package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private static DcMotor intake;

    public Intake() {}

    public Intake(HardwareMap hardwareMap) {
        init(hardwareMap);
    }

    public void init(HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public static void collect() {
        intake.setPower(1);
    }

    public static void eject() {
        intake.setPower(-1);
    }

    public static void stop() {
        intake.setPower(0);
    }

    // ==================== Actions (Road Runner) ====================

    public class CollectAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            collect();
            packet.put("intakePower", 1.0);
            return false;
        }
    }

    public class EjectAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            eject();
            packet.put("intakePower", -1.0);
            return false;
        }
    }

    public class StopAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            stop();
            packet.put("intakePower", 0.0);
            return false;
        }
    }

    public Action collectAction() {
        return new CollectAction();
    }

    public Action ejectAction() {
        return new EjectAction();
    }

    public Action stopAction() {
        return new StopAction();
    }
}
