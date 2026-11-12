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

    // Tunable via FTC Dashboard (must be public static double primitive)

   public static double KP = 1;
    public static double KI = 1;
    public static double KD = 1;
    public static double KF = 1;

    public static double targetVelocity = 1000; // ticks/sec — tune this on the dashboard

    public static double Ticks = 28;
    public static double gearRatio = 1;


    public void init(HardwareMap hardwareMap) {
        Dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        // ShooterL is PIDF-controlled with encoder
        ShooterL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        ShooterR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        applyPIDF();
    }

    /** Applies the current KP/KI/KD/KF values to ShooterL */
    public void applyPIDF() {
        PIDFCoefficients pidParameters = new PIDFCoefficients(KP, KI, KD, KF);
        ShooterL.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidParameters);
    }

    /**
     * Checks for dynamic PIDF gain updates from FTC Dashboard and syncs power to ShooterR.
     */
    @Override
    public void periodic() {
        ShooterR.setPower(ShooterL.getPower());
    }

    /** Spins ShooterL to the current targetVelocity configured on FTC Dashboard. */
    public void spin() {
        ShooterL.setVelocity(targetVelocity);
        applyPIDF();
    }

    /** Stops both shooter motors without resetting targetVelocity. */
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
        Dashboard.updateConfig();
    }
}