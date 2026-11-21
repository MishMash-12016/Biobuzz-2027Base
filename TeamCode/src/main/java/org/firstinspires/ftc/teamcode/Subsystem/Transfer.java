package org.firstinspires.ftc.teamcode.Subsystem;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Transfer extends SubsystemBase {
    private static DcMotor transfer;
    public void init(HardwareMap hardwareMap ){
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        transfer.setDirection(DcMotor.Direction.FORWARD);
    }

    public void TransferRunning() {
        transfer.setPower(1);
    }

    public static class RunTransfer implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            transfer.setPower(1);
            return false;
        }

        public static Action runTransfer() {
            return new RunTransfer();
        }
    }


    public void eject() {
        transfer.setPower(-1);
    }

    public static class TransferEject implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            transfer.setPower(-1);
            return false;
        }

        public static Action eject() {
            return new TransferEject();
        }
    }


    public void stop(){
        transfer.setPower(0);
    }

    public static class StopTransfer implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            transfer.setPower(0);
            return false;
        }

        public static Action stop() {
            return new StopTransfer();
        }
    }


}
