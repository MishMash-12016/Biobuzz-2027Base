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

@Config
public class Shooter extends SubsystemBase {
    private DcMotorEx ShooterL;
    private DcMotorEx ShooterR;
    private FtcDashboard Dashboard;

    // Tunable via FTC Dashboard (must be public static for @Config)
    public static double KP = 1;
    public static double KF = 1.5;
    public static double targetVelocity = 1500; // ticks/sec — tune this on the dashboard

    public static double Ticks = 28;
    public static double gearRatio = 1;

    public void init(HardwareMap hardwareMap) {
        Dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        // ShooterL is PIDF-controlled with encoder
        ShooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        ShooterR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // TODO Change directions if needed
        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        applyPIDF();
    }

    /** Applies the current KP/KF values to ShooterL (PIDF-controlled). */
    private void applyPIDF() {
        PIDFCoefficients pidParameters = new PIDFCoefficients(KP, 0, 0, KF);
        ShooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidParameters);
    }

    /**
     * Spins ShooterL to the tunable targetVelocity using PIDF.
     * Call update() every loop to keep ShooterR mirroring ShooterL's power.
     */
    public void spinUp() {
        ShooterL.setVelocity(targetVelocity);
    }

    /**
     * Must be called every loop iteration.
     * Mirrors ShooterL's current power output to ShooterR (since ShooterR has no encoder).
     */
    public void update() {
        ShooterR.setPower(ShooterL.getPower());
    }

    /** Stops both shooter motors. */
    public void stop() {
        ShooterL.setVelocity(0);
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
    }
}
