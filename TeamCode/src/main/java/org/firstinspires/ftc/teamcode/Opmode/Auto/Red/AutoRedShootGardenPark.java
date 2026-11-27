package org.firstinspires.ftc.teamcode.Opmode.Auto.Red;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotSystem;

@Config
@Autonomous(group = "AutonomousRed")
public class AutoRedShootGardenPark extends LinearOpMode {

  public static double SHOOTER_VELOCITY = 1165;
  public static double WAIT_SHOOTER_SPIN = 1.5;
  public static double INTAKE_TRANSFER_TIME = 2.0;

  @Override
  public void runOpMode() throws InterruptedException {

    // Init for the Drivetrain and establishing an Initial Position
    Pose2d initialPose = new Pose2d(62, -13, Math.toRadians(0));
    MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

    RobotSystem robotSystem = new RobotSystem();
    robotSystem.init(hardwareMap);

    // region TRAJECTORY

    TrajectoryActionBuilder IntakeFromGarden =
        drive
            .actionBuilder(initialPose)
            .setTangent(Math.toRadians(180))
            .splineToLinearHeading(new Pose2d(61, -60.5, Math.toRadians(0)), Math.toRadians(0));

    TrajectoryActionBuilder ScoreIntakeFromGurden =
        IntakeFromGarden.endTrajectory()
            .fresh()
            .setTangent(Math.toRadians(180))
            .splineToLinearHeading(new Pose2d(62, -13, Math.toRadians(0)), Math.toRadians(50))
            .waitSeconds(0.3);

    TrajectoryActionBuilder GoPark =
        ScoreIntakeFromGurden.endTrajectory()
            .fresh()
            .setTangent(Math.toRadians(240))
            .splineToLinearHeading(new Pose2d(-24, -56, 0), Math.toRadians(180));

    telemetry.addData("Status", "Initialized. Ready to Start");
    telemetry.update();

    Action ActionIntakeFromGarden = IntakeFromGarden.build();
    Action ActionScoreIntakeFromGurden = ScoreIntakeFromGurden.build();
    Action ActionPark = GoPark.build();

    waitForStart();

    if (isStopRequested()) return;

    Actions.runBlocking(
        new ParallelAction(
            // Continuous PIDF shooter action running in parallel with all other actions
            robotSystem.shooter.continuousAction(),

            // Main autonomous sequence
            new SequentialAction(
                // 1. stopper up
                robotSystem.stopper.upAction(),

                // 2. shooter spin (switches targetVel to SHOOTER_VELOCITY)
                robotSystem.shooter.spinAction(SHOOTER_VELOCITY),

                // 3. wait (time for shooter to accelerate to speed)
                // TODO: wait until shooter get to vel
                new SleepAction(WAIT_SHOOTER_SPIN),

                // 4. stopper down
                robotSystem.stopper.downAction(),

                // 5. intake, transfer run
                new ParallelAction(
                    robotSystem.intake.collectAction(),
                    robotSystem.transfer.transferRunningAction()),
                new SleepAction(INTAKE_TRANSFER_TIME),
                new ParallelAction(
                    robotSystem.transfer.stopAction(), robotSystem.stopper.upAction()),
                ActionIntakeFromGarden,
                new SleepAction(1),
                ActionScoreIntakeFromGurden,
                robotSystem.stopper.downAction(),

                // 5. intake, transfer run
                new ParallelAction(
                    robotSystem.intake.collectAction(),
                    robotSystem.transfer.transferRunningAction()),
                new SleepAction(INTAKE_TRANSFER_TIME),
                new ParallelAction(
                    robotSystem.transfer.stopAction(), robotSystem.stopper.upAction()),
                ActionPark)));

    robotSystem.shooter.stop();
  }

  public static boolean isHiveUp() {
    return true;
  }
}
