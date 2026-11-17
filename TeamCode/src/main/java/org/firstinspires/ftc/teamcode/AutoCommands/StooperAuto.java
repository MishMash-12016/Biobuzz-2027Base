package org.firstinspires.ftc.teamcode.AutoCommands;

import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class StooperAuto {

        private Servo stopper;
        public static double closedPos = 0.65;
        public static double openPos = 0.75;

        public void init(HardwareMap hardwareMap ){
            stopper = hardwareMap.get(Servo.class, "stopper");
        }

        public Action open (){
            stopper.setPosition(openPos);
            return new StooperAuto().open();
        }

        public Action close (){
            stopper.setPosition(closedPos);
            return new StooperAuto().close();
        }


}
