package org.firstinspires.ftc.teamcode.Subsystem;

import androidx.annotation.NonNull;

import com.seattlesolvers.solverslib.util.InterpLUT;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.controller.PIDFController;

@Config
public class Shooter {
    private DcMotorEx ShooterL;
    private DcMotorEx ShooterR;
    //1165
    public double targetVel;
    private VoltageSensor voltageSensor;

    private PIDFController pidController;

    private FtcDashboard Dashboard;

    // Tunable via FTC Dashboard (must be public static double primitive)
    public static double IdealVoltage = 11.5;
    public static double KP = 0.02;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 0.0007575;
    public static double KS = 0.0;
    InterpLUT lut = new InterpLUT();

    public Shooter() {}

    public Shooter(HardwareMap hardwareMap) {
        init(hardwareMap);
    }

    public void init(HardwareMap hardwareMap) {
        Dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        // ShooterL is PIDF-controlled with encoder
        ShooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        ShooterL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        pidController = new PIDFController(KP, KI, KD, KF);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        // Adding each val with a key
        lut.add(1.1, 0.2);
        lut.add(2.7, .5);
        lut.add(3.6, 0.75);
        lut.add(4.1, 0.9);
        lut.add(5, 1);
        lut.createLUT();
    }

    public double interpolate(double distance) {
        return lut.get(distance);
    }

    /** Applies the current KP/KI/KD/KF/KS values to ShooterL */
    public void applyPIDF(double vel) {
        targetVel = vel;
        pidController.setP(KP);
        pidController.setI(KI);
        pidController.setD(KD);
        pidController.setF(KF);
        // חישוב פלט ה-PIDF (שכבר כולל בתוכו את ה-F והמהירות)
        double pidfPower = pidController.calculate(ShooterL.getVelocity(), vel);

        // הכפלת ההספק בתיקון המתח של הבטריה
        double power = (KS + pidfPower) * IdealVoltage / voltageSensor.getVoltage();
        // שליחת ההספק לשני המנועים (מנוע ימין מעתיק את שמאל)
        ShooterL.setPower(power);
        ShooterR.setPower(power);
        FtcDashboard.getInstance().getTelemetry().addData("volteg", voltageSensor.getVoltage());
        FtcDashboard.getInstance().getTelemetry().addData("power", power);
        FtcDashboard.getInstance().getTelemetry().update();
    }

    /** Spins Shooter to the given target velocity using PIDF. */
    public void spin(double vel) {
        applyPIDF(vel);
    }

    public void setVoltage(double power) {
        ShooterL.setPower(power);
    }

    /** Stops both shooter motors. */
    public void stop() {
        ShooterL.setPower(0);
        if (ShooterR != null) {
            ShooterR.setPower(0);
        }
    }

    /** Sends velocity and PIDF telemetry to FTC Dashboard. */
    public void printVelocity() {
        TelemetryPacket velocityPacket = new TelemetryPacket();
        velocityPacket.put("ShooterL velocity (ticks/s)", ShooterL.getVelocity());
        velocityPacket.put("Target velocity (ticks/s)", targetVel);
        velocityPacket.put("PIDF", ShooterL.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        Dashboard.sendTelemetryPacket(velocityPacket);
        Dashboard.updateConfig();
    }

    // ==================== Actions (Road Runner) ====================

    public class SpinAction implements Action {
        private final double vel;

        public SpinAction(double vel) {
            this.vel = vel;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            targetVel = vel;
            spin(vel);
            packet.put("shooterVelocity", ShooterL.getVelocity());
            packet.put("targetVelocity", vel);
            return false;
        }
    }

    public class SetVoltageAction implements Action {
        private final double power;

        public SetVoltageAction(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            setVoltage(power);
            packet.put("shooterPower", power);
            return false;
        }
    }

    public class StopAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            targetVel = 0;
            stop();
            packet.put("shooterVelocity", ShooterL != null ? ShooterL.getVelocity() : 0.0);
            return false;
        }
    }

    /** Continuous PIDF action intended to run in parallel with autonomous code. */
    public class ContinuousAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (targetVel > 0) {
                applyPIDF(targetVel);
            } else {
                stop();
            }
            if (ShooterL != null) {
                packet.put("shooterVelocity", ShooterL.getVelocity());
            }
            packet.put("targetVelocity", targetVel);
            return true;
        }
    }

    public Action continuousAction() {
        return new ContinuousAction();
    }

    public Action spinAction(double vel) {
        return new SpinAction(vel);
    }

    public Action setVoltageAction(double power) {
        return new SetVoltageAction(power);
    }

    public Action stopAction() {
        return new StopAction();
    }
}
