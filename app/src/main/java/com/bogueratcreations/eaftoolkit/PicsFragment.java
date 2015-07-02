package com.bogueratcreations.eaftoolkit;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogueratcreations.eaftoolkit.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PicsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PicsFragment newInstance(String param1, String param2) {
        PicsFragment fragment = new PicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PicsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pics, container, false);
        ((TextView)v.findViewById(R.id.tvPics)).setText(mParam1);

        // Populate Image View
        int imageResource = getResources().getIdentifier(mParam2, null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        ((ImageView)v.findViewById(R.id.ivPics)).setImageDrawable(res);
        return v;
    }


}
