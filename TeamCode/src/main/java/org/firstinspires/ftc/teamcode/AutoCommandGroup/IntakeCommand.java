package org.firstinspires.ftc.teamcode.AutoCommandGroup;


import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions.IntakeAuto;


public class IntakeCommand {

    IntakeAuto intake = new IntakeAuto();


    public Action IntakeAction() {
        new SequentialAction(
                intake.IntakeRuning(),

                intake.IntakeStop()
        );
        return new SequentialAction(IntakeAction());
    }
}