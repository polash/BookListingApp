package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by sksho on 11-Apr-17.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {


    final private String url;

    public BooksLoader(Context context, String url) {
        super(context);
        this.url = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        // Perform the network request, parse the response, and extract a list of Books.
        List<Books> books = QueryUtils.fetchBooksData(url);
        return books;
    }
}
