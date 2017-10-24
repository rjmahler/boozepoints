package com.joaquintech.boozepoints.database.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.gui.BeerDetailActivity;

import static com.joaquintech.boozepoints.gui.BeerDetailActivity.EXTRA_BEER_KEY;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class MyBeersFragment extends BeersFragment {



    public MyBeersFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("user-beers")
                .child(getUid());
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
