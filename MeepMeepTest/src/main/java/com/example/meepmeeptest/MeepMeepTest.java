package com.example.meepmeeptest;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MeepMeepTest {

        public static void main(String[] args) throws IOException {
            MeepMeep meepMeep = new MeepMeep(800);

            RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                    // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                    .setConstraints(50, 50 , 50 ,50 ,17.5)
                    .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(62, -13, 110))
                            .lineTo(new Vector2d(10 , 56))

                            .lineToLinearHeading(new Pose2d(50 , -54 ,270))
                            .forward(15)
                            .lineToLinearHeading(new Pose2d(-54 ,-18 , 0))

                            .strafeRight(35)
                            .build());

            Image img;
            img = ImageIO.read(new File("MeepMeepTest/src/main/java/com/example/meepmeeptest/FieldPictures/DarkField_Image.png"));
            try { img = ImageIO.read(new File("MeepMeepTest/src/main/java/com/example/meepmeeptest/FieldPictures/DarkField_Image.png")); }
            catch(IOException e) {}

            meepMeep.setBackground(img)
                    .setDarkMode(true)
                    .setBackgroundAlpha(0.95f)
                    .addEntity(myBot)
                    .start();
        }
    }
