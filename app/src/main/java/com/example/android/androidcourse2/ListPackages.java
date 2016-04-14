package com.example.android.androidcourse2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

public class ListPackages extends AppCompatActivity {

    List<Paket> listapaketa;
    public static ArrayList<Long> checkedIndices;

    //TODO http://stackoverflow.com/questions/25500353/android-actionbar-hide-show-when-scrolling-list-view
    //TODO http://developer.android.com/training/appbar/actions.html
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);//hides appBar. Activity must be "Activity" w/o "AppCompat"
        setContentView(R.layout.activity_list_packages);

        listapaketa = null;
        long personID = getIntent().getLongExtra(Constants.personID,0);
        checkedIndices = new ArrayList<Long>();

        String sPersonID = String.valueOf(personID);
        //String PersonID = Long.toString(personID);

        if (personID == 0) {
            listapaketa = Paket.listAll(Paket.class);
        }
        else
        {
            listapaketa = Paket.find(Paket.class, "SENDER_ID=? OR RECEIVER_ID=?", new String[] {sPersonID,sPersonID},null,"PICKUP_DATE",null);
        }
        final ListView lv = (ListView)findViewById(R.id.paketilist);
        lv.setAdapter(new PaketAdapter(this, listapaketa));
        final int[] pos = new int[1];
        //lv.setFooterDividersEnabled(false);
        //lv.setHeaderDividersEnabled(false);
        lv.setDividerHeight(0);

        /*lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                i.putExtra(Constants.paketID, (((Paket)lv.getItemAtPosition(position))).getId());
                startActivity(i);
                return false;
            }
        });*/

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                pos[0] = position;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_packages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                //TODO add edit settings activity
                return true;

            case R.id.action_send:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Selected IDs:");
                for(Long lng:checkedIndices)
                {
                    stringBuffer.append(lng);
                    stringBuffer.append(", ");
                }
                Intent i = new Intent(ListPackages.this, DeliveryActivity.class);
                i.putExtra(Constants.paketID, checkedIndices.get(0));
                startActivity(i);

                //Toast.makeText(ListPackages.this, stringBuffer.toString(), Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

