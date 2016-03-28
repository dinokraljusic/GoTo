package com.example.android.androidcourse2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PersonList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        ListView listView = (ListView) findViewById(R.id.personlist);
        listView.setAdapter(new PersonAdapter(this, Person.listAll(Person.class)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PersonList.this, MainActivity.class);
                i.putExtra(Constants.personID, id);
                startActivity(i);
            }
        });
    }
}
