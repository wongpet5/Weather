package weather.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import weather.app.Classes.City;
import weather.app.HelperMethods.WeatherIdIcons;

public class CityListItemAdapter extends BaseAdapter
{
    List<String> citiesNames;
    Context context;
    LayoutInflater layoutInflater;

    public CityListItemAdapter(List<String> _citiesNames, Context context) {
        super();

        this.citiesNames  = _citiesNames;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return citiesNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Citylistitem clt;
        if (convertView == null)
        {
            String city = citiesNames.get(position);
            clt = new Citylistitem(context, city);
        }
        else
        {
            clt = (Citylistitem) convertView;
            clt.setNameText(citiesNames.get(position));
            clt.setImage(R.raw.airelement32);
        }

        return clt;
    }

}
