package org.firstinspires.ftc.teamcode.Opmode.Auto.Red;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;

public class AutoPaths {
    Pose2d initialPose = new Pose2d(62, -10, Math.toRadians(0));
    MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

    TrajectoryActionBuilder GOpark = drive.actionBuilder(initialPose)
            .strafeToLinearHeading(new Vector2d(5,5 ) , Math.toRadians(120));

}
