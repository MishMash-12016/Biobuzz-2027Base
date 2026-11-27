package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.MecanumKinematics;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.PoseVelocity2dDual;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class DriveTrain extends SubsystemBase {
    private static final MecanumKinematics POWER_KINEMATICS = new MecanumKinematics(1.0);
    public static double lastHeading;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    public GoBildaPinpointDriver pinpoint;

    private FtcDashboard Dashboard;

    public void init(HardwareMap hardwareMap) {
        initDriveMotors(hardwareMap);
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        if (lastHeading != 0.0) {
            pinpoint.setHeading(lastHeading, AngleUnit.DEGREES);
            lastHeading = 0.0;
        }

        configurePinpoint();
    }

    public void initDriveMotors(HardwareMap hardwareMap) {
        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class,  "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class,   "bl");
        backRight = hardwareMap.get(DcMotor.class,  "br");

        // TODO Change
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void configurePinpoint() {
        // Set odometry pod offsets relative to the tracking center (in millimeters)
        // X: sideways distance (Left is positive), Y: forward distance (Forward is positive)
        //TODO                    change          change          change
        pinpoint.setOffsets(-84.0, -168.0, DistanceUnit.INCH); // Update these values to match your physical robot measurements

        // Set the odometry pod type (using goBILDA 4-Bar pod as an example)
        //TODO                                                                       CHACK
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        // Set encoder directions (FORWARD or REVERSED depending on how they count)
        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // Reset position and calibrate IMU (Robot must remain completely still during init!)
        pinpoint.resetPosAndIMU();
    }

    /**
     * Updates the Pinpoint odometry computer.
     */
    public void update() {
        pinpoint.update();
    }

    public void drive(Gamepad gamepad) {
        driveRobotCentric(gamepad.left_stick_x, -gamepad.left_stick_y, gamepad.right_stick_x);
    }

    public void driveRobotCentric(double strafeRightPower, double driveForwardPower, double turnRightPower) {
        setDrivePowers(new PoseVelocity2d(new Vector2d(driveForwardPower, -strafeRightPower), -turnRightPower));
    }

    public void setDrivePowers(PoseVelocity2d powers) {
        MecanumKinematics.WheelVelocities<Time> wheels =
                POWER_KINEMATICS.inverse(PoseVelocity2dDual.constant(powers, 1));
        double normalization = Math.max(1.0,
                Math.abs(powers.linearVel.x) + Math.abs(powers.linearVel.y) + Math.abs(powers.angVel));
        frontLeft.setPower(wheels.leftFront.value() / normalization);
        backLeft.setPower(wheels.leftBack.value() / normalization);
        frontRight.setPower(wheels.rightFront.value() / normalization);
        backRight.setPower(wheels.rightBack.value() / normalization);
    }

    public void fieldOrientedDrive(Gamepad gamepad) {
        double fieldForwardPower = Math.abs(gamepad.left_stick_y) > 0.1 ? -gamepad.left_stick_y : 0.0;
        double fieldRightPower = Math.abs(gamepad.left_stick_x) > 0.1 ? gamepad.left_stick_x : 0.0;
        double turnRightPower = Math.abs(gamepad.right_stick_x) > 0.1 ? gamepad.right_stick_x : 0.0;

        // Get the current robot heading from the Pinpoint in radians
        double robotHeadingRadians = pinpoint.getPosition().getHeading(AngleUnit.RADIANS);

        // Rotate the movement vector by the negative bot heading for field-centric control
        double robotStrafeRightPower = fieldRightPower * Math.cos(-robotHeadingRadians)
                - fieldForwardPower * Math.sin(-robotHeadingRadians);
        double robotDriveForwardPower = fieldRightPower * Math.sin(-robotHeadingRadians)
                + fieldForwardPower * Math.cos(-robotHeadingRadians);

        driveRobotCentric(robotStrafeRightPower, robotDriveForwardPower, turnRightPower);
        pinpoint.update();
    }


    /**
     * Returns the current Pose2D position and heading from the Pinpoint localizer.
     */
    public Pose2D getPosition() {
        return pinpoint.getPosition();
    }
    /**
     * Resets the Pinpoint position and IMU heading back to zero.
     */
    public void resetPose() {
        pinpoint.resetPosAndIMU();
    }

    public void resetHeading(){
        pinpoint.setHeading(0, AngleUnit.RADIANS);
    }

    public void locationPacket() {
        TelemetryPacket locationPacket = new TelemetryPacket();
       locationPacket.put("Field Location" ,pinpoint.getPosition());
        Dashboard.sendTelemetryPacket(locationPacket);
        Dashboard.updateConfig();
    }
}
