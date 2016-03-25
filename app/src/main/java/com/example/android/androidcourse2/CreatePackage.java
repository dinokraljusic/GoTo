package com.example.android.androidcourse2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
        long paketID = this.getIntent().getLongExtra(Constants.paketID, 0);

        //check if getIntent is null
        if(paketID ==0){

        }
        else{
            Button takePhoto = (Button)findViewById(R.id.takePhoto);
            Button savePackage = (Button)findViewById(R.id.savePackage);
            takePhoto.setVisibility(View.INVISIBLE);
            savePackage.setVisibility(View.INVISIBLE);

            Paket p = Paket.findById(Paket.class, paketID);

            EditText dest = (EditText)findViewById(R.id.destination);
            dest.setText(p.Destination);

            ImageView iv = (ImageView)findViewById(R.id.slika);
            iv.setImageURI(Uri.parse(p.Photo));
            CheckBox perishable = (CheckBox)findViewById(R.id.perishable);
            CheckBox fragile = (CheckBox)findViewById(R.id.fragile);
            CheckBox heavy = (CheckBox)findViewById(R.id.heavy);
            CheckBox liquid = (CheckBox)findViewById(R.id.liquid);

            fragile.setChecked(p.Fragile);
            perishable.setChecked(p.Perishable);
            heavy.setChecked(p.Heavy);
            liquid.setChecked(p.Liquid);
        }

    }

    public String imageUri = null;
    public int TAKE_PHOTO_CODE = 100;
    String FILENAME = "package_picture";
    File dir = Environment.getExternalStorageDirectory();
    String path;
    File newfile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        path = dir.toString() + "/" +FILENAME;

        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK){

            ImageView iv = (ImageView)findViewById(R.id.slika);
            iv.setImageURI(Uri.parse(path));
            imageUri = path;

            //Bitmap bmp = BitmapFactory.decodeFile(path);
            //iv.setImageBitmap(bmp);

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //iv.setImageBitmap(imageBitmap);
            //Toast.makeText(this,"captured", Toast.LENGTH_SHORT).show();
        }
    }


    public void takePhoto(View view){

        Date current = new Date();
        FILENAME += Math.random()*1000;
        String file = dir + "/" + FILENAME;

        newfile = new File(file);
        try{
            newfile.createNewFile();
        }
        catch(IOException e){
            Log.d("a", "b");
        }

        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Log.i("ACTION EVENTS", "onResume");
        if(captured){
            ImageView iv = (ImageView)findViewById(R.id.slika);
            Bitmap bImage = BitmapFactory.decodeFile(path);
            iv.setImageBitmap(bImage);
            //iv.setImageURI(Uri.parse(dir.toString() + "/" + FILENAME));
        }*/
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
        Toast.makeText(this, "Package created.", Toast.LENGTH_SHORT).show();
        return true;
    }
}
