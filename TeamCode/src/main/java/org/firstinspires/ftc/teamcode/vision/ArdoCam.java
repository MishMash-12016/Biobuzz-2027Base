package org.firstinspires.ftc.teamcode.vision;

public class ArdoCam {
    double tx=0;
    double ty=0;
 public void ArdoCam(double tx, double ty){
     this.tx=tx;
     this.ty=ty;
 }

    public double getTx() {
        return tx;
    }

    public double getTy() {
        return ty;
    }
}

