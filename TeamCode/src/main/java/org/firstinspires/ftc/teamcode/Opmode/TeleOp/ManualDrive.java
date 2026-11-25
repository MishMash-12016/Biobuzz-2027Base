package org.firstinspires.ftc.teamcode.Opmode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Subsystem.Stopper;
import org.firstinspires.ftc.teamcode.Subsystem.Transfer;

@TeleOp
@Config
public class ManualDrive extends LinearOpMode {
    @Override
    public void runOpMode() {

        // region CREATING SYSTEMS
        DriveTrain dt = new DriveTrain();
        Intake intake = new Intake();
        Shooter shooter = new Shooter();
        Transfer transfer = new Transfer();
        Stopper stopper = new Stopper();
        // endregion

        // region INITILAIZE SYSTEMS
        dt.init(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
        transfer.init(hardwareMap);
        stopper.init(hardwareMap);
        // endregion

        telemetry = new MultipleTelemetry(telemetry , FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while (opModeIsActive()) {
            // Drive Train
            dt.fieldOrientedDrive(gamepad1);
            if (gamepad1.options) {
                dt.resetHeading();
            }

            // Intake + Transfer
            if (gamepad1.right_bumper) {
                Intake.collect();
                transfer.TransferRunning();
            } else if (gamepad1.left_bumper){
                intake.eject();
                transfer.eject();
            } else {
                intake.stop();
                transfer.stop();
            }



            // Shooter
            shooter.spin(shooter.targetVel);
            if (gamepad1.right_trigger_pressed ){
                shooter.targetVel = 1165;}
            if (gamepad1.left_trigger_pressed){
                shooter.targetVel = 0;

            }

            // Stopper
            if (gamepad1.dpad_up) {
                stopper.up();
            }
            if (gamepad1.dpad_down) {
                stopper.down();
            }

            shooter.printVelocity();

            telemetry.addData("X",gamepad1.left_stick_x);
            telemetry.addData("Y",gamepad1.left_stick_y);
            telemetry.update();
        }
    }
}