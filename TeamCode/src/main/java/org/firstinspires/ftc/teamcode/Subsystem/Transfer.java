package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Transfer extends SubsystemBase {
    private static DcMotor transfer;

    public Transfer() {}

    public Transfer(HardwareMap hardwareMap) {
        init(hardwareMap);
    }

    public void init(HardwareMap hardwareMap) {
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        transfer.setDirection(DcMotor.Direction.FORWARD);
    }

    public void TransferRunning() {
        transfer.setPower(1);
    }

    public void eject() {
        transfer.setPower(-1);
    }

    public void stop() {
        transfer.setPower(0);
    }

    // ==================== Actions (Road Runner) ====================

    public class TransferRunningAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            TransferRunning();
            packet.put("transferPower", 1.0);
            return false;
        }
    }

    public class EjectAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            eject();
            packet.put("transferPower", -1.0);
            return false;
        }
    }

    public class StopAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            stop();
            packet.put("transferPower", 0.0);
            return false;
        }
    }

    public Action transferRunningAction() {
        return new TransferRunningAction();
    }

    public Action runTransfer() {
        return new TransferRunningAction();
    }

    public Action ejectAction() {
        return new EjectAction();
    }

    public Action stopAction() {
        return new StopAction();
    }
}
