package weather.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import weather.app.HelperMethods.FutureWeather_XMLParse;

public class DemoCollectionPagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> fragments;
    private Fragmentfutureforecast fragment = new Fragmentfutureforecast();


    public DemoCollectionPagerAdapter(FragmentManager fm)
    {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
        fragments.add(fragment);
        fragments.add(new One());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void SetFutureForecaseFragment(FutureWeather_XMLParse futureXMLParse)
    {
        fragment.setFragmentControlData(futureXMLParse);
    }

/*
    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }   */
}