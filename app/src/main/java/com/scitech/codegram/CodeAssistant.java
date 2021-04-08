package com.scitech.codegram;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class CodeAssistant extends Fragment {


    public CodeAssistant() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_assistant, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = (TabLayout)getView().findViewById(R.id.assistant_tabLayout);
        viewPager = (ViewPager)getView().findViewById(R.id.assistant_viewPager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFollowFragment(new CodeInfo(), "Code Info");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
