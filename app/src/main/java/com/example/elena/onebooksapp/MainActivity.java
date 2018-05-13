package com.example.elena.onebooksapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private String USGS_REQUEST_URL;

    private String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText myEditText = (EditText) findViewById(R.id.search_box);
        Button submitButton = (Button) findViewById(R.id.submitButton);

        //Set an OnClickListener on the Submit button, build a new String for the URL using
        //text entered by the user in the EditText field

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo onClickNetworkInfo = connMgr.getActiveNetworkInfo();

                if (onClickNetworkInfo != null && onClickNetworkInfo.isConnected()) {
                    android.app.LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    String userQuery = myEditText.getText().toString();
                    USGS_REQUEST_URL = BASE_URL + URLEncoder.encode(userQuery);
                    getLoaderManager().restartLoader(0, null, MainActivity.this);
                } else {
                    //If there is no network connection, display error
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    //Set text to error message
                    mEmptyStateTextView.setText(getString(R.string.no_internet));
                }
            }
        });

        //find a reference to the list view in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        //Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        //Set the adapter on the list view
        bookListView.setAdapter(mAdapter);

        //Get reference to Connectivity Manager
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details of the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is network connection get data
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            //If there is no network connection, display error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            //Set text to error message
            mEmptyStateTextView.setText(getString(R.string.no_internet));
        }
    }

    @Override
    public android.content.Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        //Create a new loader for the given url
        return new BookLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books) {
        //Hide loading because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //Set empty state text to display "No books found"
        mEmptyStateTextView.setText(getString(R.string.no_books_found));

        //Clear the adapter of previous books data
        mAdapter.clear();

        //If there is a valid list of books, then add them to the adapter's data set. This
        //updates the ListView
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader) {
        // Loader reset & clear existing data
        mAdapter.clear();
    }

}

