package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

@Config // זה מה שהדשבורד מחפש וסורק באופן עצמאי
public class ShooterPARAM {
    public static double KP = 0.005;
    public static double KI = 0;
    public static double KD = 0;
    public static double KF = 0;
    public static double targetVelocity = 1000;
}