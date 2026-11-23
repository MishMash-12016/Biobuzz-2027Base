package org.firstinspires.ftc.teamcode.Opmode.Auto.Red;

import com.acmerobotics.dashboard.config.Config;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutoActions.RobotActions;
import org.firstinspires.ftc.teamcode.AutoActions.ShooterAction;
import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotSystem;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Subsystem.Stopper;

@Config
@Autonomous(name = "Auto_2_Red", group = "Autonomous")
public class Auto_2_Red extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException  {

        //Init for the Drivetrain and establishing an Initial Position
        Pose2d initialPose = new Pose2d(62, -10, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        RobotSystem robotSystem = new RobotSystem();
        RobotActions robotActions = new RobotActions();

        robotSystem.init(hardwareMap);




        // region TRAJECTORY

        TrajectoryActionBuilder IntakeFromGarden = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new com.acmerobotics.roadrunner.Vector2d(50 , -54) , Math.toRadians(90));


        TrajectoryActionBuilder ScoreIntakeFromGurden = IntakeFromGarden.endTrajectory().fresh()
                .strafeToLinearHeading(new com.acmerobotics.roadrunner.Vector2d(-20, -45) , Math.toRadians(90))
                .strafeToSplineHeading(new com.acmerobotics.roadrunner.Vector2d(-54 , -18) , Math.toRadians(130))
                .waitSeconds(0.3);


        TrajectoryActionBuilder GoPark = ScoreIntakeFromGurden.endTrajectory().fresh()
                .strafeToLinearHeading(new Vector2d(-54 , -56) , Math.toRadians(130));
        // endregion


        telemetry.addData("Status" , "Initialized. Ready to Start");
        telemetry.update();



        Action ActionIntakeFromGarden = IntakeFromGarden.build();
        Action ActionScoreIntakeFromGurden = ScoreIntakeFromGurden.build();

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
            new ParallelAction(
                    new SequentialAction(
                        RobotActions.InakeAction.intakeAction(),
                        new SleepAction(3),
                        RobotActions.ScoringAction.scoringAction()
                            
                    )
            )
        );
    }
}

