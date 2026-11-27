package org.firstinspires.ftc.teamcode.Subsystem;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake  {
    private static DcMotor intake;

    public void init(HardwareMap hardwareMap ){
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
    }


    public static void collect(){
        intake.setPower(1);
    }

    public static class Collect implements Action{

        @Override
    public boolean run(@NonNull TelemetryPacket packet) {
        intake.setPower(1);
        return false;
    }

        public static Action collect() {
          return new Collect();
        }
    }

    public static void eject(){
        intake.setPower(-1);
    }

    public static class Eject implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake.setPower(-1);
            return false;
        }

        public static Action eject() {
            return new Eject();
        }
    }

    public static void stop(){
        intake.setPower(0);
    }

    public static class Stop implements Action{

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake.setPower(0);
            return false;
        }

        public static Action stop() {
            return new Stop();
        }
    }



}
