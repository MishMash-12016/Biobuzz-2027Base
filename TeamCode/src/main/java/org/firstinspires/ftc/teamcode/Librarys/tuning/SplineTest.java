package org.firstinspires.ftc.teamcode.Librarys.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;
import org.firstinspires.ftc.teamcode.Librarys.TankDrive;

@Config
@Autonomous(name = "spline", group = "Autonomous")
public final class SplineTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(62, -13, Math.toRadians(0));
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

            waitForStart();

            Actions.runBlocking(
                drive.actionBuilder(beginPose)
                        .setTangent(Math.toRadians(180))
                        .splineToLinearHeading(new Pose2d(61 , -60.5,Math.toRadians(0))
                                ,Math.toRadians(0)).build());
        }
    }
}
