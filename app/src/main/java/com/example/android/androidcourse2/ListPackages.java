package com.example.android.androidcourse2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListPackages extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//hides appBar. Activity must be "Activity" w/o "AppCompat"
        setContentView(R.layout.activity_list_packages);

        final ListView lv = (ListView)findViewById(R.id.paketilist);
        lv.setAdapter(new PaketAdapter(this, Paket.listAll(Paket.class)));

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                Paket pak = (Paket)lv.getItemAtPosition(position);
                long poz = pak.getId();
                i.putExtra(Constants.paketID, poz);
                startActivity(i);
                return false;
            }
        });
    }
}

