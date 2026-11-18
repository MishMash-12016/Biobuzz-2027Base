package org.firstinspires.ftc.teamcode.AutoActions;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;

public class UtilActions {


    public Action waitCommand(double Wait){
        new SleepAction(Wait);
        return new SleepAction(Wait);
    }

}
