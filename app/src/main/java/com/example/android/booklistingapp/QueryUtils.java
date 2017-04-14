package com.example.android.booklistingapp;

import android.text.TextUtils;
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
 * Created by sksho on 11-Apr-17.
 */

public class QueryUtils {

    //Log tag
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //default constructor
    private QueryUtils() {
    }

    //return list of books extracted from JSON
    public static List<Books> extractFromJSON(String booksJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        // Create an empty booksList
        List<Books> booksList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            String authors = "";

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            // Extract the JSONArray associated with the key called "items"
            JSONArray itemsArray = baseJsonResponse.optJSONArray("items");

            //Cycle through every item in array
            for (int i = 0; i < itemsArray.length(); i++) {

                // Get book object inside array at position i
                JSONObject currentBook = itemsArray.optJSONObject(i);

                //Get JSONObject associated with the key called "volumeinfo"
                JSONObject volumeInfo = currentBook.optJSONObject("volumeInfo");

                //Get book title associated with the key called "title"
                String title = volumeInfo.getString("title");

                //Get published date associated with the key called "publishedDate"
                String publishedDate = volumeInfo.getString("publishedDate");

                //Get preview url associated with the key called "previewLink"
                String previewLink = volumeInfo.getString("previewLink");

                //Get JSONArray associated with the key called "authors"
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");

                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        //Add all authors
                        authors = authors + authorsArray.getString(j) + " ";
                    }
                }

                // Create a new Book
                Books books = new Books(title, publishedDate, authors, previewLink);

                // Add the new book to the booksList
                booksList.add(books);

                //Clear Authors
                authors = "";
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the Books JSON results", e);
        }

        // Return the list of books
        return booksList;
    }

    public static List<Books> fetchBooksData(String requestString) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "TEST: Books Data fetch called...");

        URL url = createUrl(requestString);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link books}s
        List<Books> books = extractFromJSON(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     *
     * @param stringUrl
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            //now need to check weather the connection is sucessful or not
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
