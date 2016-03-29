package com.example.android.androidcourse2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class ListPackages extends Activity {
    //TODO http://stackoverflow.com/questions/25500353/android-actionbar-hide-show-when-scrolling-list-view
    //TODO http://developer.android.com/training/appbar/actions.html
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);//hides appBar. Activity must be "Activity" w/o "AppCompat"
        setContentView(R.layout.activity_list_packages);

        final ListView lv = (ListView)findViewById(R.id.paketilist);
        lv.setAdapter(new PaketAdapter(this, Paket.listAll(Paket.class)));
        final int[] pos = new int[1];

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                i.putExtra(Constants.paketID, (((Paket)lv.getItemAtPosition(position))).getId());
                startActivity(i);
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                pos[0] =position;
                i.putExtra(Constants.paketID, (((Paket)lv.getItemAtPosition(position))).getId());
                startActivity(i);
            }
        });

        /*ImageView map = (ImageView)findViewById(R.id.mapicon);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                i.putExtra("lat", (((Paket)lv.getItemAtPosition(pos[0]))).pickupLat);
                i.putExtra("lon", (((Paket)lv.getItemAtPosition(pos[0]))).pickupLon);
                startActivity(i);
            }
        });*/
    }
}

