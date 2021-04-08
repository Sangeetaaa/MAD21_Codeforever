package com.scitech.codegram;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> followFragments = new ArrayList<>();
    private final List<String> followTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return followFragments.get(position);
    }

    @Override
    public int getCount() {
        return followTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return followTitles.get(position);
    }

    public void addFollowFragment(Fragment fragment, String title)
    {
        followFragments.add(fragment);
        followTitles.add(title);
    }
}
