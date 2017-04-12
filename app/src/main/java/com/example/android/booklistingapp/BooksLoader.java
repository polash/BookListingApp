package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by sksho on 11-Apr-17.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private String mUrl;

    public BooksLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }



    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Books> books = QueryUtils.fetchBooksData(mUrl);
        return books;
    }
}
