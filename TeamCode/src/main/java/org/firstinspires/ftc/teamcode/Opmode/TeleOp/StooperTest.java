package org.firstinspires.ftc.teamcode.Opmode.TeleOp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Stopper;

@TeleOp
@Config
public class StooperTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Stopper stooper = new Stopper();
        stooper.init(hardwareMap);


        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_trigger_pressed) {
                stooper.down();
            } else {
                stooper.up();
            }
        }
    }
}
