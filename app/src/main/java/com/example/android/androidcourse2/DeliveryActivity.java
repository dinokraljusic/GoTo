package com.example.android.androidcourse2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    long paketID;
    Location l1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        paketID = getIntent().getLongExtra(Constants.paketID,0);

        TextView deliveryDate = (TextView) findViewById(R.id.deliveryDate);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        deliveryDate.setText(deliveryDate.getText() + " " + currentDateTimeString);

        TextView deliveryLocation = (TextView) findViewById(R.id.deliveryLocation);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        try {
            Geocoder gc = new Geocoder(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            l1 = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if(l1==null){
                l1=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if(l1==null) l1=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            List<Address> addressList =gc.getFromLocation(l1.getLatitude(), l1.getLongitude(),3);
            String addressString= addressList.get(0).getAddressLine(0) +", "+ addressList.get(1).getAddressLine(0) +", "
                    + addressList.get(3).getAddressLine(0) + ", " + addressList.get(0).getCountryName();
           deliveryLocation.setText(addressString );
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public Uri imageUri = null;
    public int TAKE_PHOTO_CODE = 100;
    String FILENAME = "delivery_picture";
    File dir = Environment.getExternalStorageDirectory();
    String path;
    File newfile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        path = dir.toString() + "/" +FILENAME;

        if(requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK){

            ImageView iv = (ImageView)findViewById(R.id.imageView);
            iv.setImageURI(Uri.parse(path));
            imageUri = Uri.parse(path);

            //Bitmap bmp = BitmapFactory.decodeFile(path);
            //iv.setImageBitmap(bmp);

            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //iv.setImageBitmap(imageBitmap);
            //Toast.makeText(this,"captured", Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadPhoto(View view){

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

    public void saveComment(View view){

        try {
//            DeliveryData deliveryData= new DeliveryData();
//            deliveryData.Photo=imageUri;
//            EditText editText=(EditText)findViewById(R.id.deliveryComment);
//            deliveryData.Comment=editText.getText().toString();
//            deliveryData.save();

            Paket p = Paket.findById(Paket.class, paketID);
            p.deliveryDate = new Date(); //current time
            if (l1 != null) {
                p.pickupLat = l1.getLatitude();
                p.pickupLon = l1.getLongitude();
            }
            p.ReceiverID = 999;
            p.status = Paket.Status.Delivered.name();
            p.save();

            Person person = Person.findById(Person.class, p.SenderID);
            //TODO send conformation email or SMS to senderID
            String emailBody = "Paket delivered on " + p.deliveryDate.toString() + " to " + p.ReceiverID + " on location ";
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{person.email});
            i.putExtra(Intent.EXTRA_SUBJECT, "GOTO Delivery");
            i.putExtra(Intent.EXTRA_TEXT   , emailBody);
           // i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(p.));

            try {
                startActivity(i);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(DeliveryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            Log.e("DELIVERY", ex.getMessage());
        }
    }

}
