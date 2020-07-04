package com.tomschammo.coursetimer.HelperClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tomschammo.coursetimer.Fragments.HistoryFragment;
import com.tomschammo.coursetimer.Fragments.StopWatchFragment;


/**
 *
 * Adapter for the pager.
 *
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {

            case 0:
                return new StopWatchFragment();

            case 1:
                return new HistoryFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
