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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {

    //LOG Testing Message
    public static final String LOG_TAG = BooksListActivity.class.getName();

    // Base URI for the Books API
    private static final String BOOK_BASE_URL =
            "https://www.googleapis.com/books/v1/volumes?";
    // Parameter for the search string
    private static final String QUERY_PARAM = "q";
    // Parameter that limits search results
    private static final String MAX_RESULTS = "maxResults";

    /**
     * Constant value for the books loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int LOADER_ID = 1;

    //views
    private EditText searchInput;
    private View loadingIndicator;
    private ImageButton searchButton;
    private TextView mEmptyStateTextView;

    //BooksAdapter instance
    private BooksAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_list_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list);

        //set empty state on loading the app at start
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);

        //find the view ID
        loadingIndicator = findViewById(R.id.loading_indicator);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchInput = (EditText) findViewById(R.id.input_text);
        mAdapter = new BooksAdapter(this, new ArrayList<Books>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booksListView.setAdapter(mAdapter);

        //URL intent that takes the user the books page in a web browser
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Books currentBooks = mAdapter.getItem(position);

                Uri booksUri = Uri.parse(currentBooks.getPreviewLink());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, booksUri);

                startActivity(websiteIntent);
            }
        });

        //checking network connectivity if yes then create loader if not show warning message on screen
        if (hasNetworkConnectivity(this)) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            //Log.i(LOG_TAG, "TEST: Calling initLoader...");
            loaderManager.initLoader(LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible

            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_books);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get inputManager
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //Hide Keyboard
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //If network connectivity exists then search for books
                if (hasNetworkConnectivity(BooksListActivity.this)) {
                    //Start progessBar
                    loadingIndicator.setVisibility(View.VISIBLE);

                    mAdapter.clear();
                    getLoaderManager().restartLoader(LOADER_ID, null, BooksListActivity.this);

                } else {
                    //No Network connectivity. Clear adapter and let user know
                    mAdapter.clear();
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        String searchText = searchInput.getText().toString();

        //Clear text in emptyView since we are initiating a new search
        mEmptyStateTextView.setText("");

        //Build up query URI, limiting results to 10 items.
        Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, searchText)
                .appendQueryParameter(MAX_RESULTS, "10")
                .build();

        return new BooksLoader(this, builtURI.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> booksList) {
        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No Books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous books data
        mAdapter.clear();

        // If there is a valid list of {@link Books}s, then add them to the adapter's
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
