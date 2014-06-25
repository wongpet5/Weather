package weather.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import weather.app.Classes.Weather.CurrentWeather;
import weather.app.HelperMethods.FutureWeather_XMLParse;

public class DemoCollectionPagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> fragments;
    private Fragmentfutureforecast fragment = new Fragmentfutureforecast();
    private One one = new One();

    public DemoCollectionPagerAdapter(FragmentManager fm)
    {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
        fragments.add(fragment);
        fragments.add(one);


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

    public void SetClothingForecast(CurrentWeather currentWeather)
    {
        one.SetClothing(currentWeather);
    }

/*
    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }   */
}