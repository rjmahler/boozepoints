package com.joaquintech.boozepoints.database.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.gui.BeerDetailActivity;

import static com.joaquintech.boozepoints.gui.BeerDetailActivity.EXTRA_BEER_KEY;
import static com.joaquintech.boozepoints.gui.BeerDetailActivity.READ_ONLY;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class TopBeersFragment extends BeersFragment {
    public TopBeersFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // My top posts by number of stars
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("beers").child(myUserId)
                .orderByChild("points").limitToFirst(100);
        return myTopPostsQuery;
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), BeerDetailActivity.class);
                final DatabaseReference beerRef = mAdapter.getRef(position);
                // Set click listener for the whole post view
                final String beerKey = beerRef.getKey();
                intent.putExtra(EXTRA_BEER_KEY, beerKey);
                intent.putExtra(READ_ONLY, beerKey);
                startActivity(intent);
            }
        };
    }
}
