package org.firstinspires.ftc.teamcode.Actions;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import java.util.function.BooleanSupplier;

public class ConditionalAction implements Action {

    private final BooleanSupplier condition;
    private final Action trueAction;
    private final Action falseAction;

    public ConditionalAction(
            BooleanSupplier condition,
            Action trueAction,
            Action falseAction
    ) {
        this.condition = condition;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }

    @Override
    public boolean run(TelemetryPacket packet) {

        if (condition.getAsBoolean()) {
            return trueAction.run(packet);
        } else {
            return falseAction.run(packet);
        }
    }
}
