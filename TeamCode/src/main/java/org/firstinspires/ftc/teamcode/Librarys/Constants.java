package org.firstinspires.ftc.teamcode.Librarys;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

        public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
            c.frontLeftName.set("fl");
            c.frontRightName.set("fr");
            c.backLeftName.set("bl");
            c.backRightName.set("br");
            c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
            c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
            c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
            c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        });

        public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
            c.name.set("pinpoint");
            c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
            c.xPodOffset.set(-0.49245684165654224);
            c.yPodOffset.set(2.210162155271515);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3660734334167315);
                Controller secondaryTranslationalForward = Controller.proportional(0.13525435198518995);
                Controller primaryTranslationalLateral = Controller.proportional(0.49614268422004837);
                Controller secondaryTranslationalLateral = Controller.proportional(0.18331146464262463);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.012660408661804843));
                c.brake.set(Controller.proportionalFeedforward(0.010761347362534117));

                c.headingFeedback.set(Controller.proportional(6.589937946374783));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.049769835095665085, 0.006294877226346965));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09414301948077962, 0.06948878124569434));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.001546418776585081, 0.0015589965473943662));

                c.maxAchievableForwardVelocity.set(75.23188183387535);
                c.maxAchievableStrafeVelocity.set(60.760354654972886);
                c.naturalForwardDeceleration.set(31.885505715116093);
                c.naturalStrafeDeceleration.set(66.05638494024254);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

}