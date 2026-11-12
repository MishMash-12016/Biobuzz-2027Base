package org.firstinspires.ftc.teamcode.Teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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
        DriveTrain dt = new DriveTrain();
        Intake intake = new Intake();
        Shooter shooter = new Shooter();
        Transfer transfer = new Transfer();
        Stopper stopper = new Stopper();

        dt.init(hardwareMap);
        intake.init(hardwareMap);
        shooter.init(hardwareMap);
        transfer.init(hardwareMap);
        stopper.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            dt.fieldOrientedDrive(gamepad1);
            if (gamepad1.options) {
                dt.resetHeading();
            }

            // Intake + Transfer
            if (gamepad1.b) {
                intake.setIntake(1);
                transfer.setPower(1);
            } else {
                intake.setIntake(0);
                transfer.setPower(0);
            }

            // Shooter Trigger & Emergency Stop Control
            if (gamepad1.right_trigger < 0.1 ){
                // Trigger pressed: Spin up using targetVelocity and active PIDF
                shooter.spin();
            }


            if (gamepad1.x){
                shooter.stop();
            }



            // Stopper
            if (gamepad1.dpad_up) {
                stopper.open();
            }
            if (gamepad1.dpad_down) {
                stopper.close();
            }

            // Always update subsystem periodic logic (syncs dynamic PIDF tuning & motor mirroring)

            // Telemetry & Dashboard telemetry output
            //shooter.printVelocity();

            telemetry.addData("ang", dt.pinpoint.getPosition().getHeading(AngleUnit.DEGREES));
            telemetry.addData("stopper", stopper.getPosition());
            telemetry.update();
        }
    }
}