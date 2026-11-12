package org.firstinspires.ftc.teamcode.Subsystem;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * מחלקת Shooter מלאה ועצמאית (ללא SolversLib וללא קבצים חיצוניים).
 * קוראת מהירות מהאינקודר של ShooterL בלבד, ונותנת כוח זהה לשני המנועים.
 */
@Config // חובה! גורם לתיקייה "Shooter" להופיע בתפריט ה-Configuration בדשבורד
public class Shooter {


    private DcMotorEx shooterL;
    private DcMotorEx shooterR;
    private FtcDashboard dashboard;

    // =========================================================================
    // משתנים לכיוון בזמן אמת מה-FTC Dashboard (חייבים להיות public static!)
    // בדפדפן פתחו את התיקייה "Shooter", שנו ערך ולחצו Save (או Enter)
    // =========================================================================
    public static double targetVelocity = 1200; // מהירות יעד ב-Ticks לשנייה
    public static double KP = 0;            // מתקן נפילות מהירות כשכדור נורה
    public static double KI = 0.0;              // לרוב נשאר 0 בשוטר
    public static double KD = 0.0;              // מרסן רעידות
    public static double KF = 0;           // הקבוע הראשי ששומר על מהירות קבועה

    // משתנים פנימיים לחישוב ה-PID
    private ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;
    private double integralSum = 0;
    private double currentPower = 0;

    /**
     * אתחול המנועים - לקרוא לפונקציה הזו פעם אחת ב-init של ה-OpMode
     */
    public void init(HardwareMap hardwareMap) {
        dashboard = FtcDashboard.getInstance();

        shooterL = hardwareMap.get(DcMotorEx.class, "ShooterL");
        shooterR = hardwareMap.get(DcMotorEx.class, "ShooterR");

        // איפוס האינקודר של המנוע השמאלי
        shooterL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // שני המנועים על RUN_WITHOUT_ENCODER כדי שנשלוט בכוח שלהם ישירות ונשווה ביניהם,
        // אבל עדיין נוכל לקרוא את המהירות מהאינקודר שמחובר ל-ShooterL!
        shooterL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterL.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        timer.reset();
    }

    /**
     * מפעיל את השוטר עם בקרת ה-PIDF ושולח נתונים לגרף בדשבורד.
     * חובה לקרוא לפונקציה הזו בכל מחזור לולאה (בתוך ה-while) ב-OpMode!
     */
    public void spin() {
        // 1. קריאת המהירות הנוכחית מהאינקודר של המנוע השמאלי
        double currentVelocity = shooterL.getVelocity();

        // 2. חישוב הזמן שעבר מהלופ הקודם
        double dt = timer.seconds();
        timer.reset();
        if (dt <= 0 || dt > 0.5) {
            dt = 0.02; // הגנה למקרה שהפונקציה לא נקראה הרבה זמן
        }

        // 3. חישוב ה-PIDF
        if (targetVelocity <= 0) {
            currentPower = 0;
            integralSum = 0;
            lastError = 0;
        } else {
            double error = targetVelocity - currentVelocity;
            double derivative = (error - lastError) / dt;
            integralSum += error * dt;
            lastError = error;

            // משוואת ה-PIDF המלאה
            currentPower = (KP * error) + (KI * integralSum) + (KD * derivative) + (KF * targetVelocity);

            // הגבלת הכוח לטווח חוקי בין 0.0 ל-1.0
            currentPower = Math.max(0.0, Math.min(1.0, currentPower));
        }

        // 4. מתן כוח זהה לשני המנועים
        shooterL.setPower(currentPower);
        shooterR.setPower(currentPower);

        // 5. שליחת הנתונים לגרף ב-FTC Dashboard
        sendTelemetry(currentVelocity);
    }

    /**
     * עצירת השוטר ואיפוס החישובים
     */
    public void stop() {
        currentPower = 0;
        integralSum = 0;
        lastError = 0;
        shooterL.setPower(0);
        shooterR.setPower(0);

        sendTelemetry(shooterL.getVelocity());
    }

    /**
     * פונקציית עזר ששולחת את הנתונים ישירות לדשבורד (מחליפה את הצורך ב-MultipleTelemetry)
     */
    private void sendTelemetry(double currentVelocity) {
        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Target Velocity", targetVelocity);
        packet.put("Current Velocity L", currentVelocity);
        packet.put("Motor Power", currentPower);
        packet.put("Active Gains", String.format("P:%.5f I:%.5f D:%.5f F:%.5f", KP, KI, KD, KF));
        dashboard.sendTelemetryPacket(packet);
        // אין כאן updateConfig() כדי שהדשבורד לא יאפס לכם את הערכים!
    }
}