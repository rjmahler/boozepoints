package com.joaquintech.boozepoints.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.joaquintech.boozepoints.R;

/**
 * Created by rjmahler on 9/4/2017.
 */
public class CelebrationDialog extends Dialog {
    public CelebrationDialog(Context ctx) {
        super(ctx);
    }
    public void showDialog(final BaseActivity baseActivity, int points){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.fragment_celebration);

        TextView text = (TextView) findViewById(R.id.text_msg);
        text.setText("Your beer scored " + points + " points!");

        ImageView image = (ImageView) findViewById(R.id.viewResults);
        image.setImageResource(R.drawable.points);

        Button dialogButton = (Button) findViewById(R.id.dialogButtonOK);
        final BaseActivity _baseActivity = baseActivity;
        final Class _class = baseActivity.getClass();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
               // Intent intent = new Intent(_baseActivity, _class);
               // baseActivity.startActivity(intent);
            }
        });
        show();
    }
}
