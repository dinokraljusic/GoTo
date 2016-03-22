package com.example.android.androidcourse2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ListPackages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_packages);

        ListView lv = (ListView)findViewById(R.id.paketiListView);
        lv.setAdapter(new PaketAdapter(this,MockData.createListOfPackages()));
    }
}

