package com.bogueratcreations.eaftoolkit;

import java.util.ArrayList;

/**
 * Created by jodyroth on 7/1/15.
 */
public class Images {

    private ArrayList<Integer> imageID;

    public Images() {
        imageID = new ArrayList<>();
        imageID.add(R.drawable.flols);
        imageID.add(R.drawable.forks);
        imageID.add(R.drawable.calendar);
        imageID.add(R.drawable.slope);
    }

    public ArrayList getImageItem() {
        return imageID;
    }
}
