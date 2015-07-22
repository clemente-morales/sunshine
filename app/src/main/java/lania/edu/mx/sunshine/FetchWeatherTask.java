package lania.edu.mx.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by clemente on 7/21/15.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, List<String>> {
    private static final String TAG = FetchWeatherTask.class.getSimpleName();

    private WeatherListener wheatherListener;

    public interface WeatherListener {
        void onUpdate(List<String> data);
    }


    public FetchWeatherTask(WeatherListener wheatherListener) {
        this.wheatherListener = wheatherListener;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        String postalCode = params[0];
        String unitType = params[1];
        return getRealData(postalCode, unitType);
    }

    @Override
    protected void onPostExecute(List<String> data) {
        wheatherListener.onUpdate(data);
    }

    private List<String> getRealData(String postalCode, String unitType) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String forecastJson = "";
        int numberOfDays = 7;
        List<String> result = null;
        try {
            // "http://api.openweathermap.org/data/2.5/forecast/daily?q=06300&mode=json&units=metric&cnt=7"
            Uri uri = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/daily").buildUpon()
                    .appendQueryParameter("q", postalCode).appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", unitType).appendQueryParameter("cnt", "" + numberOfDays).build();

            Log.d(TAG, uri.toString());
            URL url = new URL(uri.toString());

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();

            while (line != null) {
                buffer.append(line);
                buffer.append("\n");
                line = reader.readLine();
            }

            if (buffer.length() == 0)
                return null;

            forecastJson = buffer.toString();
            Log.d(TAG, forecastJson);
            result = new ArrayList<String>(Arrays.asList(WeatherDataParser.getWeatherDataFromJson(forecastJson, numberOfDays)));

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }


        return result;

    }
}
