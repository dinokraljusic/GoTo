package com.example.android.androidcourse2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreatePackage extends AppCompatActivity {

    private List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        List<Paket> paketi = Paket.listAll(Paket.class);
        Spinner sp = (Spinner) findViewById(R.id.typespinner);
        String[] slist = {"Food", "Water", "Medical", "Tools", "Shelter", "Clothing", "Baby", "FirstAid"};
        sp.setAdapter(new ArrayAdapter<String>(CreatePackage.this, android.R.layout.simple_list_item_1, slist));
        long paketID = this.getIntent().getLongExtra(Constants.paketID, 0);

        //check if getIntent is null
        if (paketID == 0) {
            LinearLayout spinnerlayout = (LinearLayout) findViewById(R.id.spinnerlayout);
            spinnerlayout.setVisibility(View.VISIBLE);
            LinearLayout textlayout = (LinearLayout) findViewById(R.id.textlayout);
            textlayout.setVisibility(View.GONE);

            EditText dest = (EditText) findViewById(R.id.destination);
            dest.setText("");
            //dest.setInputType(InputType.TYPE_CLASS_TEXT);
            dest.setEnabled(true);
        } else {
            Paket p = Paket.findById(Paket.class, paketID);

            Button takePhoto = (Button) findViewById(R.id.takePhoto);
            Button savePackage = (Button) findViewById(R.id.savePackage);
            takePhoto.setVisibility(View.GONE);
            savePackage.setVisibility(View.GONE);

            Spinner typespinner = (Spinner) findViewById(R.id.typespinner);
            LinearLayout spinnerlayout = (LinearLayout) findViewById(R.id.spinnerlayout);
            LinearLayout textlayout = (LinearLayout) findViewById(R.id.textlayout);
            textlayout.setVisibility(View.VISIBLE);
            TextView typetext = (TextView) findViewById(R.id.typetext);
            typetext.setText(typespinner.getSelectedItem().toString());
            spinnerlayout.setVisibility(View.GONE);

            EditText dest = (EditText) findViewById(R.id.destination);
            dest.setText(p.Destination);
            dest.setEnabled(false);

            ImageView iv = (ImageView) findViewById(R.id.slika);
            iv.setImageURI(Uri.parse(p.Photo));
            CheckBox perishable = (CheckBox) findViewById(R.id.perishable);
            CheckBox fragile = (CheckBox) findViewById(R.id.fragile);
            CheckBox heavy = (CheckBox) findViewById(R.id.heavy);
            CheckBox liquid = (CheckBox) findViewById(R.id.liquid);

            Spinner type = (Spinner) findViewById(R.id.typespinner);
            //p.type = Paket.Type.values()[type.getSelectedItemPosition()];


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
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        //cameraIntent.setDisplayOrientation(90);
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
        List<Address> addressList = null;
        String addressString="";

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

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location l1= null;

            try{
                Geocoder gc = new Geocoder(this);
                l1 = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                addressList = gc.getFromLocation(l1.getLatitude(), l1.getLongitude(), 3);

                Location l2 = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location l3 = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                addressString = addressList.get(0).getAddressLine(0) +", "+ addressList.get(1).getAddressLine(0) +", "
                        + addressList.get(2).getAddressLine(0) + ", " + addressList.get(0).getCountryName();

            }
            catch(SecurityException e){
                e.printStackTrace();
            }
            catch(Exception exc){
                exc.printStackTrace();
            }

            if(l1!=null){
                p.PickupLocation = l1;
            }
            p.status = Paket.status.PickedUp;

            //Spinner type = (Spinner)findViewById(R.id.typespinner);
            //p.type = Paket.Type.getSelectedItem().toString();
            p.save();

        }
        catch(Exception e){
            return false;
        }
        String toastMessage = addressList == null ? "Package created." : "Package created on " + addressString;
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        return true;
    }
}
