package com.joaquintech.boozepoints.database.viewholder;

import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joaquintech.boozepoints.R;
import com.joaquintech.boozepoints.database.models.Beer;
import com.joaquintech.boozepoints.enums.BeerTypes;

/**
 * Created by rjmahler on 9/4/2017.
 */

public class BeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public TextView nameView;
    public TextView locationView;
    public TextView priceView;
    public TextView abvView;
    public TextView sizeView;
    public TextView pointsView;
    public ImageView photo_imageView;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnLongClickListener mLongClickListener;

    public BeerViewHolder(View itemView) {
        super(itemView);

        nameView = (TextView) itemView.findViewById(R.id.beer_title1);
        locationView = (TextView) itemView.findViewById(R.id.beer_title1a);
        priceView = (TextView) itemView.findViewById(R.id.beer_title2);
        pointsView = (TextView) itemView.findViewById(R.id.beer_points);
        photo_imageView = (ImageView) itemView.findViewById(R.id.photo_image);
       // abvView = (TextView) itemView.findViewById(R.id.beer_name);
       // sizeView = (TextView) itemView.findViewById(R.id.beer_name);
        nameView.setOnClickListener(this);
        nameView.setOnLongClickListener(this);

        locationView.setOnClickListener(this);
        locationView.setOnLongClickListener(this);

        priceView.setOnClickListener(this);
        priceView.setOnLongClickListener(this);

        pointsView.setOnClickListener(this);
        pointsView.setOnLongClickListener(this);

        photo_imageView.setOnClickListener(this);
        photo_imageView.setOnLongClickListener(this);
    }

    public void bindToBeer(Beer beer) {

        nameView.setText(beer.name + " (" + beer.vol_milliliters + "ml)");
        locationView.setText(beer.location);
        priceView.setText("ABV="+ beer.abv +"%,Price=$"+beer.price+"");
        pointsView.setText(beer.points+"");
        try {
            BeerTypes beerType = BeerTypes.valueOf(beer.type.toString().replaceAll(" ","_").toUpperCase());
            int resourceId = 0;
            switch(beerType) {
                case DARK_ALE:
                    resourceId = R.drawable.dark_ale;
                    break;
                case IMPERIAL_STOUT:
                    resourceId = R.drawable.imperial_stout;
                    break;
                case GOLDEN_ALE:
                    resourceId = R.drawable.golden_ale;
                    break;
                case LAGER:
                    resourceId = R.drawable.lager;
                    break;
                case PALE_ALE:
                    resourceId = R.drawable.pale_ale;
                    break;
                case STOUT:
                    resourceId = R.drawable.stout;
                    break;
                case WHEAT_BEER:
                    resourceId = R.drawable.wheat;
                    break;
                case PILSNER:
                    resourceId = R.drawable.pilsner;
                    break;
                case PORTER:
                    resourceId = R.drawable.porter;
                    break;
                default:
                    resourceId = R.drawable.lager;
                    break;
            }
            photo_imageView.setImageResource(resourceId);
        }catch (IllegalArgumentException e) {
            photo_imageView.setImageResource(R.drawable.lager);
        }

      //  abvView.setText(beer.abv+"");
       // sizeView.setText(beer.vol_milliliters+"");
    }

    @Override
    public void onClick(View view) {
       if (mItemClickListener != null /*&& view.getId() == photo_imageView.getId()*/) {
           mItemClickListener.onItemClick(null, view, getAdapterPosition(), 0);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(mLongClickListener != null /*&& view.getId() == photo_imageView.getId()*/) {
            mLongClickListener.onLongClick(view);
        }
        return false;
    }

    public void setOnLongClickListener(final AdapterView.OnLongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
