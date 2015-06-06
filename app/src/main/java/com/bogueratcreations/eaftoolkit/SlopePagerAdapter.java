package com.bogueratcreations.eaftoolkit;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by TheBogueRat on 6/5/2015.
 */
public class SlopePagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Dist / Elev", "Dist / Elev Diff", "Convert" };
    Context context;

    public SlopePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                SlopeFragment1 sf1 = new SlopeFragment1();
                return sf1;
            case 1:
                SlopeFragment2 sf2 = new SlopeFragment2();
                return sf2;
            case 2:
                SlopeFragment3 sf3 = new SlopeFragment3();
                return sf3;
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
