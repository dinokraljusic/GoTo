package com.example.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.android.androidcourse2.CreatePackage;
import com.example.android.androidcourse2.Delivery;
import com.example.android.androidcourse2.DeliveryActivity;
import com.example.android.androidcourse2.ListPackages;
import com.example.android.androidcourse2.MainActivity;
import com.example.android.androidcourse2.PersonList;
import com.example.android.androidcourse2.R;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        startActivity(new Intent(this, CreatePackage.class));
    }

    public void listPackage(View v){
        startActivity(new Intent(this, ListPackages.class));
    }

    public void deliver(View v) {
        startActivity(new Intent(this, DeliveryActivity.class));
    }

    }
