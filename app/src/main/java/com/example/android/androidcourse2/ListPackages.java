package com.example.android.androidcourse2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListPackages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_packages);

        ListView lv = (ListView)findViewById(R.id.paketilist);
        lv.setAdapter(new PaketAdapter(this,Paket.listAll(Paket.class)));

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                i.putExtra("PaketID", position);
                startActivity(i);
                return false;
            }
        });
    }
}

