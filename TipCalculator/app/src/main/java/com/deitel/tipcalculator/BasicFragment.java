package com.deitel.tipcalculator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BasicFragment} interface
 * to handle interaction events.
 * Use the {@link BasicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicFragment extends android.support.v4.app.Fragment {
    private static final NumberFormat currencyFormat =
            NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat =
            NumberFormat.getPercentInstance();
    private static final String ARG_SECTION_NUMBER = "section_number";
    // currency and percent formatter objects


    private double billAmount = 0.0; // bill amount entered by the user
    private double percent = 0.15; // initial tip percentage
    private double taxPercent = 0.08; // initial tax percentage
    private int numPeople = 1;

    private TextView amountTextView; // shows formatted bill amount
    private TextView percentTextView; // shows tip percentage
    private TextView tipTextView; // shows calculated tip amount
    private TextView totalTextView; // shows calculated total bill amount
    private Switch switchTax;
    private TextView percentTaxTextView;
    private EditText percentTaxEditText;
    private TextView textView12;
    private TextView numPeopleTextView;
    private TextView tipPerPersonTextView;
    private EditText numPeopleEditText;

    public BasicFragment() {}

    public static BasicFragment newInstance(int sectionNumber) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_basic, container, false);

        numPeopleTextView = (TextView) rootView.findViewById(R.id.numPeopleTextView);
        tipPerPersonTextView = (TextView) rootView.findViewById(R.id.tipPerPersonTextView);
        numPeopleTextView.setText("1");
        tipPerPersonTextView.setText(currencyFormat.format(0));

        amountTextView = (TextView) rootView.findViewById(R.id.amountTextView);
        percentTextView = (TextView) rootView.findViewById(R.id.percentTextView);
        percentTaxTextView = (TextView) rootView.findViewById(R.id.percentTaxTextView);
        tipTextView = (TextView) rootView.findViewById(R.id.tipTextView);
        totalTextView = (TextView) rootView.findViewById(R.id.totalTextView);
        tipTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));
        percentTaxTextView.setText(percentFormat.format(0));

        switchTax = (Switch) rootView.findViewById(R.id.switchTax);
        switchTax.setOnCheckedChangeListener(checkedChangeListener);
        // textView12 = (TextView) findViewById(R.id.textView12);
        // set amountEditText's TextWatcher
        EditText amountEditText =
                (EditText) rootView.findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        EditText percentEditText =
                (EditText) rootView.findViewById(R.id.percentTaxEditText);
        percentEditText.addTextChangedListener(percentTipEditTextWatcher);

        EditText numPeopleEditText =
                (EditText) rootView.findViewById(R.id.numPeopleEditText);
        numPeopleEditText.addTextChangedListener(numPeopleEditTextWatcher);
        // set percentSeekBar's OnSeekBarChangeListener
      /*LinearGradient test = new LinearGradient(0.f, 0.f, 400.f, 0.0f,

              new int[] { 0xFFb22222,0xFFFFFF00, 0xFF006400},
              null, Shader.TileMode.CLAMP);
      ShapeDrawable shape = new ShapeDrawable(new RectShape());
      shape.getPaint().setShader(test);*/

        SeekBar percentSeekBar =
                (SeekBar) rootView.findViewById(R.id.percentSeekBar);
        //percentSeekBar.setProgressDrawable((Drawable)shape);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);

        SeekBar peopleSeekBar =
                (SeekBar) rootView.findViewById(R.id.people);
        return rootView;
    }

    // listener object for the EditText's text-changed events
    private final TextWatcher numPeopleEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                numPeople = Integer.parseInt(s.toString());
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                numPeople = 1;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };

    private final TextWatcher percentTipEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get bill amount and display currency formatted value
                taxPercent = Double.parseDouble(s.toString()) / 100.0;
                percentTaxTextView.setText(percentFormat.format(taxPercent));
            } catch (NumberFormatException e) { // if s is empty or non-numeric
                percentTaxTextView.setText("");
                taxPercent = 0.0;
            }

            calculate(); // update the tip and total TextViews
        }
        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };

    private final CompoundButton.OnCheckedChangeListener checkedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    calculate();
                }
            };

    // calculate and display tip and total amounts
    private void calculate() {
        // format percent and display in percentTextView
        percentTextView.setText(percentFormat.format(percent));

        // calculate the tip and total
        double tip = billAmount * percent;
        if(! switchTax.isChecked()) {
            tip = tip / (1.0 + taxPercent);
        }
        double total = billAmount + tip;
        double tipPerPerson = tip/numPeople;



        // display tip and total formatted as currency
        tipTextView.setText(currencyFormat.format(tip));
        totalTextView.setText(currencyFormat.format(total));

        numPeopleTextView.setText(String.valueOf(numPeople));

        tipPerPersonTextView.setText(currencyFormat.format(tipPerPerson));
    }



    // listener object for the SeekBar's progress changed events
    private final SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                // update percent, then call calculate
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    percent = progress / 100.0; // set percent based on progress
                    calculate(); // calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            };

    // listener object for the EditText's text-changed events
    private final TextWatcher amountEditTextWatcher = new TextWatcher() {
        // called when the user modifies the bill amount
        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {

            try { // get bill amount and display currency formatted value
                billAmount = Double.parseDouble(s.toString()) / 100.0;
                amountTextView.setText(currencyFormat.format(billAmount));
            }
            catch (NumberFormatException e) { // if s is empty or non-numeric
                amountTextView.setText("");
                billAmount = 0.0;
            }

            calculate(); // update the tip and total TextViews
        }

        @Override
        public void afterTextChanged(Editable s) { }

        @Override
        public void beforeTextChanged(
                CharSequence s, int start, int count, int after) { }
    };


}
