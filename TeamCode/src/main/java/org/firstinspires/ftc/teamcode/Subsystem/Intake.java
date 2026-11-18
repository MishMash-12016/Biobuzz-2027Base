package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private DcMotorEx intake;

    public void init(HardwareMap hardwareMap ){
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public Command collect() {
        return new InstantCommand(() -> intake.setPower(1), this);
    }

    // פקודה לפליטה (הפוך)
    public Command eject() {
        return new InstantCommand(() -> intake.setPower(-1), this);
    }

    // פקודה לעצירה
    public Command stopIntake() {
        return new InstantCommand(() -> intake.setPower(0), this);
    }

}
