package com.example.shivam.imagefeed;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DetailsActivity extends ActionBarActivity {

    ImageView cameraImage;
    TextView locationText,addressText;
    Uri imageUri;
    Geocoder geocoder;
    List<Address> addresses;
    double latitude,longitude;
    String resultLatLong,resultAddr;
    Button mSave,mCancel,mRetry;
    PostORM p = new PostORM();
    LocationService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        service = new LocationService(DetailsActivity.this);
        cameraImage = (ImageView)findViewById(R.id.image);
        locationText = (TextView)findViewById(R.id.locationText);
        addressText = (TextView)findViewById(R.id.addressText);
        mSave = (Button)findViewById(R.id.savePost);
        mCancel = (Button)findViewById(R.id.cancelPost);
        addresses = new ArrayList<>();
        mRetry = (Button)findViewById(R.id.retryButton);

        imageUri = getIntent().getData();
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.insertPost(DetailsActivity.this, imageUri.toString(), resultLatLong, resultAddr);
                p.getAddressfromDB(DetailsActivity.this);
                p.getCoordinatesfromDB(DetailsActivity.this);
                Toast.makeText(DetailsActivity.this,"Created new Post",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailsActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailsActivity.this,"Post Discarded",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailsActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location gpsLocation = service.getLocation(LocationManager.GPS_PROVIDER);
                if (gpsLocation != null) {
                    mRetry.setVisibility(View.GONE);
                    mSave.setVisibility(View.VISIBLE);
                    latitude = gpsLocation.getLatitude();
                    longitude = gpsLocation.getLongitude();
                    resultLatLong = "Latitude: " + gpsLocation.getLatitude() +
                            " Longitude: " + gpsLocation.getLongitude();
                    geocoder = new Geocoder(DetailsActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(!isNetworkAvailable()||addresses.isEmpty())
                    {
                        Toast.makeText(DetailsActivity.this,"Could not get location !",Toast.LENGTH_SHORT).show();
                        mSave.setVisibility(View.GONE);
                        mRetry.setVisibility(View.VISIBLE);

                    }
                    else {
                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        resultAddr = address + "\n" + city + ", " + state;
                        locationText.setText(resultLatLong);
                        addressText.setText(resultAddr);
                    }
                }
            }
        });

        Location gpsLocation = service.getLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latitude = gpsLocation.getLatitude();
            longitude = gpsLocation.getLongitude();
            resultLatLong = "Latitude: " + gpsLocation.getLatitude() +
                    " Longitude: " + gpsLocation.getLongitude();
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses.isEmpty()||!isNetworkAvailable())
            {
                    mSave.setVisibility(View.GONE);
                    mRetry.setVisibility(View.VISIBLE);
            }
            else {

                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                resultAddr = address + "\n" + city + ", " + state;
                locationText.setText(resultLatLong);
                addressText.setText(resultAddr);
            }
        }
        else
        {
            Toast.makeText(DetailsActivity.this,"Could not get location !",Toast.LENGTH_SHORT).show();
            mSave.setVisibility(View.GONE);
            mRetry.setVisibility(View.VISIBLE);
        }

            Picasso.with(this).load(imageUri.toString()).resize(500,500).into(cameraImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
