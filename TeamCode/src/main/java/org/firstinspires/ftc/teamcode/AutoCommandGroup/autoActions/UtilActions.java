package org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions;

import android.app.Notification;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;

public class UtilActions {

    public Action waitCommand(double Wait){
        new SleepAction(Wait);
        return new SleepAction(Wait);
    }
}
