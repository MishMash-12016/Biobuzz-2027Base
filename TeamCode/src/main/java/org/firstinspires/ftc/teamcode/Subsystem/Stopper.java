package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
@Config
public class Stopper extends SubsystemBase {
    private Servo stopper;
    public static double closedPos = 0.55;
    public static double openPos = 0.75;

    public void init(HardwareMap hardwareMap ){
        stopper = hardwareMap.get(Servo.class, "stopper");
    }

    public void open (){
        stopper.setPosition(openPos);
    }

    public void close (){
        stopper.setPosition(closedPos);
    }

}
