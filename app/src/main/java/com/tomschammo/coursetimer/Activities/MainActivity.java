package com.tomschammo.coursetimer.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tomschammo.coursetimer.HelperClasses.PagerAdapter;
import com.tomschammo.coursetimer.R;


import lombok.Setter;

/**
 * Main Activity of CourseTimer app. <br>
 *
 * Provides the TabLayout on which the {@link com.tomschammo.coursetimer.Fragments.StopWatchFragment} and {@link com.tomschammo.coursetimer.Fragments.HistoryFragment} are built upon.
 *
 */

@Setter
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        ViewPager viewPager = findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

    }


}