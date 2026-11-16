package org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;

public class IntakeAuto {
    private DcMotor intake;


    public void init(HardwareMap hardwareMap ){
        intake = hardwareMap.get(DcMotor.class, "intake");

        // TODO Change
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }



    public Command IntakeRun(){
        intake.setPower(1);
        return IntakeRun();
    }

    public Command IntakePause(){
        intake.setPower(0);
        return IntakePause();
    }

}
