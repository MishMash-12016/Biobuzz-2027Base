package org.firstinspires.ftc.teamcode.Auto;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * מחלקה לניהול והגדרת מסלולי Pedro Pathing 3.x עבור תוכניות האוטונומי.
 */
public class Trajectory {

    // נקודות מפתח לדוגמה על המגרש (באינצ'ים ורדיאנים)
    public static final Pose START_POSE = new Pose(9, 111, Math.toRadians(0));
    public static final Pose SCORE_POSE = new Pose(38, 111, Math.toRadians(0));
    public static final Pose PARK_POSE  = new Pose(60, 96, Math.toRadians(90));

    /**
     * מסלול קו ישר מנקודת ההתחלה לנקודת הניקוד
     */
    public static Path toScorePath() {
        return Paths.line(START_POSE, SCORE_POSE)
                .constant(START_POSE.heading());
    }

    /**
     * מסלול מנקודת הניקוד לאזור החניה
     */
    public static Path toParkPath() {
        return Paths.line(SCORE_POSE, PARK_POSE)
                .linear(SCORE_POSE.heading(), PARK_POSE.heading());
    }
}
