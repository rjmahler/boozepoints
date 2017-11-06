package com.joaquintech.boozepoints.database.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.viewholder.BeerViewHolder;
import com.joaquintech.boozepoints.gui.BeerDetailActivity;

import static com.joaquintech.boozepoints.gui.BeerDetailActivity.EXTRA_BEER_KEY;
import static com.joaquintech.boozepoints.gui.BeerDetailActivity.READ_ONLY;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class RecentBeersFragment extends BeersFragment{

    private DatabaseReference mDatabase;

    protected FirebaseRecyclerAdapter<Beer, BeerViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private static final String SEARCH_QUERY = "SearchQuery";
    private String query;

    public RecentBeersFragment() {

    }

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("beers")//.orderByChild("location").startAt("San").endAt("San"+"\uf8ff")
                .limitToFirst(100);
/*
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    Beer beer = dataSnapshot.getValue(Beer.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        return recentPostsQuery;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null)
            query = bundle.getString(SEARCH_QUERY);
        else {

        }

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_beers, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.beer_list);
        mRecycler.setHasFixedSize(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        final Query postsQuery = query == null ? getQuery(mDatabase) : mDatabase.startAt(query).endAt(query +"\uf8ff")
                .limitToFirst(100);

        mAdapter = new FirebaseRecyclerAdapter<Beer, BeerViewHolder>(Beer.class, R.layout.item_beer,
                BeerViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final BeerViewHolder viewHolder, final Beer model, final int position) {
                final DatabaseReference beerRef = getRef(position);

                final String beerKey = beerRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch BeerDetailActivity
                        Intent intent = new Intent(getActivity(), BeerDetailActivity.class);
                        intent.putExtra(BeerDetailActivity.EXTRA_BEER_KEY, beerKey);
                    }
                });

                viewHolder.setOnItemClickListener(getOnItemClickListener());

                viewHolder.bindToBeer(model);
            }


        };
        mRecycler.setAdapter(mAdapter);
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
