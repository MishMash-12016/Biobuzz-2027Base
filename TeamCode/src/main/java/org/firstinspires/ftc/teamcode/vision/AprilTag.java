package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class AprilTag {
    private  static Limelight3A limelight;
    int blueAudienceID = 40;
    int redAudienceID = 36;
    int blueStageID =43;
    int redStageID = 31;

    public void init(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }
    public boolean seeID(int ID){
        LLResult result = limelight.getLatestResult();

        if(isResultValid(result)) {
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            for(LLResultTypes.FiducialResult fiducial: fiducials){
                int resultID = fiducial.getFiducialId();

                if(ID == resultID){
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    public static boolean isResultValid(LLResult result){
        return result != null && result.isValid();
    }
    public boolean isBlueAudience(){
        return seeID(blueAudienceID);
    }
    public boolean isRedAudience(){
        return seeID(redAudienceID);
    }
    public boolean isBlueStage(){
        return seeID(blueStageID);
    }
    public boolean isRedStage(){
        return seeID(redStageID);
    }
}
