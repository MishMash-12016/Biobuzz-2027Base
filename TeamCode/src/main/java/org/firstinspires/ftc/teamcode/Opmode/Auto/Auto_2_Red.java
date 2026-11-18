package org.firstinspires.ftc.teamcode.Opmode.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutoActions.IntakeAction;
import org.firstinspires.ftc.teamcode.AutoActions.ShooterAction;
import org.firstinspires.ftc.teamcode.AutoActions.StooperAction;
import org.firstinspires.ftc.teamcode.AutoActions.TransferAction;
import org.firstinspires.ftc.teamcode.AutoActions.UtilActions;
import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;

@Config
@Autonomous(name = "Auto_2_Red", group = "Autonomous")
public class Auto_2_Red extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException  {

        //Init for the Drivetrain and establishing an Initial Position
        Pose2d initialPose = new Pose2d(62, -10, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // region CREATE AND INITIALIZE SYSTEMS
        //init for all the Subsystems that this Opmode uses
        IntakeAction intake = new IntakeAction();
        StooperAction stooper = new StooperAction();
        TransferAction transfer = new TransferAction();
        UtilActions util = new UtilActions();
        ShooterAction shooter = new  ShooterAction();

        intake.init(hardwareMap);
        stooper.init(hardwareMap);
        transfer.init(hardwareMap);
        shooter.init(hardwareMap);
        // endregion



        // region TRAJECTORY

        TrajectoryActionBuilder IntakeFromGarden = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(50 , -54) , Math.toRadians(90));


        TrajectoryActionBuilder ScoreIntakeFromGurden = IntakeFromGarden.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-20, -45) , Math.toRadians(90))
                .strafeToSplineHeading(new Vector2d(-54 , -18) , Math.toRadians(130))
                .waitSeconds(0.3);


        TrajectoryActionBuilder GoPark = ScoreIntakeFromGurden.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-54 , -56) , Math.toRadians(130));
        // endregion


        telemetry.addData("Status" , "Initialized. Ready to Start");
        telemetry.update();



        //Starts the Opmode
        if (isStopRequested()) {return;}


        waitForStart();

        while (opModeIsActive()){

            Actions.runBlocking(stooper.up());
            Actions.runBlocking(
                    new ParallelAction(

                    new SequentialAction(
                            shooter.spinAction(),
                            util.waitCommand(2),
                            stooper.Down(),
                            intake.IntakeRun(),
                            transfer.TransferRunning(),
                            util.waitCommand(3)
                    ),

                    intake.IntakePause(),
                    transfer.TransferStop(),
                    stooper.up(),

                    new ParallelAction(
                    IntakeFromGarden.build(),
                    intake.IntakeRun(),
                    transfer.TransferRunning()
                    ),

                    new SequentialAction(
                    ScoreIntakeFromGurden.build(),
                    util.waitCommand(3),
                    shooter.spinAction(),
                    util.waitCommand(3),
                    stooper.Down(),
                    intake.IntakeRun(),
                    transfer.TransferRunning()
                    ),

                    GoPark.build()

                    )
            );

        }

    }
}

