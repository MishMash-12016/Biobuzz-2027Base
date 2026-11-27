package org.firstinspires.ftc.teamcode.Opmode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vision.AprilTag;

@TeleOp
public class LLTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        AprilTag LL = new AprilTag();
        LL.init(hardwareMap);

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("isBlueAudience", LL.isBlueAudience());
            telemetry.addData("isRedAudience", LL.isRedAudience());
            telemetry.addData("isBlueStage", LL.isBlueStage());
            telemetry.addData("isRedStage", LL.isRedStage());
            telemetry.update();
        }
    }
}
