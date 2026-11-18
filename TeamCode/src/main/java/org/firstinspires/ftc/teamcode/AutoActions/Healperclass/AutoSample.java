package org.firstinspires.ftc.teamcode.AutoActions.Healperclass;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.firstinspires.ftc.teamcode.AutoActions.IntakeAction;
import org.firstinspires.ftc.teamcode.AutoActions.StooperAction;
import org.firstinspires.ftc.teamcode.AutoActions.TransferAction;
import org.firstinspires.ftc.teamcode.AutoActions.UtilActions;
import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;
@Disabled
@Config
@Autonomous(name = "Autotest", group = "Autonomous")
public class AutoSample extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException  {

        //Init for the Drivetrain and establishing an Initial Position
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        //init for all the Subsystems that this Opmode uses
        IntakeAction intake = new IntakeAction();
        StooperAction stooper = new StooperAction();
        TransferAction transfer = new TransferAction();
        UtilActions util = new UtilActions();


        TrajectoryActionBuilder IntakeFromGarden = drive.actionBuilder(initialPose)
                .lineToX(24);



        TrajectoryActionBuilder ScoreIntakeFromGurden = IntakeFromGarden.endTrajectory().fresh()
                .lineToX(44);

        telemetry.addData("Status" , "Initialized. Ready to Start");
        telemetry.update();

        //Starts the Opmode
        if (isStopRequested()) return;
        Actions.runBlocking(
            new SequentialAction(
                //Actions that will happend before building a trajectory is built  
                    util.waitCommand(0.3),
                    intake.IntakeRun(),
                
                //Creating a new parallel action so that tab1 trajectory will Intake while moving
                new ParallelAction(
                    IntakeFromGarden.build(),
                    intake.IntakeRun()
                ),

                //Intake Stops and robot Resumes Course
                    intake.IntakePause(),
                    ScoreIntakeFromGurden.build(),

                // creating a new Sequence Action for the robot
                // actions will happen one after the other
                new SequentialAction(
                    IntakeFromGarden.build(),
                    util.waitCommand(0.3),
                    intake.IntakeRun()
                )
            )
        );

    }
}
