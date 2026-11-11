package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.SubsystemBase;

@Config
public class Shooter extends SubsystemBase {
    private DcMotorEx ShooterL;
    private DcMotorEx ShooterR;
    private FtcDashboard Dashboard;

    public static double KP = 0.1;
    public static double KF = 7000;
    public static double KI = 0.001;
    public static double KD = 0;
    public static double Power = 0.1;
    public static double Velocity = 20;

    public void init(HardwareMap hardwareMap ) {
        Dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorImplEx.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        ShooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        ShooterR.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        // TODO Change
        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        ShooterL.setVelocityPIDFCoefficients(KP,KI,KD,KF);
    }


    //Velocity in rad / S
    public void SetVelocity (double velocity){
//        PIDFCoefficients pidParameters = new PIDFCoefficients(KP,KI,KD,KF);
        if(velocity != 0) {
            ShooterL.setVelocity(10000);
        } else {
            ShooterL.setVelocity(0);
        }
    }
    public void printVelocity(){
        TelemetryPacket velocityPacket= new TelemetryPacket();
        velocityPacket.put("velocity left", ShooterL.getVelocity());
        velocityPacket.put("L power", ShooterL.getPower());
        velocityPacket.put("pid",ShooterL.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER));
        Dashboard.sendTelemetryPacket(velocityPacket);
    }
    public void setShooter(double Power){
        ShooterL.setPower(Power);
        ShooterR.setPower(Power);
    }

    public void setPowerByDashboard(){
        setShooter(Power);
    }
}
