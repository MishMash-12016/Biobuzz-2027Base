package org.firstinspires.ftc.teamcode.vision;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

final class HiveAimMath {
    // Each BIOBUZZ CELL has the same row of four tags, ordered left to right.
    private static final double[] TAG_OFFSETS_INCHES = {-6.50, -2.75, 2.75, 6.50};
    static final int TAGS_PER_CELL = TAG_OFFSETS_INCHES.length;
    static final double TARGET_DISTANCE_METERS = 1.2;
    private static final double POSITION_TOLERANCE_METERS = 0.03;
    private static final double HEADING_TOLERANCE_RADIANS = Math.toRadians(3.0);
    private static final double MAX_TRANSLATION_POWER = 0.30;
    private static final double MAX_TURN_POWER = 0.22;

    private final Vector2d cameraOffset;
    private final double cameraYaw;
    private final double cameraPitch;
    private final double cameraRoll;

    HiveAimMath(double cameraForwardMeters, double cameraRightMeters,
            double yawDegrees, double pitchDegrees, double rollDegrees) {
        cameraOffset = new Vector2d(cameraForwardMeters, -cameraRightMeters);
        cameraYaw = Math.toRadians(yawDegrees);
        cameraPitch = Math.toRadians(pitchDegrees);
        cameraRoll = Math.toRadians(rollDegrees);
    }

    // A null result indicates that the measured pose cannot be used for alignment.
    Alignment calculate(int tagIndex, Pose3D pose, double txDegrees, double tyDegrees) {
        Position cameraToTag = pose.getPosition().toUnit(DistanceUnit.METER);
        if (!Double.isFinite(cameraToTag.z) || cameraToTag.z <= 0.0) {
            return null;
        }
        // Use the lens-centered tx and ty to construct a ray. The 3D pose supplies depth in meters.
        Vector2d angles = new Vector2d(Math.toRadians(txDegrees),
                Math.toRadians(tyDegrees));
        double rayRight = Math.tan(angles.x);
        double rayDown = -Math.tan(angles.y);
        double depth = cameraToTag.z;
        // Firmware without lens-centered angles reports zero. Check the ray against the 3D pose.
        if (!Double.isFinite(rayRight) || Math.abs(rayRight - cameraToTag.x / depth) > 0.04) {
            rayRight = cameraToTag.x / depth;
        }
        if (!Double.isFinite(rayDown) || Math.abs(rayDown - cameraToTag.y / depth) > 0.04) {
            rayDown = cameraToTag.y / depth;
        }
        Vector2d tagPosition = cameraToFloor(rayRight * depth, rayDown * depth, depth)
                .plus(cameraOffset);

        // The tag's +X axis runs along the row. Project it onto the floor before steering.
        double yaw = pose.getOrientation().getYaw(AngleUnit.RADIANS);
        double pitch = pose.getOrientation().getPitch(AngleUnit.RADIANS);
        double cosPitch = Math.cos(pitch);
        Vector2d tagRight = cameraToFloor(Math.cos(yaw) * cosPitch, Math.sin(yaw) * cosPitch,
                -Math.sin(pitch));
        double rightLength = tagRight.norm();
        if (!Double.isFinite(tagPosition.norm()) || !Double.isFinite(rightLength) || rightLength < 0.5) {
            return null;
        }
        tagRight = tagRight.div(rightLength);
        Vector2d towardHive = new Vector2d(tagRight.x, -tagRight.y);
        if (towardHive.dot(tagPosition) < 0.0) {
            towardHive = towardHive.unaryMinus();
        }

        // Center on the row at the configured horizontal distance from the physical tag.
        double offset = DistanceUnit.METER.fromInches(TAG_OFFSETS_INCHES[tagIndex]);
        double standoff = Math.sqrt(TARGET_DISTANCE_METERS * TARGET_DISTANCE_METERS - offset * offset);
        Vector2d positionError = tagPosition.minus(tagRight.times(offset)).minus(towardHive.times(standoff));
        // Aim the robot's front toward the Hive so the forward-facing camera can track the tag.
        double headingError = towardHive.angleCast().toDouble();
        double positionErrorMeters = positionError.norm();
        boolean positionReady = positionErrorMeters <= POSITION_TOLERANCE_METERS;
        boolean headingReady = Math.abs(headingError) <= HEADING_TOLERANCE_RADIANS;
        double translationScale = positionReady ? 0.0 : Math.min(1.0, MAX_TRANSLATION_POWER / positionErrorMeters);
        double turn = headingReady ? 0.0 : Range.clip(headingError, -MAX_TURN_POWER, MAX_TURN_POWER);
        PoseVelocity2d drivePowers = new PoseVelocity2d(positionError.times(translationScale), turn);
        double tagBearing = tagPosition.angleCast().toDouble();
        return new Alignment(drivePowers, positionReady && headingReady, tagPosition.norm(),
                Math.toDegrees(tagBearing), Math.toDegrees(headingError));
    }

    // Camera axes are right, down, forward. Road Runner uses robot forward and left.
    private Vector2d cameraToFloor(double right, double down, double forward) {
        double rolledRight = right * Math.cos(cameraRoll) - down * Math.sin(cameraRoll);
        double rolledDown = right * Math.sin(cameraRoll) + down * Math.cos(cameraRoll);
        double pitchedForward = forward * Math.cos(cameraPitch) + rolledDown * Math.sin(cameraPitch);
        return new Vector2d(pitchedForward * Math.cos(cameraYaw) - rolledRight * Math.sin(cameraYaw),
                -pitchedForward * Math.sin(cameraYaw) - rolledRight * Math.cos(cameraYaw));
    }

    static final class Alignment {
        final PoseVelocity2d drivePowers;
        final boolean aligned;
        final double tagDistanceMeters;
        final double tagBearingDegrees;
        final double headingErrorDegrees;

        Alignment(PoseVelocity2d drivePowers, boolean aligned, double tagDistanceMeters,
                double tagBearingDegrees, double headingErrorDegrees) {
            this.drivePowers = drivePowers;
            this.aligned = aligned;
            this.tagDistanceMeters = tagDistanceMeters;
            this.tagBearingDegrees = tagBearingDegrees;
            this.headingErrorDegrees = headingErrorDegrees;
        }
    }
}
