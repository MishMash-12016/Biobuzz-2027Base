package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;


@Config

public class Shooter extends SubsystemBase {
    private DcMotorEx ShooterL;
    private DcMotorEx ShooterR;

    private PIDFController pidController;

    private FtcDashboard Dashboard;

    // Tunable via FTC Dashboard (must be public static double primitive)

    public static double KP = 0;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 0;

    public static double targetVelocity = 1000; // ticks/sec — tune this on the dashboard

    public static double Ticks = 28;
    public static double gearRatio = 1;


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


    }

    /** Applies the current KP/KI/KD/KF values to ShooterL */
    public void applyPIDF() {
        pidController.setP(KP);
        pidController.setI(KI);
        pidController.setD(KD);
        pidController.setF(KF);

        pidController.setSetPoint(targetVelocity);

        ShooterL.setPower(pidController.calculate(ShooterL.getVelocity()));
        ShooterR.setPower(ShooterL.getPower());

    }

    /**
     * Checks for dynamic PIDF gain updates from FTC Dashboard and syncs power to ShooterR.
     */

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
        velocityPacket.put("ShooterL RPM", (ShooterL.getVelocity() / Ticks) * 60 / gearRatio);
        velocityPacket.put("ShooterR power (mirrored)", ShooterR.getPower());
        velocityPacket.put("Target velocity (ticks/s)", targetVelocity);
        velocityPacket.put("PIDF", ShooterL.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        Dashboard.sendTelemetryPacket(velocityPacket);
        Dashboard.updateConfig();
    }
}