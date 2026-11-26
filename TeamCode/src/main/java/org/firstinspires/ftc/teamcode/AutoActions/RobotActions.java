package org.firstinspires.ftc.teamcode.AutoActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

import org.firstinspires.ftc.teamcode.RobotSystem;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Stopper;
import org.firstinspires.ftc.teamcode.Subsystem.Transfer;

public class RobotActions {

    RobotSystem robotSystem = new RobotSystem();

    public static class InakeAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            new SequentialAction(
//                    Stopper.Up.up(),
                    new SleepAction(3),
                    Intake.Collect.collect(),
                    Transfer.RunTransfer.runTransfer()
            );
            return true;
        }

        public static Action intakeAction() {
            return new InakeAction();
        }
    }

    public static class ScoringAction implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            new SequentialAction(
//                    Stopper.Down.down(),
                    new SleepAction(3),
                    Intake.Collect.collect(),
                    Transfer.RunTransfer.runTransfer()
            );
            return false;
        }
        public static Action scoringAction() {
            return new ScoringAction();
        }
    }
}
