package com.bogueratcreations.eaftoolkit.slope;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 * Created by TheBogueRat on 6/5/2015.
 */
class SlopePagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Elev / Dist", "Elev Diff / Dist", "Convert % <-> \u00b0" };
    Context context;

    public SlopePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new SlopeFragment1();
            case 1:
                return new SlopeFragment2();
            case 2:
                return new SlopeFragment3();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT; //No of Tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

}
