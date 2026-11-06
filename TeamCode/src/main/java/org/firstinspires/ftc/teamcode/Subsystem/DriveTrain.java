package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class DriveTrain {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    public GoBildaPinpointDriver pinpoint;

    public void init(HardwareMap hardwareMap) {
        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft = hardwareMap.get(DcMotor.class, "bl");
        backRight = hardwareMap.get(DcMotor.class, "br");
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        configurePinpoint();

        // TODO Change
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
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
        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;
        double rx = gamepad.right_stick_x;

        // Denominator scales powers down if any individual motor exceeds 1.0 magnitude
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);

        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public void fieldOrientedDrive(Gamepad gamepad) {
        double y,x,rx;
        if (Math.abs(gamepad.left_stick_y) > 0.1)  {y = gamepad.left_stick_y;}    else {y = 0;}
        if (Math.abs(gamepad.left_stick_x) > 0.1)  {x = -gamepad.left_stick_x;}    else {x = 0;}
        if (Math.abs(gamepad.right_stick_x)> 0.1)  {rx = gamepad.right_stick_x;}  else {rx = 0;}

        // Get the current robot heading from the Pinpoint in radians
        double botHeading = pinpoint.getPosition().getHeading(AngleUnit.RADIANS);

        // Rotate the movement vector by the negative bot heading for field-centric control
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        // Denominator scaling to keep powers within [-1, 1]
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1.0);

        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY + rotX - rx) / denominator;
        double backRightPower = (rotY - rotX - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);

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
        pinpoint.setHeading(0,AngleUnit.RADIANS);
    }


}
