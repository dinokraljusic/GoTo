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
        List<Person> list = new ArrayList<Person>();
        Person p = new Person();
        p.ID = 234;
        p.name = "Asmir";
        p.lastname = "Kuric";
        p.address= "Adresa";
        p.email= "adasdasd";
        p.phone= "11111111" ;
        list.add(p);





        ListView listView = (ListView) findViewById(R.id.personlist);
        listView.setAdapter(new PersonAdapter(this, Person.listAll(Person.class)));
    }
}
