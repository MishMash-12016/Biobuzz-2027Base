package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Transfer extends SubsystemBase {
    private DcMotor transfer;
    public void init(HardwareMap hardwareMap ){
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        transfer.setDirection(DcMotor.Direction.FORWARD);
    }

    public void TransferRunning(){
        transfer.setPower(1);
    }

    public void TransferStop(){
        transfer.setPower(0);
    }


}
