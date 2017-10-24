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

    private DatabaseReference mDatabase;

    protected FirebaseRecyclerAdapter<Beer, BeerViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    
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

        // Set up FirebaseRecyclerAdapter with the Query
        final Query postsQuery = getQuery(mDatabase);

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
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });
    }
*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public void setAdapter(String query) {
        Query queryRef = getQuery(FirebaseDatabase.getInstance().getReference()).startAt(query).endAt(query +"\uf8ff");

        FirebaseRecyclerAdapter mAdapter = new FirebaseRecyclerAdapter<Beer, BeerViewHolder>(Beer.class, R.layout.item_beer,
                BeerViewHolder.class, queryRef) {
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
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public abstract AdapterView.OnItemClickListener getOnItemClickListener();

    public  FirebaseRecyclerAdapter<Beer, BeerViewHolder> getAdapter() {return mAdapter;}

    public  RecyclerView getRecyclerView() {

        return mRecycler;
    }

}
