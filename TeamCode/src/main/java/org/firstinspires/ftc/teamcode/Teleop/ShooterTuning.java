package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Subsystem.Transfer;

@TeleOp
@Config
public class ShooterTuning extends LinearOpMode {
    @Override
    public void runOpMode()  {

        Shooter shooter = new Shooter();
        Intake intake = new Intake();
        Transfer transfer = new Transfer();

        shooter.init(hardwareMap);
        intake.init(hardwareMap);
        transfer.init(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry , FtcDashboard.getInstance().getTelemetry());
         waitForStart();

        while (opModeIsActive()){

            if (gamepad1.dpad_up){
                shooter.spin();
            }

            if (gamepad1.dpad_down){
                shooter.stop();
            }

            if(gamepad1.right_trigger_pressed){
                intake.IntakeRuning();
                transfer.TransferRunning();
            }else{
                intake.IntakeStop();
                transfer.TransferStop();
            }

        }
    }
}
