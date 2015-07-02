package com.bogueratcreations.eaftoolkit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jodyroth on 7/2/15.
 */
public class PicsFragmentAdapter extends FragmentPagerAdapter{

    public int getCount() {
        return 3;
    }

    public Fragment getItem(int i) {
        switch (i) {
            // Add all images to this list for the Picture Gallery, (Caption, Image)
            case 0: return PicsFragment.newInstance("One", "@drawable/flols");
            case 1: return PicsFragment.newInstance("Two", "@drawable/slope");
            default: return PicsFragment.newInstance("Three", "@drawable/calendar");
        }
    }

    public PicsFragmentAdapter (FragmentManager fm) {
        super(fm);
    }
}
