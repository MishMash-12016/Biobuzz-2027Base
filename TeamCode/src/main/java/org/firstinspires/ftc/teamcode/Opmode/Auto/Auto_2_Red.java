package org.firstinspires.ftc.teamcode.Opmode.Auto;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Librarys.Constants;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;

import com.pedropathing.paths.Path;



@Autonomous
public class Auto_2_Red extends OpMode {

    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(88.1586, 8, 90);
    private final Pose intakePose = poseFactory.of(48.2093, 8.1634, 180);
    private final Pose shootPose = poseFactory.of(60.0, 48.0, 90);

    // 1. הגדרת המערכות שלך
    private Intake intake;
    private Shooter shooter;

    public Path toIntakePath() {
        return Paths.line(start, intakePose).linear(start, intakePose);
    }

    public Path toShootPath() {
        return Paths.line(intakePose, shootPose).linear(intakePose, shootPose);
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(start);

        // אתחול המערכות
        intake = new Intake();
        shooter = new Shooter();

        intake.init(hardwareMap);
        shooter.init(hardwareMap);
    }

    @Override
    public void start() {


    }

    @Override
    public void loop() {
        // ה-loop נשאר נקי לחלוטין!
        follower.update();
        Scheduler.execute();
    }
}