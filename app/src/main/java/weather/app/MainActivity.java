package weather.app;

import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.*;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.FragmentManager;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private String mTitle = "";

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.drawer_list_item, getResources().getStringArray(R.array.menu_array));
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String[] menuItems = getResources().getStringArray(R.array.menu_array);

                mTitle = menuItems[position];

                if (position != 0)
                {
                    WebViewFragment rFragment = new WebViewFragment();

                    Bundle data = new Bundle();
                    data.putInt("position", position);
                    data.putString("url", getUrl(position));
                    rFragment.setArguments(data);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, rFragment);
                    ft.commit();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
                else if (position == 0)
                {
                    TodayFragment rFragment = new TodayFragment(getApplicationContext());
                    Bundle data = new Bundle();
                    data.putInt("position", position);
                    data.putString("url", getUrl(position));
                    rFragment.setArguments(data);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.content_frame, rFragment);
                    ft.commit();
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            }
        });
    }

    protected String getUrl(int position) {
        switch (position) {
            case 0:
                return "http://javatechig.com";
            case 1:
                return "http://javatechig.com/category/android/";
            default:
                return "http://javatechig.com";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
