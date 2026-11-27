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
import org.firstinspires.ftc.teamcode.Actions.ConditionalAction;
import org.firstinspires.ftc.teamcode.Librarys.MecanumDrive;
import org.firstinspires.ftc.teamcode.RobotSystem;

@Config
@Autonomous(group = "AutonomousRed")
public class AutoRedShootMoveShootPark extends LinearOpMode {

  public static double SHOOTER_VELOCITY = 1165;
  public static double WAIT_SHOOTER_SPIN = 1.5;
  public static double INTAKE_TRANSFER_TIME = 2.0;

  TrajectoryActionBuilder ShootOnePosition;
  TrajectoryActionBuilder IntakeFromGarden;
  TrajectoryActionBuilder ScoreIntakeFromGarden;
  TrajectoryActionBuilder ParkTwoShots;
  TrajectoryActionBuilder MoveSecondPosition;
  TrajectoryActionBuilder Park;

  Action ActionShootOnePosition;
  Action ActionIntakeFromGarden;
  Action ActionScoreIntakeFromGurden;
  Action ActionMoveSecondPosition;
  Action ActionPark;
  Action ActionParkTwoShots;

  @Override
  public void runOpMode() throws InterruptedException {

    // Init for the Drivetrain and establishing an Initial Position
    Pose2d initialPose = new Pose2d(62, -37, Math.toRadians(0));
    MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
    RobotSystem robotSystem = new RobotSystem();
    robotSystem.init(hardwareMap);

    createTrajectories(drive, initialPose);

    Action ActionHiveUp = new SequentialAction(ActionShoot(robotSystem), ActionParkTwoShots);
    Action ActionHiveDown =
        new SequentialAction(ActionMoveSecondPosition, ActionShoot(robotSystem), ActionPark);

    telemetry.addData("Status", "Initialized. Ready to Start");
    telemetry.update();
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
                    ActionShootOnePosition,

                // 2. shooter spin (switches targetVel to SHOOTER_VELOCITY)
                robotSystem.shooter.spinAction(SHOOTER_VELOCITY),

                // 3. wait (time for shooter to accelerate to speed)
                // TODO: wait until shooter get to vel
                new SleepAction(WAIT_SHOOTER_SPIN),

                // 4. stopper down
                    ActionShoot(robotSystem),
                ActionIntakeFromGarden,
                new SleepAction(1),
                ActionScoreIntakeFromGurden,
                new ConditionalAction(()->robotSystem.aprilTag.isRedAudience(), ActionHiveUp, ActionHiveDown)),
                robotSystem.intake.stopAction(),
                robotSystem.shooter.stopAction()
        ));


  }

  public void createTrajectories(MecanumDrive drive, Pose2d initialPose) {
    ShootOnePosition =
        drive.actionBuilder(initialPose).lineToXLinearHeading(54, Math.toRadians(330));

    IntakeFromGarden =
        ShootOnePosition.endTrajectory()
            .fresh()
            .setTangent(Math.toRadians(270))
            .splineToLinearHeading(new Pose2d(61, -60.5, Math.toRadians(0)), Math.toRadians(0));
ScoreIntakeFromGarden=
        IntakeFromGarden.endTrajectory()
                .fresh()
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(
                        new Pose2d(54, -36, Math.toRadians(330)), Math.toRadians(90));
    ParkTwoShots =
        ScoreIntakeFromGarden.endTrajectory()
            .fresh()
            .setTangent(Math.toRadians(200))
            .splineToLinearHeading(new Pose2d(-25, -57, Math.toRadians(30)), Math.toRadians(200));

    MoveSecondPosition =
        ScoreIntakeFromGarden.endTrajectory()
            .fresh()
            .setTangent(Math.toRadians(180))
            .splineToLinearHeading(new Pose2d(54, -36, Math.toRadians(330)), Math.toRadians(90))
            .splineToSplineHeading(new Pose2d(10, -42, Math.toRadians(180)), Math.toRadians(180))
            .splineTo(new Vector2d(-41, -21), Math.toRadians(110))
            .splineTo(new Vector2d(-56, -10), Math.toRadians(180));

    Park =
        MoveSecondPosition.endTrajectory()
            .fresh()
            .setTangent(Math.toRadians(345))
            .splineToLinearHeading(new Pose2d(-43, -57, Math.toRadians(180)), Math.toRadians(275));


    ActionShootOnePosition = ShootOnePosition.build();
    ActionIntakeFromGarden = IntakeFromGarden.build();
    ActionScoreIntakeFromGurden = ScoreIntakeFromGarden.build();
    ActionMoveSecondPosition = MoveSecondPosition.build();
    ActionPark = Park.build();
    ActionParkTwoShots = ParkTwoShots.build();
  }

  public Action ActionShoot(RobotSystem robotSystem) {
    return new SequentialAction(
        robotSystem.stopper.downAction(),
        new ParallelAction(
            robotSystem.intake.collectAction(), robotSystem.transfer.transferRunningAction()),
        new SleepAction(INTAKE_TRANSFER_TIME),
        new ParallelAction(robotSystem.transfer.stopAction(), robotSystem.stopper.upAction()));
  }
}
