package com.example.android.androidcourse2;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        TextView deliveryDate = (TextView)findViewById(R.id.deliveryDate);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        deliveryDate.setText(deliveryDate.getText()+" "+ currentDateTimeString);

        TextView deliveryLocation = (TextView)findViewById(R.id.deliveryLocation);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location l1=null;

        try{
            Geocoder gc = new Geocoder(this);
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
        catch(SecurityException e){
            e.printStackTrace();
        } catch (IOException e) {
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
            DeliveryData deliveryData= new DeliveryData();
            deliveryData.Photo=imageUri;
            EditText editText=(EditText)findViewById(R.id.deliveryComment);
            deliveryData.Comment=editText.getText().toString();
deliveryData.save();
        }
        catch (Exception ex){}
    }

}
