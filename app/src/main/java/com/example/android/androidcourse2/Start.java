package com.example.android.androidcourse2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android.androidcourse2.Constants;
import com.example.android.androidcourse2.DeliveryActivity;
import com.example.android.androidcourse2.ListPackages;
import com.example.android.androidcourse2.MainActivity;
import com.example.android.androidcourse2.Person;
import com.example.android.androidcourse2.PersonList;
import com.example.android.androidcourse2.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;


public class Start extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button b = (Button)findViewById(R.id.delivery);
        assert b != null;
        b.setOnTouchListener(new View.OnTouchListener() {

            private static final String logTag = "SwipeDetector";
            private static final int MIN_DISTANCE = 100;
            private float downX, downY, upX, upY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        downX = event.getX();
                        downY = event.getY();
                        return false; // allow other events like Click to be processed
                    }

                    case MotionEvent.ACTION_MOVE: {
                        upX = event.getX();
                        upY = event.getY();

                        float deltaX = downX - upX;
                        float deltaY = downY - upY;

                        // horizontal swipe detection
                        if (Math.abs(deltaX) > MIN_DISTANCE) {


                            if (deltaX < 0) {
                                Log.i(logTag, "Swipe Left to Right");

                                return true;
                            }
                            if (deltaX > 0) {
                                Log.i(logTag, "Swipe Right to Left");

                                return true;
                            }
                        } else

                            // vertical swipe detection
                            if (Math.abs(deltaY) > MIN_DISTANCE) {
                                // top or down
                                if (deltaY < 0) {

                                    Log.i(logTag, "Swipe Top to Bottom");
                                    return false;
                                }
                                if (deltaY > 0) {

                                    Log.i( logTag, "Swipe Bottom to Top");
                                    return false;
                                }
                            }
                        return true;
                    }
                }
                return false;
            }

        });
//        /**
//        SharedPreferences settings = getSharedPreferences(Constants.goTo,0);
//        boolean firstTime = settings.getBoolean("FirstTime", true);
//        if(firstTime) {
//            Intent i = new Intent(this, MainActivity.class);
//            i.putExtra(Constants.firstTime, firstTime);
//            startActivity(i);
//        }
//        ***/

    }

    public void createPerson(View v){startActivity(new Intent(this, MainActivity.class));}

    public void listPerson(View v){
        startActivity(new Intent(this, PersonList.class));
    }

    public void createPackage(View v){
        Intent i = new Intent(this, PersonList.class);
        i.putExtra(Constants.FromCreatePackageButton,true);
        startActivity(i);
    }

    public void listPackage(View v){

        startActivity(new Intent(this, ListPackages.class));
    }

    public void deliver(View v) {
        startActivity(new Intent(this, DeliveryActivity.class));}

    }
