package org.firstinspires.ftc.teamcode.AutoCommands;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class TransferAuto {

    private DcMotor transfer;

    public void init(HardwareMap hardwareMap) {
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        transfer.setDirection(DcMotor.Direction.FORWARD);
    }

    public Action TransferRunning() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (transfer != null) {
                    transfer.setPower(1.0);
                }

                return false;
            }
        };
    }

    public Action TransferStop() {
        return new InstantAction(() -> {
            if (transfer != null) {
                transfer.setPower(0.0);
            }
        });


    }
}

