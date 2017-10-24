package com.joaquintech.boozepoints.gui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.joaquintech.boozepoints.R;

public class SearchResultsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data
            buildSearchListView(query);
            finish();
        }
    }
}
