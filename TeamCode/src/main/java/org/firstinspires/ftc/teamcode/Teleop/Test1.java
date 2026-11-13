package org.firstinspires.ftc.teamcode.Teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class Test1 extends LinearOpMode {


    public static double Test = 0;

    private DcMotorEx Intake;

    @Override
    public void runOpMode() throws InterruptedException {
        Intake = hardwareMap.get(DcMotorEx.class, "intake");

        telemetry = new MultipleTelemetry(telemetry , FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while (opModeIsActive()){

            Intake.setPower(Test);

            telemetry.addData("Power" , Intake.getPower());
            telemetry.addData("target Speed" , Test);
            telemetry.update();
        }

    }
}
