package lania.edu.mx.sunshine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

         adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, getFakeData());

        ListView listView = (ListView) view.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        return view;
    }

    @NonNull
    private List<String> getFakeData() {
        List<String> data = new ArrayList<String>();

        data.add("today - Sunny - 88/63");
        data.add("tomorrow - Foggy - 70/40");
        return data;
    }
}
