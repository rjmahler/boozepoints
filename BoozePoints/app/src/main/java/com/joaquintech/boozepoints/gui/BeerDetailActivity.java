package com.joaquintech.boozepoints.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.viewholder.BeerViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjmahler on 9/4/2017.
 *
 * An activity representing a single Beer detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link NewBeerActivity}.
 */
public class BeerDetailActivity extends BaseActivity {

    private static final String TAG = "BeerDetailActivity";

    public static final String EXTRA_BEER_KEY = "beer_key";
    public static final String READ_ONLY = "read_only";

    private DatabaseReference mBeerReference;
    private ValueEventListener mBeerListener;

    private BeerAdapter mAdapter;

    private TextView viewPoints;
    private TextView viewName;
    private TextView viewPrice;
    private TextView viewSize;
    private TextView viewABV;
    private TextView viewLocation;

    private Button editBeer;
    private Button deleteBeer;
    private String mBeerKey;
    private boolean readOnly;

    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        // Get post key from intent
        mBeerKey = getIntent().getStringExtra(EXTRA_BEER_KEY);
        if (mBeerKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_BEER_KEY");
        }
        if(getIntent().getStringExtra(READ_ONLY) != null) {
            readOnly = true;
        }

        mBeerReference = FirebaseDatabase.getInstance().getReference()
                .child("beers").child(mBeerKey);

        viewPoints = (TextView) findViewById(R.id.viewPoints);
        viewName = (TextView) findViewById(R.id.viewName);
        viewPrice = (TextView) findViewById(R.id.viewPrice);
        viewSize = (TextView) findViewById(R.id.viewSize);
        viewABV = (TextView) findViewById(R.id.viewABV);
        viewLocation = (TextView) findViewById(R.id.viewLocation);
        editBeer = (Button) findViewById(R.id.buttonEditBeer);
        editBeer.setVisibility(readOnly ? View.INVISIBLE : View.VISIBLE);
        editBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BeerDetailActivity.this, NewBeerActivity.class);
                intent.putExtra(BeerDetailActivity.EXTRA_BEER_KEY, mBeerKey);
                startActivity(intent);
                finish();
            }
        });
        deleteBeer = (Button) findViewById(R.id.buttonDeleteBeer);
        deleteBeer.setVisibility(readOnly ? View.INVISIBLE : View.VISIBLE);
        deleteBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder =
                        new android.app.AlertDialog.Builder(BeerDetailActivity.this);
                builder.setTitle(R.string.delete_confirm);
                builder.setItems(R.array.dialog_items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: //
                                        showProgressDialog();
                                        mBeerReference.removeValue((new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if(databaseError == null) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("user-beers").child(getUid()).child(mBeerKey).removeValue((new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            hideProgressDialog();
                                                            if(databaseError != null) {
                                                                Toast.makeText(BeerDetailActivity.this, "Error while deleting beer",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                            else {
                                                                Toast.makeText(BeerDetailActivity.this, "Deleted beer",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }));
                                                }
                                                else {
                                                    hideProgressDialog();
                                                    Toast.makeText(BeerDetailActivity.this, "Error while deleting beer",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }));
                                        finish();
                                        break;
                                }
                            }
                        }
                );

                builder.setNegativeButton(getString(R.string.cancel), null);
                builder.create().show();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, NewBeerActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Beer beer = dataSnapshot.getValue(Beer.class);
                if(beer != null) {
                    viewName = (TextView) findViewById(R.id.viewName);
                    viewName.setText(beer.name);

                    viewLocation = (TextView) findViewById(R.id.viewLocation);
                    viewLocation.setText(beer.location);

                    viewPrice = (TextView) findViewById(R.id.viewPrice);
                    viewPrice.setText(currencyFormat.format(beer.price));

                    viewSize = (TextView) findViewById(R.id.viewSize);
                    viewSize.setText(beer.vol_milliliters + " ml");

                    viewABV = (TextView) findViewById(R.id.viewABV);
                    viewABV.setText(beer.abv + "%");

                    viewPoints = (TextView) findViewById(R.id.viewPoints);
                    viewPoints.setText(beer.points + " points");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w(TAG, "loadBeer:onCancelled", databaseError.toException());
                Toast.makeText(BeerDetailActivity.this, "Failed to load beer.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mBeerReference.addValueEventListener(postListener);
        mBeerListener = postListener;

        mAdapter = new BeerAdapter(this, mBeerReference);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBeerListener != null) {
            mBeerReference.removeEventListener(mBeerListener);
        }
        mAdapter.cleanupListener();
    }

    protected static class BeerAdapter extends RecyclerView.Adapter<BeerViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mBeerIds = new ArrayList<>();
        private List<Beer> mBeers = new ArrayList<>();


        public BeerAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                /*    Beer beer = dataSnapshot.getValue(Beer.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mBeerIds.add(dataSnapshot.getKey());
                    mBeers.add(beer);
                    notifyItemInserted(mBeers.size() - 1);*/
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A beer has changed, use the key to determine if we are displaying this
                    // beer and if so displayed the changed beer.
                    Beer newBeer = dataSnapshot.getValue(Beer.class);
                    String beerKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int beerIndex = mBeerIds.indexOf(beerKey);
                    if (beerIndex > -1) {
                        // Replace with the new data
                        mBeers.set(beerIndex, newBeer);
                        notifyItemChanged(beerIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + beerKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A beer has changed, use the key to determine if we are displaying this
                    // beer and if so remove it.
                    String beerKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int beerIndex = mBeerIds.indexOf(beerKey);
                    if (beerIndex > -1) {
                        // Remove data from the list
                        mBeerIds.remove(beerIndex);
                        mBeers.remove(beerIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(beerIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + beerKey);

                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A beer has changed position, use the key to determine if we are
                    // displaying this beer and if so move it.
                    Beer movedbeer = dataSnapshot.getValue(Beer.class);
                    String beerKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;
        }

        @Override
        public BeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_beer, parent, false);

            return new BeerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BeerViewHolder holder, int position) {
            final Beer beer = mBeers.get(position);
            holder.abvView.setText(beer.abv+"");
            holder.nameView.setText(beer.name);
            holder.priceView.setText(beer.price+"");
            holder.sizeView.setText(beer.vol_milliliters+"");
        }

        @Override
        public int getItemCount() {
            return mBeers.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }
}
