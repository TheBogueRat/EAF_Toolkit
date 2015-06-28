package com.bogueratcreations.eaftoolkit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;


/**
 * A placeholder fragment containing a simple view.
 */
public class FLOLSFragment extends Fragment {

    EditText etH2e, etGSA, etDistCL, etDistTD, etDistBeyond;
    EditText etCellHt, etElevBASP, etElevRWSP, etElevTD;
    EditText etResultBAPH, etResultSPDist, etResultRWPH, etResultRoll;
    Button btnGetResults;

    public FLOLSFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.fragment_flols, container, false);

        etH2e = (EditText)V.findViewById(R.id.etH2E);
        etGSA = (EditText)V.findViewById(R.id.etGSA);
        etDistCL = (EditText)V.findViewById(R.id.etDistCL);
        etDistTD = (EditText)V.findViewById(R.id.etDistTD);
        etDistBeyond = (EditText)V.findViewById(R.id.etDistBeyond);

        etCellHt = (EditText)V.findViewById(R.id.etCellHt);
        etElevBASP = (EditText)V.findViewById(R.id.etElevBASP);
        etElevRWSP = (EditText)V.findViewById(R.id.etElevRWSP);
        etElevTD = (EditText)V.findViewById(R.id.etElevTD);
        btnGetResults = (Button)V.findViewById(R.id.btnResults);

        etResultBAPH = (EditText)V.findViewById(R.id.etBAPH);
        etResultSPDist = (EditText)V.findViewById(R.id.etSPDist);
        etResultRWPH = (EditText)V.findViewById(R.id.etRWPH);
        etResultRoll = (EditText)V.findViewById(R.id.etRoll);

        btnGetResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Declare and instantiate fm form
                Double dblH2E = Double.parseDouble(etH2e.getText().toString()) * 12.0;      // Hook2Eye Value in inches
                Double GSA = Double.parseDouble(etGSA.getText().toString());         // Glide Slope Angle as entered
                Double distCL = Double.parseDouble(etDistCL.getText().toString()) * 12.0;      // Distance to Centerline in inches
                Double distFLOLS = Double.parseDouble(etDistTD.getText().toString());   // Distance of FLOLS from Cable
                Double distBeyond = Double.parseDouble(etDistBeyond.getText().toString());  // Distance of TD from Cable
                Double distTD = (distFLOLS + distBeyond) * 12.0;      // Distance fm FLOLS to touch down point in inches (distFLOLS + distBeyond)
                Double cellHt = Double.parseDouble(etCellHt.getText().toString());      // Height of Center Cell above pad
                Double elevTD = Double.parseDouble(etElevTD.getText().toString());      // Elevation at Touch Down Point
                Double elevSP = Double.parseDouble(etElevBASP.getText().toString());      // Elevation at Basic Angle Pole Check location
                Double elevPC = Double.parseDouble(etElevRWSP.getText().toString());      // Elevation at Runway Pole Check location
    Log.d("DEBUG", "Declared and Instantiated variables.");
                // Calculate Roll Angle in Units
                Double tangSA = Math.tan(GSA * (Math.PI / 180.0));
                Double beamTD = (tangSA * distTD) + (cellHt - elevTD);
                Double beamHook = dblH2E - beamTD;
                Double angleRoll = (Math.atan(beamHook / distCL)) * (180 / Math.PI);
                if ((angleRoll > 7.5)||(angleRoll < -7.5)) {
                    Toast.makeText(getActivity(), "Width must be divisible by 6 and Length by 2; and greater than zero!", Toast.LENGTH_SHORT).show();
                    return;  //  Stop processing, out of limits
                }
                // Calculate pole height for Basic GSA
                Double dblPH = (1800.0 * tangSA) + cellHt - elevSP;
                dblPH = Math.round(dblPH * 10.0) / 10.0;

                // Calculate roll angle test procedure distance to PC forward of FLOLS
                // uses 20 degree plane intersect with runway centerline
                Double distPC = (distCL / 12.0) / (Math.tan(20.0 * Math.PI / 180.0));

                // Calculate Roll Angle Check Pole Height in feet
                Double RAPH = (12.0 * distPC * tangSA);
                RAPH = RAPH + (distCL * (Math.tan(angleRoll * Math.PI / 180.0)));
                RAPH = RAPH + cellHt - elevPC;
                RAPH = Math.round(RAPH * 10.0 / 12.0) / 10.0;

                // Display Results
                etResultBAPH.setText(Double.toString(dblPH) + " inches");
                etResultRWPH.setText(Double.toString(RAPH) + " feet");
                etResultSPDist.setText(Double.toString(Math.round(distPC * 10.0) / 10.0) + " feet");
                etResultRoll.setText(Double.toString(Math.round((7.5 + angleRoll) * 100.0) / 100.0) + " units");
            }
        });

        return V;
    }

}
