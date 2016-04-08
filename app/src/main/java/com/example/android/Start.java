package com.example.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        SharedPreferences settings = getSharedPreferences(Constants.goTo,0);
        boolean firstTime = settings.getBoolean("FirstTime", true);
        if(firstTime) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(Constants.firstTime, firstTime);
            startActivity(i);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void createPerson(View v){
        startActivity(new Intent(this, MainActivity.class));
    }

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

    public void deliver(View v) { startActivity(new Intent(this, DeliveryActivity.class));}

    }
