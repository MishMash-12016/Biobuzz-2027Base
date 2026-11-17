package org.firstinspires.ftc.teamcode.Opmodes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutoCommands.IntakeAuto;
import org.firstinspires.ftc.teamcode.AutoCommands.StooperAuto;
import org.firstinspires.ftc.teamcode.AutoCommands.TransferAuto;
import org.firstinspires.ftc.teamcode.AutoCommands.UtilActions;
import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;

@Config
@Autonomous(name = "Autotest", group = "Autonomous")
public class Auto_2_Red extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException  {

        //Init for the Drivetrain and establishing an Initial Position
        Pose2d initialPose = new Pose2d(62, 12, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        //init for all the Subsystems that this Opmode uses
        IntakeAuto intake = new IntakeAuto();
        StooperAuto stooper = new StooperAuto();
        TransferAuto transfer = new TransferAuto();
        UtilActions util = new UtilActions();

        intake.init(hardwareMap);
        stooper.init(hardwareMap);
        transfer.init(hardwareMap);



        TrajectoryActionBuilder IntakeFromGarden = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(55 , 56) , Math.toRadians(90))
                .lineToY(62);


        TrajectoryActionBuilder ScoreIntakeFromGurden = IntakeFromGarden.endTrajectory().fresh()
                .strafeToSplineHeading(new Vector2d(-43 , 55) , Math.toRadians(90))
                .stopAndAdd(intake.IntakeRun())
                .waitSeconds(0.3)
                .stopAndAdd(intake.IntakePause());


        TrajectoryActionBuilder GoPark = ScoreIntakeFromGurden.endTrajectory().fresh()
                .lineToX(-65);





        telemetry.addData("Status" , "Initialized. Ready to Start");
        telemetry.update();



        //Starts the Opmode
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(

                        new ParallelAction(
                                IntakeFromGarden.build(),
                                intake.IntakeRun()
                        ),

                        ScoreIntakeFromGurden.build(),
                        transfer.TransferRunning(),
                        util.waitCommand(0.3),
                        transfer.TransferStop()




                )
        );

    }
}

