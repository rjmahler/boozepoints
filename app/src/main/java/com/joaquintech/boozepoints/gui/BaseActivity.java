package com.joaquintech.boozepoints.gui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.database.viewholder.BeerViewHolder;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    public void onStop() {
        super.onStop();
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
