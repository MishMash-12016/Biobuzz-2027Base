package org.firstinspires.ftc.teamcode.Subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDController;


/**
 * דוגמה ל-Subsystem של זרוע / מנגנון המשתמש ב-PIDController של SolversLib.
 * יורש מ-SubsystemBase (של SolversLib) ומאפשר עבודה גם עם Command-Based וגם ב-OpMode רגיל.
 */
public class ArmExemple extends SubsystemBase {
    private DcMotorEx armMotor;

    // קבועי PID התחלתיים (יש לכייל אותם עבור הרובוט שלכם)
    public static double kP = 0.005;
    public static double kI = 0.0;
    public static double kD = 0.0001;

    // בקר ה-PID של SolversLib
    private final PIDController pidController;

    private int targetPosition = 0;

    public ArmExemple() {
        // אתחול בקר ה-PID
        pidController = new PIDController(kP, kI, kD);
        // הגדרת טווח שגיאה מקובל (Tolerance) בטיקים של אנקודר
        pidController.setTolerance(15);
    }

    public void init(HardwareMap hardwareMap) {
        armMotor = hardwareMap.get(DcMotorEx.class, "armMotor");

        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // איפוס האנקודר ומעבר למצב RUN_WITHOUT_ENCODER כדי שבקר ה-PID של SolversLib ינהל את הכוח
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * קביעת נקודת יעד חדשה בזוויות / טיקים של אנקודר
     */
    public void setTargetPosition(int target) {
        this.targetPosition = target;
        pidController.setSetPoint(target);
    }

    /**
     * עדכון כוח המנוע לפי ה-PID.
     * נקרא בכל מחזור (Loop) של ה-OpMode.
     */
    public void update() {
        if (armMotor == null) return;

        int currentPosition = armMotor.getCurrentPosition();
        double power = pidController.calculate(currentPosition, targetPosition);

        // הגבלת הכוח בין -1.0 ל-1.0
        power = Math.max(-1.0, Math.min(1.0, power));

        armMotor.setPower(power);
    }

    /**
     * אם עובדים בגישת Command-Based של SolversLib,
     * המתודה periodic נקראת אוטומטית ע"י ה-CommandScheduler.
     */
    @Override
    public void periodic() {
        update();
    }

    /**
     * בדיקה האם הזרוע הגיעה ליעד בטווח ה-Tolerance
     */
    public boolean atTarget() {
        return pidController.atSetPoint();
    }

    public int getCurrentPosition() {
        return armMotor != null ? armMotor.getCurrentPosition() : 0;
    }

    public int getTargetPosition() {
        return targetPosition;
    }

    /**
     * עדכון מקדמי ה-PID בזמן ריצה
     */
    public void setPID(double p, double i, double d) {
        kP = p;
        kI = i;
        kD = d;
        pidController.setPID(p, i, d);
    }

    public void stop() {
        if (armMotor != null) {
            armMotor.setPower(0);
        }
    }
}
