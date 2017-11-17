package com.joaquintech.boozepoints.gui;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.fragments.BeersFragment;
import com.joaquintech.boozepoints.database.fragments.MyBeersFragment;
import com.joaquintech.boozepoints.database.fragments.MyTopBeersFragment;
import com.joaquintech.boozepoints.database.fragments.RecentBeersFragment;
import com.joaquintech.boozepoints.database.fragments.SearchBeersFragment;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.viewholder.BeerViewHolder;

/**
 * Created by rjmahler on 9/4/2017.
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final String SEARCH_QUERY = "SearchQuery";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new RecentBeersFragment(),
                    new MyBeersFragment(),
                    new SearchBeersFragment()
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_beers),
                    getString(R.string.heading_search_beers)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.beer_list) ;
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        findViewById(R.id.fab_new_beer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewBeerActivity.class));
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String volPref = sharedPref.getString
                (SettingsActivity.KEY_VOLUME_UNIT_SWITCH, "millilitres");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {

            // create a new AlertDialog
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Logout Confirmation")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("YES", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int whichButton) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            })
            .setNegativeButton(getString(R.string.cancel), null).show();
            return true;
        } else if (i == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
