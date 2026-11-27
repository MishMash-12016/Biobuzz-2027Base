package com.example.meepmeeptest;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeep {

  public static void main(String[] args) throws IOException {
    org.rowlandhall.meepmeep.MeepMeep meepMeep = new org.rowlandhall.meepmeep.MeepMeep(800);

    RoadRunnerBotEntity myBot =
        new DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(50, 50, 50, 50, 17.5)
            .followTrajectorySequence(drive ->
                drive
                    .trajectorySequenceBuilder(new Pose2d(62, -37, Math.toRadians(0)))
                    // Intake from garden
                    .lineToLinearHeading(new Pose2d(54, -37, Math.toRadians(330)))
                    .waitSeconds(1)


                   .setTangent(Math.toRadians(270))
                   .splineToLinearHeading(
                      new Pose2d(61, -60.5, Math.toRadians(0)), Math.toRadians(0))
                   .setTangent(Math.toRadians(180))
                               .splineToLinearHeading(
                       new Pose2d(54, -36, Math.toRadians(330)), Math.toRadians(90))

                        .setTangent(Math.toRadians(200))
                        .splineToLinearHeading(
                                new Pose2d(-25, -57, Math.toRadians(30)), Math.toRadians(200))
                    //
                    //                            // Go Park
                   // .splineToSplineHeading(
                      //  new Pose2d(10, -42, Math.toRadians(180)), Math.toRadians(180))
                   // .splineTo(new Vector2d(-41, -21), Math.toRadians(110))
                   // .splineTo(
                  //      new Vector2d(-56, -10), Math.toRadians(180))
//                        .setTangent(Math.toRadians(345))
//                        .splineToLinearHeading(
//                                new Pose2d(-43, -57, Math.toRadians(180)), Math.toRadians(275))


                    .build());

    Image img;
    img =
        ImageIO.read(
            new File(
                "MeepMeepTest/src/main/java/com/example/meepmeeptest/FieldPictures/DarkField_Image.png"));
    try {
      img =
          ImageIO.read(
              new File(
                  "MeepMeepTest/src/main/java/com/example/meepmeeptest/FieldPictures/DarkField_Image.png"));
    } catch (IOException e) {
    }

    meepMeep
        .setBackground(img)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(myBot)
        .start();
  }
}
