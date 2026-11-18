package org.firstinspires.ftc.teamcode.Opmode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Subsystem.Stopper;
import org.firstinspires.ftc.teamcode.Subsystem.Transfer;

@TeleOp
@Config
public class ShooterTuning extends LinearOpMode {
    @Override
    public void runOpMode()  {

        Shooter shooter = new Shooter();
        Intake intake = new Intake();
        Transfer transfer = new Transfer();
        Stopper stooper = new Stopper();

        shooter.init(hardwareMap);
        intake.init(hardwareMap);
        transfer.init(hardwareMap);
        stooper.init(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry , FtcDashboard.getInstance().getTelemetry());
         waitForStart();

        while (opModeIsActive()){
            //shooter.spin();



            if(gamepad1.right_trigger_pressed){
                intake.collect();
                transfer.TransferRunning();
            }else{
                intake.stop();
                transfer.stop();
            }

            if(gamepad2.right_trigger_pressed){
                stooper.down();

            } else {
                stooper.up();
            }

            shooter.printVelocity();

        }
    }
}
