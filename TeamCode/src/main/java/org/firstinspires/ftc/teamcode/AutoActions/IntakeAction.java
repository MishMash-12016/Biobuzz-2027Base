package org.firstinspires.ftc.teamcode.AutoActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;


public class IntakeAction extends SubsystemBase {
    private DcMotor intake;

    public void init(HardwareMap hardwareMap ){
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public Action IntakeRun() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (intake != null) {
                    intake.setPower(1.0);
                }
                return false;
            }
        };
    }

    public Action IntakePause() {
        return new InstantAction(() -> {
            if (intake != null) {
                intake.setPower(0.0);
            }
        });
    }
}
