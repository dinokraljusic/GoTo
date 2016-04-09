package com.example.android.androidcourse2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreatePackage extends AppCompatActivity {

    private List<Address> addressList;
    private double lon, lat;
    Long personID;
    public int TAKE_PHOTO_CODE = 100;
    String FILENAME = "package";
    File dir = Environment.getExternalStorageDirectory();
    File newfile;
    Uri outputFileUri;
    //TODO: runnable and handler

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_package);

        //If this activity is invoked from person list, it should be passed the paketID parameter.
        //If it's not, we're setting it to 0. If 0, we make the fields editable and prepare the activity
        //for entering required data for a new package (line 64 - if). If, however, it's a nonzero value,
        //we set the activity to display details for the selected Paket (line 80 - if).

        //When creating a new Paket, we pass the personID argument to know who created it
        personID = getIntent().getLongExtra(Constants.personID, 0);

        List<Paket> paketi = Paket.listAll(Paket.class);
        Spinner sp = (Spinner) findViewById(R.id.typespinner);
        String[] slist = {"Food", "Water", "Medical", "Tools", "Shelter", "Clothing", "Baby", "FirstAid"};
        sp.setAdapter(new ArrayAdapter<String>(CreatePackage.this, android.R.layout.simple_list_item_1, slist));
        long paketID = this.getIntent().getLongExtra(Constants.paketID, 0);

        //No paketID passed, setting the activity for creation of a new Paket...
        if (paketID == 0) {
            LinearLayout spinnerlayout = (LinearLayout) findViewById(R.id.spinnerlayout);
            spinnerlayout.setVisibility(View.VISIBLE);
            LinearLayout textlayout = (LinearLayout) findViewById(R.id.textlayout);
            textlayout.setVisibility(View.GONE);
            LinearLayout packageDestinationText = (LinearLayout) findViewById(R.id.packagedestinationlayout);
            packageDestinationText.setVisibility(View.VISIBLE);
            LinearLayout packageDestinationButton = (LinearLayout) findViewById(R.id.packagedestinationbuttonlayout);
            packageDestinationButton.setVisibility(View.GONE);

            EditText dest = (EditText) findViewById(R.id.destination);
            dest.setText("");
            //dest.setInputType(InputType.TYPE_CLASS_TEXT);
            dest.setEnabled(true);
            lat=lon=0;
        }
        else {//paketID passed to this activity, setting it to just show Paket info
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
            LinearLayout packageDestinationText = (LinearLayout) findViewById(R.id.packagedestinationlayout);
            packageDestinationText.setVisibility(View.VISIBLE);
            EditText destination = (EditText)findViewById(R.id.destination);
            destination.setText("Destination: " + p.Destination);

            LinearLayout packageDestinationButton = (LinearLayout) findViewById(R.id.packagedestinationbuttonlayout);

            if(p.pickupLat != 0)
                packageDestinationButton.setVisibility(View.VISIBLE);

            EditText dest = (EditText) findViewById(R.id.destination);
            dest.setText(p.Destination);
            dest.setEnabled(false);

            ImageView iv = (ImageView) findViewById(R.id.slika);
            if(p.Photo != null && p.Photo.length() > 4)
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


            if(p.pickupLon !=0 && p.pickupLat !=0){
                lon = p.pickupLon;
                lat = p.pickupLat;
            }
        }

    }

    //We get a unique number for current time so that every pic has a unique name. We create a file from
    //that and pass path to that file to cameraActivity. When photo is taken, onActivityResult is invoked
    public void takePhoto(View view){

        Date current = new Date();
        FILENAME += String.valueOf(current.getTime());
        String file = dir + "/" + FILENAME;

        newfile = new File(file);
        try{
            newfile.createNewFile();
        }
        catch(IOException e){
            Log.e("ERR", "mewfile creation failed");
        }

        outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        //cameraIntent.setDisplayOrientation(90);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    //We create two files; one is the image, the other is the thumbnail used to display in ListaPaketa.
    //Every Paket image has a corresponding thumbnail
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

            ImageView iv = (ImageView)findViewById(R.id.slika);
            iv.setImageURI(outputFileUri);

            final Bitmap thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(outputFileUri.toString()), 92, 92);
            OutputStream stream = null;//TODO: crashes in emulator, resolve
            try {
                stream = new FileOutputStream(outputFileUri.toString()+"thumb");
                thumb.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //iv.setImageBitmap(imageBitmap);
            //Toast.makeText(this,"captured", Toast.LENGTH_SHORT).show();
        }
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
        String addressString="";
        Location l1=null;
        try {
            Paket p = new Paket();

            p.Photo = outputFileUri.toString();
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
            p.SenderID = Integer.valueOf(1); //TODO change SenderID to Long
            p.status = Paket.Status.PickedUp.name();

            Spinner type = (Spinner) findViewById(R.id.typespinner);
            Paket.Type t = Paket.Type.values()[type.getSelectedItemPosition()];
            p.type = t.name();
            p.SenderID = personID;

            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

            try {
                l1 = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                if(l1==null)
                {
                    l1=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if(l1==null)
                    l1=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                try {
                    Geocoder gc = new Geocoder(this);
                    addressList = gc.getFromLocation(l1.getLatitude(), l1.getLongitude(), 4);

                    addressString = addressList.get(0).getAddressLine(0) +", "
                            + addressList.get(1).getAddressLine(0) +", "
                            + addressList.get(3).getAddressLine(0) + ", "
                            + addressList.get(0).getCountryName();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ERR", "Geocoder failed");
                }

                p.pickupLon = l1.getLongitude();
                p.pickupLat = l1.getLatitude();
            }
            catch(SecurityException e){
                e.printStackTrace();
            }
            catch(Exception exc){
                exc.printStackTrace();
            }

            p.save();

        }
        catch(Exception e){
            return false;
        }

        String toastMessage = addressList == null ? "Package created." : "Package created on " + addressString;
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        return true;
    }

    public void openMap(View view){
        Intent i = new Intent(CreatePackage.this, Map.class);
        i.putExtra("lon", lon);
        i.putExtra("lat", lat);
        //i.put
        startActivity(i);
        //startActivity(new Intent(this, Map.class));
    }
}
