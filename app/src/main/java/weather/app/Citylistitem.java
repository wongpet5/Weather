package weather.app;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import weather.app.Classes.City;

public class Citylistitem extends RelativeLayout {

    private TextView cName;
    private TextView cTemp;
    private ImageView iView;

    public Citylistitem(Context context, String city) {

        super(context);

        cName = new TextView(context);
        cName.setText(city);
        cName.setTextSize(19);
        cName.setTextColor(Color.WHITE);
        cName.setTypeface(Typeface.SANS_SERIF);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        relativeLayoutParams.setMargins(20, 0, 0, 0);
        addView(cName, relativeLayoutParams);


        LinearLayout linLayout = new LinearLayout(context);

        iView = new ImageView(context);
        iView.setImageResource(R.raw.airelement32);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 125, 0);
        linLayout.addView(iView, params);

        cTemp = new TextView(context);
        cTemp.setText("21");
        cTemp.setTextSize(19);
        cTemp.setTextColor(Color.WHITE);
        cTemp.setTypeface(Typeface.SANS_SERIF);
        linLayout.addView(cTemp);

        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        viewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        viewParams.setMargins(0, 0, 20, 0);
        addView(linLayout, viewParams);

    }

    public void setNameText(String name) {
        cName.setText(name);
    }

    public void setTempText(String temp) {
        cTemp.setText(temp);
    }

    public void setImage(int id) {
        iView.setImageResource(id);
    }
}
