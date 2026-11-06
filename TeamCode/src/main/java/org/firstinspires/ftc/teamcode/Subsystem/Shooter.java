package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {
    private DcMotor ShooterL;
    private DcMotor ShooterR;


    public void init(HardwareMap hardwareMap ) {
        ShooterL = hardwareMap.get(DcMotor.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotor.class, "ShooterR");

        ShooterL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ShooterR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // TODO Change
        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setShooter(double Power){
        ShooterL.setPower(Power);
        ShooterR.setPower(Power);
    }
}
