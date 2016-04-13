package com.example.android.androidcourse2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private class AsyncRunner extends AsyncTask<Person, Void, String>{

        @Override
        protected String doInBackground(Person... params) {

            Person p = params[0];
            String result = "AA00";
            HttpURLConnection con;
            try {
                URL urlPost = new URL("http://gotodelivery.azurewebsites.net/api/people/add");
                    con = (HttpURLConnection) urlPost.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
               // con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Content-Type", "application/json");

                    OutputStream os = con.getOutputStream();
                    Gson g = new Gson();
                    os.write(g.toJson(p).getBytes("utf-8"));
                    os.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK ||HttpResult == HttpURLConnection.HTTP_CREATED ) {

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                    //Toast.makeText(a, sb.toString(), Toast.LENGTH_LONG).show();
                } else {
                    return "HTTP Code " + HttpResult;
                }
                    //InputStreamReader isr = new InputStreamReader(connection.getInputStream());


            }
            catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //create ProgresBar.show()
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ProgressBar.cancel()
            SharedPreferences sp = getSharedPreferences(Constants.goTo, 0);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("ID",s);
            editor.commit();

            Toast.makeText(getBaseContext(), "User register with ID " + s,Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    //main activity
    boolean firstTime ;
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

        firstTime = getIntent().getBooleanExtra(Constants.firstTime,false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), PersonalInfoActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        long personID = getIntent().getLongExtra( Constants.personID, 0);
        String person = getIntent().getStringExtra(Constants.person);

        Person p = null;
        if(personID > 0 || person !=null)
        {

            if (personID > 0) Person.findById(Person.class, personID);
            else {
                Gson gson = new Gson();
                p = gson.fromJson(person, Person.class);
            }

            if (p == null) {
                Log.e("GOTO", "No Person found");
                return;
            }

            EditText et1 = (EditText) findViewById(R.id.name);
            et1.setText(p.name);

            EditText et2 = (EditText) findViewById(R.id.lastname);
            et2.setText(p.lastName);

            EditText et3 = (EditText)findViewById(R.id.address);
            et3.setText(p.address);

            EditText et4 = (EditText)findViewById(R.id.phone);
            et4.setText(p.phone);

            EditText et5 = (EditText)findViewById(R.id.email);
            et5.setText(p.email);

            Button takePhoto = (Button) findViewById(R.id.takePersonPhoto);
            takePhoto.setText("List Packages");

            //TODO check if person has any packages, to disable button
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, ListPackages.class);
                    //ARR i.putExtra(Constants.personID, p.getId());
                    startActivity(i);
                }
            });
        }

    }

    private class AsyncTaskRunner extends AsyncTask<JSONObject, Void, String > {

        ProgressDialog pd;
        Activity a;

        public AsyncTaskRunner(Activity a)
        {
            this.a = a;
            pd = new ProgressDialog(a);
        }

        @Override
        protected String doInBackground(JSONObject... params) {

            JSONObject personJson = params[0];
            HttpURLConnection con = null;
            try {
                URL urlPost = new URL("http://gotodelivery.azurewebsites.net/api/people/add");

                con = (HttpURLConnection) urlPost.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                //con.setRequestProperty("Accept", "application/json");

                OutputStream os = con.getOutputStream();
                os.write(personJson.toString().getBytes("utf-8"));
                os.flush();

//                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
//                wr.write(o.toString());

                //display what returns the POST request

                StringBuilder sb = new StringBuilder();
                int HttpResult = con.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK ||HttpResult == HttpURLConnection.HTTP_CREATED ) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
                    //Toast.makeText(a, sb.toString(), Toast.LENGTH_LONG).show();
                } else {
                    return "HTTP Code " + HttpResult;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            finally {
                if (con != null) con.disconnect();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage( "Wait while we register you...");
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.cancel();
            SharedPreferences sp = getSharedPreferences(Constants.goTo, 0);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("ID",s);
            editor.commit();
            Toast.makeText(a, "You personal info is saved on server with ID " + s, Toast.LENGTH_LONG).show();
        }
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


    Toast.makeText(getBaseContext(),"Saving user info to database", Toast.LENGTH_SHORT).show();
    //save to Database
        Person p = new Person();
        p.name = etName;
        p.lastName = etLastName;
        p.address = etAddress;
        p.phone = etPhone;
        p.email = etEmail;
        p.save();
        ImageView image = (ImageView)findViewById(R.id.profilePict);
        String photoFile = "PersonPhoto" + p.getId();
        //Bitmap b = ((BitmapDrawable)image.getDrawable()).getBitmap();
        if (imageBitmap != null) saveFile(this, imageBitmap,photoFile);
        p.photoFileName = photoFile;

        AsyncRunner ar = new AsyncRunner();
        ar.execute(p);

        if(firstTime){
            Toast.makeText(getBaseContext(),"Saving user info in settings", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences(Constants.goTo, 0);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("name",etName);
            editor.putString("lastName",etLastName);
            editor.putString("adress",etAddress);
            editor.putString("phone",etPhone);
            editor.putString("email",etEmail);

            editor.commit();

        }


    }

    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("GOTO", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("GOTO", "io exception");
            e.printStackTrace();
        }

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

    Bitmap imageBitmap=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = (ImageView)findViewById(R.id.profilePict);
        Bundle extras = data.getExtras();
        imageBitmap = (Bitmap) extras.get("data");
        image.setImageBitmap(imageBitmap);

    }
}
