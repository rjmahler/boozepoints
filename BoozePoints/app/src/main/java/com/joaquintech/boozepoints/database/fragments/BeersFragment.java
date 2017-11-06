package com.joaquintech.boozepoints.database.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.viewholder.BeerViewHolder;
import com.joaquintech.boozepoints.gui.BeerDetailActivity;
import com.joaquintech.boozepoints.gui.MainActivity;

/**
 * Created by rjmahler on 9/4/2017.
 */

public abstract class BeersFragment extends Fragment {
    private static final String TAG = "BeerListFragment";
    private static final String SEARCH_QUERY="searchQuery";

    protected DatabaseReference mDatabase;

    protected FirebaseRecyclerAdapter<Beer, BeerViewHolder> mAdapter;
    protected RecyclerView mRecycler;
    protected LinearLayoutManager mManager;
    
    public BeersFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_beers, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.beer_list);
        mRecycler.setHasFixedSize(true);

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

        //String searchQuery = getActivity().getIntent().getStringExtra(SEARCH_QUERY);

        // Set up FirebaseRecyclerAdapter with the Query
        final Query postsQuery = /*searchQuery == null ? */ getQuery(mDatabase);// : getQuery(mDatabase).startAt(searchQuery).endAt(null).limitToFirst(100);


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
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public abstract AdapterView.OnItemClickListener getOnItemClickListener();

}
