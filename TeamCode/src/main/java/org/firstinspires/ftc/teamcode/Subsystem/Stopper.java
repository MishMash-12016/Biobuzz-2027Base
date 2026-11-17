package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
@Config
public class Stopper extends SubsystemBase {
    private Servo servo;
    public static double down = 0.65;
    public static double up = 0.8;

    public void init(HardwareMap hardwareMap ){
        servo = hardwareMap.get(Servo.class, "stopper");
    }

    public void up (){
        servo.setPosition(up);
    }

    public void down(){
        servo.setPosition(down);
    }

}
