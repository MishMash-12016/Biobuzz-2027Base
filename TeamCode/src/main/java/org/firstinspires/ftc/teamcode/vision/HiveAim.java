package org.firstinspires.ftc.teamcode.vision;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystem.DriveTrain;

@Config
public class HiveAim {
    public static int APRIL_TAG_PIPELINE = 0;
    // Set to any tag ID in the desired CELL. -1 selects the strongest visible BIOBUZZ CELL.
    public static int HIVE_TAG_ID = -1;
    public static double CAMERA_FORWARD_METERS = 0.0;
    public static double CAMERA_RIGHT_METERS = 0.0;
    public static double CAMERA_YAW_DEGREES = 0; // Clockwise from robot forward.
    public static double CAMERA_PITCH_DEGREES = 30; // Positive when aimed up.
    public static double CAMERA_ROLL_DEGREES = 0.0;  // Positive when the right edge is lower.

    private static final int FIRST_HIVE_TAG = 30;
    private static final int LAST_HIVE_TAG = 45;
    private static final int ANY_CELL = -1;
    private static final int INVALID_CELL = -2;
    private static final long MAX_FRAME_AGE_MS = 150;
    private static final PoseVelocity2d STOPPED = new PoseVelocity2d(new Vector2d(0.0, 0.0), 0.0);

    private final Limelight3A camera;
    private int pipeline;
    private int lockedCell = ANY_CELL;
    private double lastFrameTimestamp = Double.NaN;
    private long lastNewFrameNanos;

    public HiveAim(Limelight3A camera) {
        this.camera = camera;
    }

    public void start() {
        if (camera != null) {
            pipeline = APRIL_TAG_PIPELINE;
            camera.pipelineSwitch(pipeline);
            camera.start();
        }
    }

    public void stop() {
        if (camera != null) {
            camera.stop();
        }
    }

    public void update(boolean aiming, DriveTrain drive, Telemetry telemetry) {
        long now = System.nanoTime();
        LLResult frame = camera == null ? null : camera.getLatestResult();
        double frameTimestamp = frame == null ? 0.0 : frame.getTimestamp();
        if (Double.isFinite(frameTimestamp) && frameTimestamp > 0.0 && frameTimestamp != lastFrameTimestamp) {
            lastFrameTimestamp = frameTimestamp;
            lastNewFrameNanos = now;
        }

        if (!aiming) {
            lockedCell = ANY_CELL;
            return;
        }

        if (frame != null && frame.isValid() && frame.getPipelineIndex() == pipeline
                && frameTimestamp == lastFrameTimestamp
                && frame.getStaleness() <= MAX_FRAME_AGE_MS
                && now - lastNewFrameNanos <= MAX_FRAME_AGE_MS * 1_000_000L
                && alignToCell(frame, drive, telemetry)) {
            return;
        }

        drive.setDrivePowers(STOPPED);
        telemetry.addData("Hive aim", camera == null ? "Limelight missing" : "Waiting for Hive tag");
    }

    private boolean alignToCell(LLResult frame, DriveTrain drive, Telemetry telemetry) {
        int requestedTag = HIVE_TAG_ID;
        int requestedCell = lockedCell;
        if (lockedCell == ANY_CELL && requestedTag != -1) {
            requestedCell = cellForTag(requestedTag);
        }
        FiducialResult bestTag = null;
        for (FiducialResult tag : frame.getFiducialResults()) {
            int cell = cellForTag(tag.getFiducialId());
            if (cell >= 0 && (requestedCell == ANY_CELL || cell == requestedCell)
                    && (bestTag == null || tag.getTargetArea() > bestTag.getTargetArea())) {
                bestTag = tag;
            }
        }
        if (bestTag == null) {
            return false;
        }

        int tagId = bestTag.getFiducialId();
        int cell = cellForTag(tagId);
        HiveAimMath math = new HiveAimMath(CAMERA_FORWARD_METERS, CAMERA_RIGHT_METERS,
                CAMERA_YAW_DEGREES, CAMERA_PITCH_DEGREES, CAMERA_ROLL_DEGREES);
        HiveAimMath.Alignment alignment = math.calculate(tagId - cell, bestTag.getTargetPoseCameraSpace(),
                bestTag.getTargetXDegreesNoCrosshair(), bestTag.getTargetYDegreesNoCrosshair());
        if (alignment == null) {
            return false;
        }

        drive.setDrivePowers(alignment.drivePowers);
        lockedCell = cell;
        telemetry.addData("Hive aim", alignment.aligned ? "Aligned" : "Aligning");
        telemetry.addData("Hive tag", tagId);
        telemetry.addData("Front bearing to tag (deg)", "%.1f", alignment.tagBearingDegrees);
        telemetry.addData("Tag distance (m)", "%.2f / %.2f",
                alignment.tagDistanceMeters, HiveAimMath.TARGET_DISTANCE_METERS);
        telemetry.addData("Hive heading error (deg)", "%.1f", alignment.headingErrorDegrees);
        return true;
    }

    private static int cellForTag(int tagId) {
        if (tagId < FIRST_HIVE_TAG || tagId > LAST_HIVE_TAG) {
            return INVALID_CELL;
        }
        return tagId - (tagId - FIRST_HIVE_TAG) % HiveAimMath.TAGS_PER_CELL;
    }



    private boolean isTip(int startId,int endId) {
        LLResult data = camera.getLatestResult();
        if (data.isValid()) {
            for (LLResultTypes.FiducialResult tag : data.getFiducialResults()) {
                int id = tag.getFiducialId();
                if (id >= startId && id <=endId) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    public boolean isTipRedAudience() {
        return isTip(30,33);

    }

    public boolean isTipBlueAudience() {
        return isTip(38,41);

    }
        public boolean isTipRedScoring() {
        return isTip(34,37);
        }

        public boolean isTipBlueScoring() {
            return isTip(42,45);

        }

    }
