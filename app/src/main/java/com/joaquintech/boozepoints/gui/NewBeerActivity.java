package com.joaquintech.boozepoints.gui;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.models.Location;
import com.joaquintech.boozepoints.database.models.User;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rjmahler on 9/4/2017.
 *
 * An activity representing a list of Beers. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BeerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class NewBeerActivity extends BaseActivity {

    private static final String TAG = "NewBeerActivity";
    private TextInputLayout til;
    private static final int MAX_NAME_SIZE = 67;
   // private static final int MAX_TYPE_SIZE = 67;
    private static final int MAX_PLACE_SIZE = 67;
    private static final float MAX_PRICE = 999.99f;
    private static final float MAX_ABV = 100.00f;
    private static final int MAX_SIZE_ML = 20000;
    private static final float MAX_US_OZ = 700;
    private static final float MAX_UK_OZ = 700;
    private static final String ERROR_REQUIRED = "Required";
    private static final String ERROR_NO_SELECTION = "Please make a selection";
    private static final String ERROR_ZERO = "Cannot be zero";
    private static final String ERROR_NAME_SIZE = "Name cannot exceed " + MAX_NAME_SIZE + "characters";
  //  private static final String ERROR_TYPE_SIZE = "Name cannot exceed " + MAX_TYPE_SIZE + "characters";
    private static final String ERROR_PLACE_SIZE = "Name cannot exceed " + MAX_PLACE_SIZE + "characters";
    private static final String ERROR_LOCATION = "Invalid location";
    private static final String ERROR_ABV = "ABV cannot be greater than " + MAX_ABV;
    private static final String ERROR_ML = "Volume cannot be greater than " + MAX_SIZE_ML;
    private static final String ERROR_PRICE = "Price cannot be greater than $" + MAX_PRICE;
    private static final String ERROR_US_OZ = "Volume cannot be greater than " + MAX_US_OZ;
    private static final String ERROR_UK_OZ = "Volume cannot be greater than " + MAX_UK_OZ;

    public static final String EXTRA_BEER_KEY = "beer_key";
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private ValueEventListener mBeerListener;
    // [END declare_database_ref]

    private EditText textName;
    private EditText textType;
    private EditText textPlace;
    private EditText textLocation;
    private EditText textPrice;
    private EditText textSize;
    private EditText textABV;
    private TextView textPoints;
    private Spinner beerTypesSpinner;
    private ImageView image;

    private String mBeerKey;
    private Button mSubmitButton;

    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private AlertDialog.Builder alertDialog;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beer);
        mBeerKey = getIntent().getStringExtra(EXTRA_BEER_KEY);
        if(mBeerKey != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("beers").child(mBeerKey);
        }
        else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        textName = (EditText) findViewById(R.id.textName);
       // textType = (EditText) findViewById(R.id.textType);
        beerTypesSpinner = (Spinner) findViewById(R.id.beer_types_spinner);
       // beerTypesSpinner.setFocusable(true);
       // beerTypesSpinner.setFocusableInTouchMode(true);
       // beerTypesSpinner.requestFocus();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.beer_types, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beerTypesSpinner.setAdapter(adapter);
        beerTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        textPlace = (EditText) findViewById(R.id.textPlace);
        textLocation = (EditText) findViewById(R.id.textLocation);
        textPrice = (EditText) findViewById(R.id.textPrice);
        textABV = (EditText) findViewById(R.id.textABV);
        textSize = (EditText) findViewById(R.id.textSize);
        textPoints = (TextView) findViewById(R.id.text_msg);


        mSubmitButton = (Button) findViewById(R.id.buttonPostBeer);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Beer _beer = getBeer(mBeerKey);
                if(_beer == null) {
                    return;
                }

                final Dialog dialog = new Dialog(context);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.fragment_celebration);
                dialog.setTitle("BoozePoints");

                TextView text = (TextView) dialog.findViewById(R.id.text_msg);
                text.setText("Your beer scored " + _beer.points +" points!");

                ImageView image = (ImageView) dialog.findViewById(R.id.viewResults);
                image.setImageResource(R.drawable.points);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(_beer != null) {
                            submitBeer(mBeerKey,_beer);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mBeerKey != null) {
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Beer beer = dataSnapshot.getValue(Beer.class);
                    textName.setText(beer.name);
                   // textType.setText(beer.type);
                    beerTypesSpinner.setSelection(((ArrayAdapter<String>)beerTypesSpinner.getAdapter()).getPosition(beer.type));
                    textPlace.setText(beer.place);
                    textLocation.setText(beer.location);
                    textPrice.setText((beer.price)+"");
                    textSize.setText(beer.vol_milliliters+"");
                    textABV.setText(beer.abv+"");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting beer failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    Toast.makeText(NewBeerActivity.this, "Failed to load beer.",
                            Toast.LENGTH_SHORT).show();
                }
            };

            mDatabase.getRef().addValueEventListener(postListener);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mBeerListener = postListener;

            // Listen for posts
            //mAdapter = new NewBeerActivity.BeerAdapter(this, mDatabase.getRef());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mBeerListener != null) {
            mDatabase.getRef().removeEventListener(mBeerListener);
        }

        // Clean up comments listener
       // mAdapter.cleanupListener();
    }

    private Beer getBeer(String mBeerKey) {
        final String name = textName.getText().toString().trim();
//        final String type = textType.getText().toString().trim();
        int typeIndex = beerTypesSpinner.getSelectedItemPosition();
        final String type = beerTypesSpinner.getSelectedItem().toString();
        final String place = textPlace.getText().toString().trim();
        final String location = textLocation.getText().toString().trim();
        final String price = textPrice.getText().toString().trim();
        final String ABV = textABV.getText().toString().trim();
        final String size = textSize.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            textName.setError(ERROR_REQUIRED);
            return null;
        }

        if (TextUtils.getTrimmedLength(name) > MAX_NAME_SIZE) {
            textName.setError(this.ERROR_NAME_SIZE);
            return null;
        }

        if (typeIndex == 0) {
           // beerTypesSpinner.setError(ERROR_NO_SELECTION);
            ((TextView)beerTypesSpinner.getChildAt(0)).setError(ERROR_NO_SELECTION);
            return null;
        }

        if (TextUtils.isEmpty(place)) {
            textPlace.setError(ERROR_REQUIRED);
            return null;
        }

        if (TextUtils.getTrimmedLength(place) > MAX_PLACE_SIZE) {
            textPlace.setError(this.ERROR_PLACE_SIZE);
            return null;
        }

        if (TextUtils.isEmpty(location)) {
            textLocation.setError(ERROR_REQUIRED);
            return null;
        }

        if(!Location.validateState(location)) {
            textLocation.setError(this.ERROR_LOCATION);
            return null;
        }
        if (TextUtils.isEmpty(price)) {
            textPrice.setError(ERROR_REQUIRED);
            return null;
        }


        if (TextUtils.isEmpty(ABV)) {
            textABV.setError(ERROR_REQUIRED);
            return null;
        }

        if (TextUtils.isEmpty(size)) {
            textSize.setError(ERROR_REQUIRED);
            return null;
        }

        final Float f_price = Float.valueOf(price);
        final Float f_ABV = Float.valueOf(ABV);
        final int i_size = Integer.valueOf(size);
        if(f_price < 0.01) {
            textPrice.setError(ERROR_ZERO);
            return null;
        }
        if (f_price > MAX_PRICE) {
            textPrice.setError(ERROR_PRICE);
            return null;
        }
        if(f_ABV < 0.01) {
            textABV.setError(ERROR_ZERO);
            return null;
        }
        if(f_ABV > MAX_ABV) {
            textABV.setError(ERROR_ABV);
            return null;
        }
        if(i_size <= 0) {
            textSize.setError(ERROR_ZERO);
            return null;
        }

        if(i_size > MAX_SIZE_ML) {
            textSize.setError(ERROR_ML);
            return null;
        }

        Beer beer = new Beer();
        beer.location = location;
        beer.name = name;
        beer.searchable_name = makeSearchableString(name);
        beer.type = type;
        beer.place = place;
        beer.vol_milliliters = i_size;
        beer.abv = f_ABV;
        beer.price = f_price;
        beer.points = getPoints(f_price, f_ABV, i_size);
        return beer;
    }

    private static String makeSearchableString(String name) {
        StringBuilder builder = new StringBuilder();

        for(String s : name.split("\\s+")){
            builder.append(s.toUpperCase());
            builder.append(" ");
        }

        return builder.toString().trim();
    }
    private void submitBeer(String mBeerKey, final Beer beer) {

        setEditingEnabled(false);

        // Disable button so there are no multi-posts

        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewBeerActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            beer.uid = userId;
                            persistToDB(beer);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        setEditingEnabled(true);
                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        textName.setEnabled(enabled);
        textLocation.setEnabled(enabled);
        textPrice.setEnabled(enabled);
        textABV.setEnabled(enabled);
        textSize.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    private int getPoints(float price, float ABV, int size) {
        int points = 0;
        if(price == 0.0) {
            points = 0;
        } else {
            points = (int) ((size*ABV)/price);
        }
        return points;
    }

    private void persistToDB(Beer beer) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String key;
        if(mBeerKey == null) {
            key = mDatabase.child("beers").push().getKey();
        }
        else {
            key = mBeerKey;
        }

        Map<String, Object> beerValues = beer.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/beers/" + key, beerValues);
        childUpdates.put("/user-beers/" + beer.uid + "/" + key, beerValues);

        mDatabase.updateChildren(childUpdates);
    }
}
