package org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class TransferAuto {

        private DcMotor transfer;
        public void init(HardwareMap hardwareMap ){
            transfer = hardwareMap.get(DcMotor.class, "transfer");
            transfer.setDirection(DcMotor.Direction.FORWARD);
        }

        public Action TransferRunning() {
            transfer.setPower(1);
            return new TransferAuto().TransferRunning();
        }

        public Action TransferStop(){
            transfer.setPower(0);
            return new TransferAuto().TransferStop();
        }


    }

