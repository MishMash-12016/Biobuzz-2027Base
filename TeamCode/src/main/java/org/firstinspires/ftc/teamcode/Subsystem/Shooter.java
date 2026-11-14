package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;


@Config

public class Shooter extends SubsystemBase {
    private DcMotorEx ShooterL;
    private DcMotorEx ShooterR;
    private VoltageSensor voltageSensor;

    private PIDFController pidController;

    private FtcDashboard Dashboard;

    // Tunable via FTC Dashboard (must be public static double primitive)
    public static double IdealVoltage = 14;
    public static double KP = 0.01;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 0.000356;
    public static double KS = 0;
    public static double targetVelocity = 1500; // ticks/sec — tune this on the dashboard


    public void init(HardwareMap hardwareMap) {
        Dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        // ShooterL is PIDF-controlled with encoder
        ShooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        ShooterL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        ShooterR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        pidController = new PIDFController(KP, KI, KD, KF);
        voltageSensor = hardwareMap.voltageSensor.iterator().next();


    }

    /** Applies the current KP/KI/KD/KF/KS values to ShooterL */
    public void applyPIDF() {
        pidController.setP(KP);
        pidController.setI(KI);
        pidController.setD(KD);
        pidController.setF(KF);
        pidController.setSetPoint(targetVelocity);

        // חישוב פלט ה-PIDF (שכבר כולל בתוכו את ה-F והמהירות)
        double pidfPower = pidController.calculate(ShooterL.getVelocity());

// הכפלת ההספק בתיקון המתח של הבטריה
        double power = (KS + pidfPower) * IdealVoltage / voltageSensor.getVoltage();

// שליחת ההספק לשני המנועים (מנוע ימין מעתיק את שמאל)
        ShooterL.setPower(power);
        ShooterR.setPower(ShooterL.getPower());

    }

    /**
     * Checks for dynamic PIDF gain updates from FTC Dashboard and syncs power to ShooterR.
     * */

    /** Spins ShooterL to the current targetVelocity configured on FTC Dashboard. */
    public void spin() {
        applyPIDF();
    }

    /** Stops both shooter motors without resetting targetVelocity. */
    public void stop() {
        ShooterL.setPower(0);
        ShooterR.setPower(0);
    }

    /** Sends velocity and PIDF telemetry to FTC Dashboard. */
    public void printVelocity() {
        TelemetryPacket velocityPacket = new TelemetryPacket();
        velocityPacket.put("ShooterL velocity (ticks/s)", ShooterL.getVelocity());
        velocityPacket.put("Target velocity (ticks/s)", targetVelocity);
        velocityPacket.put("PIDF", ShooterL.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        Dashboard.sendTelemetryPacket(velocityPacket);
        Dashboard.updateConfig();
    }
}