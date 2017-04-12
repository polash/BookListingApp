package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {
    public static final String LOG_TAG = BooksListActivity.class.getName();
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private String searchURL;
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int LOADER_ID = 1;


    private BooksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_list_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);


        mAdapter = new BooksAdapter(this, new ArrayList<Books>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booksListView.setAdapter(mAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Books currentBooks = mAdapter.getItem(position);

                Uri booksUri = Uri.parse(currentBooks.getPreviewLink());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, booksUri);

                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        // Create a new loader for the given URL
        return new BooksLoader(this, searchURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> booksList) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (booksList != null && !booksList.isEmpty()) {
            mAdapter.addAll(booksList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    public void onSearchIconClicked(View view) {
        EditText searchText = (EditText) findViewById(R.id.input_text);

        //Get inputManager
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //Hide Keyboard
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //If network connectivity exists then search for books
        if (hasNetworkConnectivity(this)) {
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);


            //Clear text in emptyView since we are initiating a new search
            mEmptyStateTextView.setText("");

            //Remove all spaces from user inputted text and new lines
            searchURL = String.valueOf(searchText.getText());
            searchURL = searchURL.replace(" ", "");
            searchURL = searchURL.replace("\n", "");


            if (getLoaderManager().getLoader(LOADER_ID) == null) {
                getLoaderManager().initLoader(LOADER_ID, null, this);
            } else {
                mAdapter.clear();
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            }

        } else {
            //No Network connectivity. Clear adapter and let user know
            mAdapter.clear();
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    //Checks Network Connectivity
    public boolean hasNetworkConnectivity(Context context) {
        //Init connectivity manager which is used to check if we have internet access
        ConnectivityManager check = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get network information
        NetworkInfo networkInfo = check.getActiveNetworkInfo();

        return networkInfo != null;
    }
}
