package com.example.android.quakereport;

public class Earthquake {

    private String mLocation;
    private double mMagnitude;
    private long mTimeInMilliseconds;
    private String mUrl;


    public Earthquake (String location, double magnitude, long timeInMilliseconds, String url){
        mLocation = location;
        mMagnitude = magnitude;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public String getmLocation() {
        return mLocation;
    }

    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmUrl() {
        return mUrl;
    }
}
