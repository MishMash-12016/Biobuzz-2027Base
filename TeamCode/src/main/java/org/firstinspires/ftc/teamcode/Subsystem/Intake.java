package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private DcMotor intake;


    public void init(HardwareMap hardwareMap ){
        intake = hardwareMap.get(DcMotor.class, "intake");

        // TODO Change
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void IntakeRuning(){
        intake.setPower(1);
    }

    public void IntakeStop(){
        intake.setPower(0);
    }

}
