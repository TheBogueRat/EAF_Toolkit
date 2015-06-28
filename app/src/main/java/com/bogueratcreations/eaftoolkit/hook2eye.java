package com.bogueratcreations.eaftoolkit;

/**
 * Created by TheBogueRat on 6/27/2015.
 */
public class hook2eye {

    public static String getRollAngle(String glideSlopeAngle, String distanceFmTouchDownPt, String cellHeight, String elevAtTouchDown, String distFmCenterline, String hook2eye) {
        // Declare and instantiate fm form
        Double GSA = Double.parseDouble(glideSlopeAngle); //Glide Slope Angle as entered
        Double distTD = Double.parseDouble(distanceFmTouchDownPt);  //Distance fm FLOLS to touch down point in inches
        Double cellHt = Double.parseDouble(cellHeight);  //Height of Center Cell above pad
        Double elevTD = Double.parseDouble(elevAtTouchDown);
        Double elevSP = 0.0;
        Double elevPC = 0.0;
        Double distCL = Double.parseDouble(distFmCenterline);
        Double dblH2E = Double.parseDouble(hook2eye);

        // Calculate Roll Angle in Units
        Double tangSA = Math.tan(GSA * (Math.PI / 180.0));
        //tangSA = tangSA.toFixed(10);
        Double beamTD = (tangSA * distTD) + (cellHt - elevTD);
        Double beamHook = dblH2E - beamTD;
        Double angleRoll = (Math.atan(beamHook / distCL)) * (180 / Math.PI);
        if ((angleRoll > 7.5)||(angleRoll < -7.5)) {
        //    alert("Roll exceedes mechanical limits.");
            return null;
        }
        // Calculate pole height for Basic GSA
        Double dblPH = (1800 * tangSA) + cellHt - elevSP;
        dblPH = Math.round(dblPH * 10.0) / 10.0;

        // Calculate roll angle test procedure distance to PC forward of FLOLS
        // uses 20 degree plane intersect with runway centerline
        Double distPC = (distCL / 12.0) / (Math.tan(20.0 * Math.PI / 180.0));

        // Calculate Roll Angle Check Pole Height in feet
        Double RAPH = (12 * distPC * tangSA);
        RAPH = RAPH + (distCL * (Math.tan(angleRoll * Math.PI / 180)));
        RAPH = RAPH + cellHt - elevPC;
        RAPH = Math.round(RAPH * 10.0 / 12.0) / 10.0;
        /*
        // TODO: Display Results
        document.getElementById("txtUnits").innerHTML = Math.round((7.5 + angleRoll) * 100.0) / 100.0 + " units";
        document.getElementById("SPBA").innerHTML = dblPH + " inches";
        document.getElementById("txtSPLoc").innerHTML = Math.round(distPC * 10.0) / 10.0 + " feet";
        document.getElementById("txtSPHt").innerHTML = RAPH + " feet";
        */
        return "Success";
    }
}
