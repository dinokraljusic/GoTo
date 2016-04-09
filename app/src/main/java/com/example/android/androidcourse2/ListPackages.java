package com.example.android.androidcourse2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListPackages extends AppCompatActivity {
    List<Paket> listapaketa;
    public static ArrayList<Long> checkedIndices;
    public static  MenuItem sendMenuItem;

    //TODO http://stackoverflow.com/questions/25500353/android-actionbar-hide-show-when-scrolling-list-view
    //TODO http://developer.android.com/training/appbar/actions.html
    //If we've passed a personID to this activity, "listapaketa" contains all Pakets pertaining to Person with that ID.
    //If no value is passed (0), "listapaketa" will contain all Pakets from DB.

    //checkedIndices is an array that contains the Paket.IDs of all checked Pakets. It is made public so that its
    //contents could be changed from within the PaketAdapter class, specifically from "setOnCheckedChangeListener"
    //Listener has to be created in Adapter cause that's where the checkbox is created. (PaketAdapter lines 108 and 111)

    //sendMenuItem is also changed from within PaketAdapter, where we make it invisible when there are no selected
    //items (i.e. selectedIndices.count() returns 0). Otherwise, we make it visible. (PaketAdapter lines 109 and 113)

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
        //final int[] pos = new int[1];

        lv.setDividerHeight(0);

        //On item click, we open CreatePackage activity and pass Paket.Id so that it displays its details.
        //In PaketAdapter, however, we set different onClick listeners for the checkbox (PaketAdapter line 104)
        //and the map icon (if present) (PaketAdapter line 94)

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPackages.this, CreatePackage.class);
                //pos[0] =position;
                i.putExtra(Constants.paketID, (((Paket)lv.getItemAtPosition(position))).getId());
                startActivity(i);
            }
        });

        //TODO: if there are more than 7 Pakets listed, checked states have to be watched
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            //http://stackoverflow.com/questions/28741645/how-to-implement-onscrolllistener-to-a-listview

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;


            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
                    //TODO: foreach cFVI .. cFVi + cVIC if checked setChecked
                    for(int i =0; i<currentVisibleItemCount; i++){
                        //Log.d("AAAA", checkedIndices.get(i).toString());
                        //lv.getItemAtPosition()
                        Paket pak = (Paket)(lv.getItemAtPosition(currentFirstVisibleItem+i));
                        Long id = pak.getId();
                        if(checkedIndices.contains(id)){
                           // lv.getAdapter().getItem(0);
                            //setChecked cFVI+i
                            //lv.getItemAtPosition(i)
                        }
                    }

                }
            }
            });

    }

    //Setting the ActionBar menu for this activity from res/layout/menu_list_packages with the only
    //item being the Send button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_packages, menu);
        sendMenuItem = menu.getItem(0);
        sendMenuItem.setVisible(false);
        return true;
    }

    //Passes the checked otem to DeliveryActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
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

