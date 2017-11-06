package com.joaquintech.boozepoints.gui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;

import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.fragments.RecentBeersFragment;

public class SearchResultsActivity extends MainActivity {

    private static final String SEARCH_QUERY="searchQuery";
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private RecyclerView recyclerView;

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
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_QUERY, query);
            RecentBeersFragment fragment = new RecentBeersFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment, RecentBeersFragment.class.getSimpleName())
                    .commit();

            // bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, query);
            finish();
        }
    }
}
