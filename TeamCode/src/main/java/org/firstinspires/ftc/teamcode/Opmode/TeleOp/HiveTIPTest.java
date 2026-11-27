package org.firstinspires.ftc.teamcode.Opmode.TeleOp;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vision.HiveAim;

@TeleOp
public class HiveTIPTest extends LinearOpMode {
    public void runOpMode() {
        Limelight3A limelight = hardwareMap.tryGet(Limelight3A.class, "limelight");
        HiveAim hiveAim = new HiveAim(limelight);
        hiveAim.init(hardwareMap);
        hiveAim.start();
        waitForStart();
        while (opModeIsActive()) {

            telemetry.addData("isTipBlueScoring",hiveAim.isTipBlueScoring());
            telemetry.addData("isTipBlueAudience",hiveAim.isTipBlueAudience());
            telemetry.addData("isTipRedScoring",hiveAim.isTipRedScoring());
            telemetry.addData("isTipRedAudience",hiveAim.isTipRedAudience());
            telemetry.update();
        }
    }
}