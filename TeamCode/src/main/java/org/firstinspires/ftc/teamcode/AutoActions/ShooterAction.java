package org.firstinspires.ftc.teamcode.AutoActions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.seattlesolvers.solverslib.controller.PIDFController;

public class ShooterAction {

    private DcMotorEx ShooterL;
    public double targetVel = 0;
    private VoltageSensor voltageSensor;

    private PIDFController pidController;

    private FtcDashboard Dashboard;

    // Tunable via FTC Dashboard (must be public static double primitive)
    public static double IdealVoltage = 14;
    public static double KP = 0.0155;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 0.00031;
    public static double KS = 0.13;
//    public double targetVelocity = 1165; // ticks/sec — tune this on the dashboard


    public void init(HardwareMap hardwareMap) {
        Dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");

        // ShooterL is PIDF-controlled with encoder
        ShooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        ShooterL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);

        pidController = new PIDFController(KP, KI, KD, KF);
        voltageSensor = hardwareMap.voltageSensor.iterator().next();

    }

    /** Applies the current KP/KI/KD/KF/KS values to ShooterL */
    public void applyPIDF(double vel) {
        targetVel = vel;
        if (vel > ShooterL.getVelocity()){
            pidController.setP(KP);
            pidController.setI(KI);
            pidController.setD(KD);
            pidController.setF(KF);
            pidController.setSetPoint(vel);

            // חישוב פלט ה-PIDF (שכבר כולל בתוכו את ה-F והמהירות)
            double pidfPower = pidController.calculate(ShooterL.getVelocity());

            // הכפלת ההספק בתיקון המתח של הבטריה
            double power = (KS + pidfPower) * IdealVoltage / voltageSensor.getVoltage();

            // שליחת ההספק לשני המנועים (מנוע ימין מעתיק את שמאל)
            ShooterL.setPower(power);

        }else {
            ShooterL.setPower(0);
        }
    }


    /** Spins ShooterL to the current targetVelocity configured on FTC Dashboard. */
    public void spin(double vel) {
        applyPIDF(vel);
    }

    public Action spinAction() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (ShooterL != null){
                spin(targetVel);
                }
                return true;
            }
        };
    }

    public Action speed() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (ShooterL != null){
                    targetVel =1165;
                }
                return true;
            }
        };
    }




    /** Stops both shooter motors without resetting targetVelocity. */
    public void stop() {
        ShooterL.setPower(0);
    }

    /** Sends velocity and PIDF telemetry to FTC Dashboard. */
    public void printVelocity() {
        TelemetryPacket velocityPacket = new TelemetryPacket();
        velocityPacket.put("ShooterL velocity (ticks/s)", ShooterL.getVelocity());
        velocityPacket.put("Target velocity (ticks/s)",targetVel );
        velocityPacket.put("PIDF", ShooterL.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        Dashboard.sendTelemetryPacket(velocityPacket);
        Dashboard.updateConfig();
    }

}
