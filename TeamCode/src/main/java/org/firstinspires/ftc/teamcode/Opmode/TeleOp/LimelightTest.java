package org.firstinspires.ftc.teamcode.Opmode.TeleOp;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystem.DriveTrain;
import org.firstinspires.ftc.teamcode.vision.HiveAim;

import java.util.List;

@TeleOp(name = "Limelight Test", group = "Test")
public class LimelightTest extends LinearOpMode {
    private static final String LIMELIGHT_HARDWARE_NAME = "limelight";
    private static final int TELEMETRY_INTERVAL_MS = 100;
    private static final int DRIVE_LOOP_INTERVAL_MS = 20;

    @Override
    public void runOpMode() {
        Limelight3A limelight = hardwareMap.tryGet(Limelight3A.class, LIMELIGHT_HARDWARE_NAME);
        if (limelight == null) {
            telemetry.addData("Limelight", "Missing from Robot Configuration: %s", LIMELIGHT_HARDWARE_NAME);
            telemetry.update();
            waitForStart();
            return;
        }

        DriveTrain driveTrain = initializeDriveTrain();
        HiveAim hiveAim = new HiveAim(limelight);

        telemetry.setMsTransmissionInterval(TELEMETRY_INTERVAL_MS);
        telemetry.addData("Limelight", "Press Start to read the camera");
        telemetry.addData("Drive correction", driveTrain == null ? "Check motor configuration" : "Hold Cross (X)");
        telemetry.update();
        waitForStart();
        if (isStopRequested()) return;

        int requestedPipelineIndex = HiveAim.APRIL_TAG_PIPELINE;
        boolean wasAligningToHive = false;
        try {
            hiveAim.start();
            while (opModeIsActive()) {
                boolean alignToHive = gamepad1.cross;
                if (driveTrain != null) {
                    hiveAim.update(alignToHive, driveTrain, telemetry);
                    if (wasAligningToHive && !alignToHive) {
                        driveTrain.driveRobotCentric(0.0, 0.0, 0.0);
                    }
                    wasAligningToHive = alignToHive;
                } else if (alignToHive) {
                    telemetry.addData("Drive correction", "Check motor configuration");
                }

                telemetry.addData("Requested pipeline", requestedPipelineIndex);
                showCameraTelemetry(limelight);
                telemetry.update();
                sleep(DRIVE_LOOP_INTERVAL_MS);
            }
        } finally {
            try {
                if (driveTrain != null) driveTrain.driveRobotCentric(0.0, 0.0, 0.0);
            } finally {
                hiveAim.stop();
            }
        }
    }

    private DriveTrain initializeDriveTrain() {
        DriveTrain driveTrain = new DriveTrain();
        try {
            driveTrain.initDriveMotors(hardwareMap);
            return driveTrain;
        } catch (IllegalArgumentException configurationError) {
            telemetry.addData("Drive setup", configurationError.getMessage());
            return null;
        }
    }

    private void showCameraTelemetry(Limelight3A limelight) {
        boolean cameraConnected = limelight.isConnected();
        telemetry.addData("Limelight connected", cameraConnected);
        if (!cameraConnected) {
            telemetry.addData("Camera result", "Waiting for data");
            return;
        }

        LLResult latestResult = limelight.getLatestResult();
        telemetry.addData("Active pipeline", "%d (%s)", latestResult.getPipelineIndex(), latestResult.getPipelineType());
        telemetry.addData("Frame age (ms)", latestResult.getStaleness());
        telemetry.addData("Target valid", latestResult.isValid());
        if (!latestResult.isValid()) return;

        telemetry.addData("Primary target tx / ty (deg)", "%.2f / %.2f", latestResult.getTx(), latestResult.getTy());
        List<FiducialResult> detectedTags = latestResult.getFiducialResults();
        telemetry.addData("AprilTags detected", detectedTags.size());
        for (FiducialResult detectedTag : detectedTags) {
            telemetry.addData("Tag " + detectedTag.getFiducialId() + " tx / ty (deg)", "%.2f / %.2f",
                    detectedTag.getTargetXDegreesNoCrosshair(), detectedTag.getTargetYDegreesNoCrosshair());
        }
    }
}
