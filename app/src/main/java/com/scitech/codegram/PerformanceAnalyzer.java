package com.scitech.codegram;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */

public class PerformanceAnalyzer extends Fragment {


    public PerformanceAnalyzer() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_performance_analyzer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = (TabLayout)getView().findViewById(R.id.performance_tabLayout);
        viewPager = (ViewPager)getView().findViewById(R.id.performance_viewPager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFollowFragment(new TotalAnalysis(), "Total Analysis");
        adapter.addFollowFragment(new DailyBasis(), "Datewise Analysis");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
