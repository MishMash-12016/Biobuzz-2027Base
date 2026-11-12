package org.firstinspires.ftc.teamcode.Subsystem;

import static org.firstinspires.ftc.teamcode.ShooterPARAM.KD;
import static org.firstinspires.ftc.teamcode.ShooterPARAM.KF;
import static org.firstinspires.ftc.teamcode.ShooterPARAM.KI;
import static org.firstinspires.ftc.teamcode.ShooterPARAM.KP;
import static org.firstinspires.ftc.teamcode.ShooterPARAM.targetVelocity;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import org.firstinspires.ftc.teamcode.ShooterPARAM; // ייבוא מחלקת ההגדרות

//@Config
public class Shooter extends SubsystemBase {
    private DcMotorEx ShooterL;
    private DcMotorEx ShooterR;

    private PIDFController pidController;
    private FtcDashboard dashboard;

    // Tunable via FTC Dashboard
//   public  double KP = 0.005;
//    public  double KI = 0;
//    public  double KD = 0;
//    public  double KF = 0;

//    public  double targetVelocity = 1000; // ticks/sec

    public void init(HardwareMap hardwareMap) {
        dashboard = FtcDashboard.getInstance();
        ShooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");
        ShooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        ShooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        ShooterL.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        ShooterR.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        ShooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        ShooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        // יצירת הבוס יצירת הבקר פעם אחת
        pidController = new PIDFController(KP, KI, KD, KF);
    }

    /**
     * מעדכן און-ליין את הערכים מהדשבורד ומפעיל את המנועים.
     * חובה לקרוא לפונקציה זו בתוך הלולאה הראשית (while(opModeIsActive)) כל מחזור!
     */
    public void spin() {
        // עדכון המקדמים מהמשתנים הסטטיים
        pidController.setP(KP);
        pidController.setI(KI);
        pidController.setD(KD);
        pidController.setF(KF);
        pidController.setSetPoint(targetVelocity);

        double power = pidController.calculate(ShooterL.getVelocity());
        ShooterL.setPower(power);
        ShooterR.setPower(power);

        // כפה רענון של קונפיגורציית ה-Dashboard
        FtcDashboard.getInstance().updateConfig();
    }

    public void stop() {
        ShooterL.setPower(0);
        ShooterR.setPower(0);
    }

}