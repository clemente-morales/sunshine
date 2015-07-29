package lania.edu.mx.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements FetchWeatherTask.WeatherListener {

    private static final String TAG = ForecastFragment.class.getSimpleName();
    public static final String WEATHER_DATA = "weatherData";

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
        addEventsToControls();
        new FetchWeatherTask(this, getActivity(), null).execute(getZipCode(), getUnitType());
        return view;
    }


    private void addEventsToControls() {
        getForecastListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter = (ArrayAdapter<String>) parent.getAdapter();
                String data = adapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(WEATHER_DATA, data);
                getActivity().startActivity(intent);
            }
        });
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
            new FetchWeatherTask(this, getActivity(), null).execute(getZipCode(), getUnitType());
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchWeatherTask(this, getActivity(), null).execute(getZipCode(), getUnitType());
    }

    private String getZipCode() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        return preferences.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_deafult));
    }

    private String getUnitType() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        return preferences.getString(getString(R.string.pref_unitType_key), getString(R.string.pref_unitType_default));
    }

    @Override
    public void onUpdate(List<String> data) {
        Log.d(TAG, "" + data);

        if (data != null) {
            loadData(data);
        }
    }

    private void loadData(List<String> data) {
        if (adapter != null) {
            adapter.clear();
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, data);
        getForecastListView().setAdapter(adapter);
    }

    private ListView getForecastListView() {
        return (ListView) view.findViewById(R.id.listview_forecast);
    }
}
