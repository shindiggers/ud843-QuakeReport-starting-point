package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);



        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.tvMagnitude);
        DecimalFormat mag1DP = new DecimalFormat("#0.0");
        String magString = mag1DP.format(currentEarthquake.getmMagnitude());
        magnitudeView.setText(magString);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        magnitudeCircle.setColor(magnitudeColor);

        TextView offsetLocationView = (TextView) listItemView.findViewById(R.id.tvOffestLocation);
        String locationText = currentEarthquake.getmLocation();
        if (locationText.contains("of")){
            int breakPoint = locationText.indexOf("of")+3;
            String offsetString = locationText.substring(0,breakPoint);
            offsetLocationView.setText(offsetString);
        } else {
            String offsetString = "Near the";
            offsetLocationView.setText(offsetString);
        }

        TextView locationView = (TextView) listItemView.findViewById(R.id.tvLocation);
        if (locationText.contains("of")){
            int breakPoint = locationText.indexOf("of")+3;
            String locationString = locationText.substring(breakPoint);
            locationView.setText(locationString);
        } else {
            locationView.setText(locationText);
        }


        TextView dateView = (TextView) listItemView.findViewById(R.id.tvDate);
        Long quakeTime = currentEarthquake.getmTimeInMilliseconds();
        Date dateObject = new Date(quakeTime);
        String dateToDisplay = DateFormat.getDateInstance().format(dateObject);
        dateView.setText(dateToDisplay);

        TextView timeView = (TextView) listItemView.findViewById(R.id.tvTime);
        String timeToDisplay = DateFormat.getTimeInstance(DateFormat.SHORT).format(dateObject);
        timeView.setText(timeToDisplay);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude){

        int magnitudeFloor = (int) Math.floor(magnitude);
        int magnitudeColorResourceId;

        switch (magnitudeFloor){

            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }

}
