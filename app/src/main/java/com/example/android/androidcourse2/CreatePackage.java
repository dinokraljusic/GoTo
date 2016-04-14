package com.example.android.androidcourse2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreatePackage extends AppCompatActivity {

    private class AsyncRunner extends AsyncTask<Paket, Void, String> {

        @Override
        protected String doInBackground(Paket... params) {
            Paket p = params[0];

            try {
                URL url = new URL("http://gotodelivery.azurewebsites.net/api/packets/add");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.addRequestProperty("Content-Type","application/json");
                OutputStream os = con.getOutputStream();
                Gson g = new Gson();
//                String jj = "{\n" +
//                        "    \"senderId\" : 1,\n" +
//                        "    \"receiverId\": 2,\n" +
//                        "    \"transporterId\": 3,\n" +
//                        "    \"packetStatusId\": 2,\n" +
//                        "    \"packetTypeId\": 7,\n" +
//                        "    \"destination\": \"Livno\"\n" +
//                        "}";
//                os.write(jj.getBytes("utf-8"));
                JSONObject jo = new JSONObject(g.toJson(p));
                jo.remove("id");

                os.write(jo.toString().getBytes());
                os.flush();
                Log.d("WEB_LOG", jo.toString());

                int code = con.getResponseCode();

                if (code == HttpURLConnection.HTTP_CREATED) {
                    InputStreamReader isr = new InputStreamReader(con.getInputStream());

                    StringBuilder sb1 = new StringBuilder();
                    int data = isr.read();
                    while (data != -1) {
                        sb1.append((char) data);
                        data = isr.read();
                    }
                    return sb1.toString(); //TODO read stream
                }
                else {
                    Log.d("WEB_LOG", "Error HTTP Code " + code );
                    return "Error";
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getBaseContext(), "Paket registered " + s, Toast.LENGTH_LONG).show();
        }
    }
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

            if (outputFileUri != null) p.Photo = outputFileUri.toString();
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
            p.senderId = Integer.valueOf(1); //TODO change SenderID to Long
            p.packetStatusId = Paket.Status.PickedUp.ordinal() + 1;
            p.transpoterId = 3; //TODO add real transporter
            p.receiverId = 2;
//            SharedPreferences settings = getSharedPreferences("ID",0);
//            p.transpoterId = settings.getLong(Constants.personID,0L);
//
            Spinner type = (Spinner) findViewById(R.id.typespinner);
           // Paket.Type t = Paket.Type.values()[type.getSelectedItemPosition()];
            p.packetTypeId = type.getSelectedItemPosition() + 1;
            p.senderId = personID;

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
                Log.e("CreatePackage", e.toString());
                e.printStackTrace();
            }
            catch(Exception exc){
                Log.e("CreatePackage", exc.toString());
                exc.printStackTrace();
            }

            p.save();
            AsyncRunner ar = new AsyncRunner();
            ar.execute(p);
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
