package lania.edu.mx.sunshine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

import lania.edu.mx.sunshine.dao.WeatherContract;

/**
 * Created by clemente on 7/21/15.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, List<String>> {
    private static final String TAG = FetchWeatherTask.class.getSimpleName();
    private final Context context;

    private WeatherListener wheatherListener;

    public interface WeatherListener {
        void onUpdate(List<String> data);
    }


    public FetchWeatherTask(WeatherListener wheatherListener, Context context, Object object) {
        this.wheatherListener = wheatherListener;
        this.context = context;
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

    public int addLocation(String locationSettings, String cityName, double lat, double lon) {

        Cursor cursor = context.getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI, new String[]{
                        WeatherContract.LocationEntry._ID}, WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSettings}, null);

        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSettings);
            values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, lat);
            values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, lon);

            Uri uri = context.getContentResolver().insert(WeatherContract.LocationEntry.CONTENT_URI, values);
            Log.d(TAG, uri.toString());
            cursor.close();
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        int resultado = cursor.getInt(0);
        cursor.close();
        return resultado;
    }
}
