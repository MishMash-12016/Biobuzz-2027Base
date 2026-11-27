package com.example.meepmeeptest;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AutoRedShootGardenParkMeepMeep {

  public static void main(String[] args) throws IOException {
    MeepMeep meepMeep = new MeepMeep(800);

    RoadRunnerBotEntity myBot =
        new DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(50, 50, 50, 50, 17.5)
            .followTrajectorySequence(drive ->
                drive
                    .trajectorySequenceBuilder(new Pose2d(62, -13, 0))
                    // Intake from garden
                    .setTangent(Math.toRadians(230))
                    .splineToLinearHeading(
                        new Pose2d(61, -60.5, Math.toRadians(0)), Math.toRadians(0))

                    // Go to score Gurden Intake
                    .setTangent(Math.toRadians(180))
                    .splineToLinearHeading(
                        new Pose2d(62, -13, Math.toRadians(0)), Math.toRadians(50))
                    .waitSeconds(0.3)
                    //
                    //                            // Go Park
                    .setTangent(Math.toRadians(240))
                    .splineToLinearHeading(new Pose2d(-24, -56, 0), Math.toRadians(180))

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
