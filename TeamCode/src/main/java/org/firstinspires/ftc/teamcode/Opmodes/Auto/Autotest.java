package org.firstinspires.ftc.teamcode.Opmodes.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions.IntakeAuto;
import org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions.StooperAuto;
import org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions.TransferAuto;
import org.firstinspires.ftc.teamcode.AutoCommandGroup.autoActions.UtilActions;
import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;

@Config
@Autonomous(name = "Autotest", group = "Autonomous")
public class Autotest extends LinearOpMode {

    @Override
    public void runOpMode()  {

        //Init for the Drivetrain and establishing an Initial Position
        Pose2d initialPose = new Pose2d(11.8, 61.7, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        //Init for the Auto Subsystems
        IntakeAuto intake = new IntakeAuto();
        StooperAuto Stooper = new StooperAuto();
        TransferAuto transfer = new TransferAuto();

        //A utility class that has Waits and ETC
        UtilActions utils = new UtilActions();

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                .lineToYSplineHeading(33, Math.toRadians(0))
                .waitSeconds(2)
                .setTangent(Math.toRadians(90))
                .lineToY(48)
                .setTangent(Math.toRadians(0))
                .lineToX(32)
                .strafeTo(new Vector2d(44.5, 30))
                .turn(Math.toRadians(180))
                .lineToX(47.5);


        TrajectoryActionBuilder tab2 = tab1.endTrajectory().fresh()
                .lineToX(44);

        // Actions that happen Along with the tab1 Trajectory using Actions
        new ActionCommand(tab1.build()).alongWith(
            new ActionCommand(
                    new SequentialAction(
                            transfer.TransferRunning(),
                            utils.waitCommand(0.3),
                            transfer.TransferStop()
                    )
            )
        );

        // Actoions that happend during tab1 trajectory using Commands
        new ActionCommand(tab1.build()).alongWith(
                new ParallelCommandGroup(
                    intake.IntakeRun(),
                    new WaitCommand(300),
                    intake.IntakePause()
                )
        );

        
        Actions.runBlocking(
                new SequentialAction(
                        tab1.build(),
                        utils.waitCommand(300),
                        tab2.build()
                )
        );

    }
}
