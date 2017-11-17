package com.joaquintech.boozepoints.database.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.gui.BeerDetailActivity;

import static com.joaquintech.boozepoints.gui.BeerDetailActivity.EXTRA_BEER_KEY;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class MyTopBeersFragment extends BeersFragment {
    public MyTopBeersFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("user-beers").child(myUserId)
                .orderByChild("points");

        return myTopPostsQuery;
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), BeerDetailActivity.class);
                final DatabaseReference beerRef = mAdapter.getRef(position);

                final String beerKey = beerRef.getKey();
                intent.putExtra(EXTRA_BEER_KEY, beerKey);
                startActivity(intent);
            }
        };
    }
}
