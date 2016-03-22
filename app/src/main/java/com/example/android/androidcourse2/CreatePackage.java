package com.example.android.androidcourse2;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreatePackage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        List<Paket> paketi = Paket.listAll(Paket.class);
        Spinner sp = (Spinner)findViewById(R.id.type);
        String[] slist = {"Food", "Water", "Medical", "Tools", "Shelter", "Clothing", "Baby", "FirstAid" };
        sp.setAdapter(new ArrayAdapter<String>(CreatePackage.this, android.R.layout.simple_list_item_1, slist));


    }

    public Uri imageUri = null;
    public Uri slika = null;
    public int TAKE_PHOTO_CODE = 100;
    String FILENAME = "package_picture";
    File dir = Environment.getExternalStorageDirectory();
    String path;
    public boolean captured = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        //String file = dir + "/" + FILENAME;
        path = dir.toString() + "/" +FILENAME;

        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK){
            //makeText(this, "Camera demo: Photo saved to folder " + dir, LENGTH_LONG).show();

      //      ImageView iv = (ImageView)findViewById(R.id.slika);
            //iv.setImageBitmap(dir+FILENAME);
            //String path = dir.toString() + "\\" +FILENAME;

     //       iv.setImageURI(Uri.parse("file://" + path));
            //iv.setImageURI(slika);

            //imageUri = Uri.parse(path);
            captured = true;
        }
   //     imageUri = Uri.parse(path);
    }


    public void takePhoto(View view){
        //File dir = Environment.getExternalStorageDirectory();

        String file = dir + "/" + FILENAME;

        File newfile = new File(file);
        try{
            newfile.createNewFile();
        }
        catch(IOException e){
            Log.d("a", "b");
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        //slika = outputFileUri;
        captured = false;
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

       // ImageView iv = (ImageView)findViewById(R.id.slika);
       // iv.setImageBitmap(dir+FILENAME);
        //String path = dir.toString() + "\\" +FILENAME;
       // iv.setImageURI(outputFileUri);

        //imageUri = Uri.parse(path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ACTION EVENTS", "onResume");
        if(captured){
            ImageView iv = (ImageView)findViewById(R.id.slika);
            iv.setImageURI(Uri.parse(dir + "/" + FILENAME));
        }
    }

    public boolean savePackage(View view){
        try{
            Paket p = new Paket();

            p.Photo = imageUri;
           // p.DeliveryLocation
            Calendar cal = Calendar.getInstance();
            p.pickupDate = cal.getTime();
            EditText dest = (EditText)findViewById(R.id.destination);
            p.Destination = dest.getText().toString();

            CheckBox perishable = (CheckBox)findViewById(R.id.perishable);
            CheckBox fragile = (CheckBox)findViewById(R.id.fragile);
            CheckBox heavy = (CheckBox)findViewById(R.id.heavy);
            CheckBox liquid = (CheckBox)findViewById(R.id.liquid);

            p.Fragile = fragile.isChecked();
            p.Perishable = perishable.isChecked();
            p.Heavy = heavy.isChecked();
            p.Liquid = liquid.isChecked();
            p.save();

        }
        catch(Exception e){
            return false;
        }
        return true;
    }
}
