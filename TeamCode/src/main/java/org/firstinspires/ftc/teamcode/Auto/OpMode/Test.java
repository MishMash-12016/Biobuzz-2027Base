package org.firstinspires.ftc.teamcode.Auto.OpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.Auto.Trajectory;
import org.firstinspires.ftc.teamcode.RobotSystem;

/**
 * תוכנית אוטונומית לדוגמה באמצעות Pedro Pathing 3.x (FSM סטנדרטי, ללא SolversLib).
 */
@Autonomous(name = "Pedro Auto Test", group = "Autonomous")
public class Test extends OpMode {

    private RobotSystem robot;
    private int pathState = 0;

    @Override
    public void init() {
        robot = new RobotSystem();
        robot.init(hardwareMap);
        robot.follower.setPose(Trajectory.START_POSE);

        telemetry.addLine("Initialized. Ready to start!");
        telemetry.update();
    }

    @Override
    public void start() {
        setPathState(0);
    }

    @Override
    public void loop() {
        robot.follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("X", robot.follower.pose().x());
        telemetry.addData("Y", robot.follower.pose().y());
        telemetry.addData("Heading (Deg)", Math.toDegrees(robot.follower.pose().heading()));
        telemetry.update();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                robot.follower.follow(Trajectory.toScorePath());
                setPathState(1);
                break;
            case 1:
                if (!robot.follower.isBusy()) {
                    robot.armExemple.setTargetPosition(1000);
                    robot.follower.follow(Trajectory.toParkPath());
                    setPathState(2);
                }
                break;
            case 2:
                if (!robot.follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
    }
}
