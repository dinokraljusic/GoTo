package com.example.android.androidcourse2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    }
}
