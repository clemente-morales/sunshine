package lania.edu.mx.sunshine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, getRealData());

        ListView listView = (ListView) view.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        return view;
    }

    private List<String> getRealData() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String forecastJson = "";

        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=06300&mode=json&units=metric&cnt=7");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();

            while (line!=null) {
                buffer.append(line);
                buffer.append("\n");
                reader = new BufferedReader(new InputStreamReader(inputStream));
            }

            if (buffer.length()==0)
                return  null;

            forecastJson = buffer.toString();
            Log.d(TAG, forecastJson);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (connection!=null) {
                connection.disconnect();
            }

            if (reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }


        return null;

    }

    @NonNull
    private List<String> getFakeData() {
        List<String> data = new ArrayList<String>();

        data.add("today - Sunny - 88/63");
        data.add("tomorrow - Foggy - 70/40");
        return data;
    }
}
