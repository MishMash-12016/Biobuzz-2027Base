package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intake;


    public void init(HardwareMap hardwareMap ){
        intake = hardwareMap.get(DcMotor.class, "intake");

        // TODO Change
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setIntake (double power){
        intake.setPower(power);
    }

}
