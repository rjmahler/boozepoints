package com.joaquintech.boozepoints.database.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.viewholder.BeerViewHolder;
import com.joaquintech.boozepoints.gui.BeerDetailActivity;

import static com.joaquintech.boozepoints.gui.BeerDetailActivity.EXTRA_BEER_KEY;
import static com.joaquintech.boozepoints.gui.BeerDetailActivity.READ_ONLY;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class SearchBeersFragment extends BeersFragment{


    private Query query = null;
    private EditText mSearchText;
    public SearchBeersFragment() {

    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search_beers, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.beer_list);
        mRecycler.setHasFixedSize(true);
        mSearchText = (EditText) rootView.findViewById(R.id.textSearch);
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Set up FirebaseRecyclerAdapter with the Query
                final Query postsQuery =  getQuery(mDatabase);


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
        });
        return rootView;
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
        final Query postsQuery =  getQuery(mDatabase);


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

/*
    public void setQuery(DatabaseReference databaseReference, String queryString) {
        query = databaseReference.child("beers").startAt(queryString).endAt(queryString +"\uf8ff").limitToFirst(100);
    }.orderByChild("location").startAt("San").endAt("San"+"\uf8ff")
                .limitToFirst(100);
*/
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        if(query == null) {
            String queryString = mSearchText.getText().toString().trim();
            return queryString.isEmpty() ? databaseReference.child("beers").limitToFirst(100) : databaseReference.child("beers").orderByChild("name").startAt(queryString).endAt(queryString +"\uf8ff").limitToFirst(100);
        }

        return query;
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
