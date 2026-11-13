package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Subsystem.ArmExemple;
import org.firstinspires.ftc.teamcode.Subsystem.DriveTrain;
import org.firstinspires.ftc.teamcode.Subsystem.Intake;
import org.firstinspires.ftc.teamcode.Subsystem.Shooter;
import org.firstinspires.ftc.teamcode.Subsystem.Stopper;
import org.firstinspires.ftc.teamcode.Subsystem.Transfer;

/**
 * מחלקה מרכזית המרכזת ומאתחלת את כל ה-Subsystems של הרובוט,
 * כולל ה-Follower של Pedro Pathing.
 */
public class RobotSystem {
    public DriveTrain driveTrain;
    public ArmExemple armExemple;
    public Intake intake;
    public Shooter shooter;
    public Stopper stopper;
    public Transfer transfer;
//    public Follower follower;

    public FtcDashboard ftcDashboard;

    public void init(HardwareMap hardwareMap) {
        driveTrain = new DriveTrain();
        driveTrain.init(hardwareMap);

        armExemple = new ArmExemple();
        armExemple.init(hardwareMap);

        intake = new Intake();
        intake.init(hardwareMap);

        shooter = new Shooter();
        shooter.init(hardwareMap);

        stopper = new Stopper();

        transfer = new Transfer();
        transfer.init(hardwareMap);



    }
}
