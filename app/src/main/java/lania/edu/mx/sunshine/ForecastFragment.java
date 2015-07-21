package lania.edu.mx.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements FetchWeatherTask.WeatherListener {

    private static final String TAG = ForecastFragment.class.getSimpleName();

    private ArrayAdapter<String> adapter;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_main, container, false);
        new FetchWeatherTask(this).execute("06300");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.refresh_menu_item) {
            new FetchWeatherTask(this).execute("06300");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdate(List<String> data) {
        Log.d(TAG, "" + data);

        if (data!=null) {
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, data);

            ListView listView = (ListView) view.findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);
        }
    }
}
