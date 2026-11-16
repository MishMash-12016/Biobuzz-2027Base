//package org.firstinspires.ftc.teamcode.CommandGroup;
//
//
//import com.seattlesolvers.solverslib.command.Command;
//
//import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//
//import org.firstinspires.ftc.teamcode.Subsystem.Intake;
//
//
//public class IntakeCommand {
//
//    Intake intake = new Intake();
//
//
//    public Command IntakeCommands() {
//        return new ParallelCommandGroup(
//                intake.Intakerunning(),
//                new WaitCommand(300),
//                intake.IntakeStop()
//        );
//
//    }
//}