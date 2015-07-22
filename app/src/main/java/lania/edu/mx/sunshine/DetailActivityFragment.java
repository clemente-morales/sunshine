package lania.edu.mx.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        TextView textView = (TextView) getActivity().findViewById(R.id.detailTextView);
        textView.setText(getWeatherData());
    }

    private String getWeatherData() {
        Intent intent = getActivity().getIntent();
        return intent.getStringExtra(ForecastFragment.WEATHER_DATA);
    }
}
