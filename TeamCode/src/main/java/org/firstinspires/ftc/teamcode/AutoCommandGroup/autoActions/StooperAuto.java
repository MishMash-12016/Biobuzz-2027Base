package org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions;

import android.app.Notification;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class StooperAuto {

        private Servo stopper;
        public static double closedPos = 0.55;
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
