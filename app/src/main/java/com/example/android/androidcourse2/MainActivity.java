package com.example.android.androidcourse2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //main activity

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ACTION_EVENTS", "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ACTION_EVENTS", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ACTION_EVENTS", "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ACTION_EVENTS", "onDestroy");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ACTION_EVENTS", "onStop");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("ACTION_EVENTS", "onConfigurationChanged");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ACTION_EVENTS", "onCreate");
        setContentView(R.layout.activity_main);
        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
     //   List<Person> p = Person.listAll(Person.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), PersonalInfoActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    public void Save(View v){ // first line is method signature
        EditText et1 = (EditText) findViewById(R.id.name);
        String etName = et1.getText().toString();

        EditText et2 = (EditText) findViewById(R.id.lastname);
        String etLastName = et2.getText().toString();

        String fullName = etName + " " + etLastName;

        EditText et3 = (EditText)findViewById(R.id.address);
        String etAddress = et3.getText().toString();

        EditText et4 = (EditText)findViewById(R.id.phone);
        String etPhone = et4.getText().toString();

        EditText et5 = (EditText)findViewById(R.id.email);
        String etEmail = et5.getText().toString();

        Toast.makeText(getBaseContext(), "Saving Personal Info for: "+fullName, Toast.LENGTH_SHORT).show();

        Person p = new Person();
        p.name = etName;
        p.lastname = etLastName;
        p.address = etAddress;
        p.phone = etPhone;
        p.email = etEmail;
        p.save();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static final int TAKE_PHOTO_CODE = 1;

    public void TakePhoto(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,TAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = (ImageView)findViewById(R.id.profilePict);
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        image.setImageBitmap(imageBitmap);
    }
}
