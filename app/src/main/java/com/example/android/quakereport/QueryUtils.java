package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    // Create LOG_TAG for class log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractEarthquakes(String earthquakeRawJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // build up a list of Earthquake objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(earthquakeRawJSON);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i = 0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                Double mag = properties.getDouble("mag");
                Log.v("QueryUtils", "Mag = " + mag.toString());
                String place = properties.getString("place");
                Long time = properties.getLong("time");
                String url = properties.getString("url");

                earthquakes.add(new Earthquake(place, mag, time, url));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Query USGS dataset and return an {@link < com.example.android.quakereport.Earthquake>}
     * to represent a list of earthquakes
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Make HTTP request to the URL and receive JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error closing input stream ",e);
        }
        List<Earthquake> earthquake = extractEarthquakes(jsonResponse);

        return earthquake;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error creating URL ",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null then return early
        if (url== null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream= null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the connection request was successful (response code 200),
            // then read the InputStream and parse the response.
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Network connection error: Code " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Failed to open internet connection ",e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }
}