package com.example.android.androidcourse2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonList extends AppCompatActivity {

    ListView listView;
    ProgressDialog pd;

    private class AsyncTaskRunner extends AsyncTask<String, List<Person>, List<Person> > {

        @Override
        protected List<Person> doInBackground(String... params) {

            String sss = params[0];
            HttpURLConnection con = null;
//            try {

                /****
                URL urlPost = new URL("http://gotodelivery.azurewebsites.net/api/people/add");

                con = (HttpURLConnection) urlPost.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");

                JSONObject o = new JSONObject();
                o.put("name", "Simon");
                o.put("lastName", "Cowel");
                o.put("phone", "065465456");
                o.put("address", "Sunset boulevard 32");
                o.put("personTypeId", 2);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(o.toString());
                ****/
                //display what returns the POST request

//                StringBuilder sb = new StringBuilder();
//                int HttpResult = con.getResponseCode();
//                if (HttpResult == HttpURLConnection.HTTP_OK) {
//                    BufferedReader br = new BufferedReader(
//                            new InputStreamReader(con.getInputStream(), "utf-8"));
//                    String line = null;
//                    while ((line = br.readLine()) != null) {
//                        sb.append(line + "\n");
//                    }
//                    br.close();
//                    System.out.println("" + sb.toString());
//                } else {
//                    System.out.println(con.getResponseMessage());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//            finally {
//                if (con != null) con.disconnect();
//            }


            try {
                URL url =  new URL("http://gotodelivery.azurewebsites.net/api/people");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                //c.setConnectTimeout();
                InputStream in = c.getInputStream();
                InputStreamReader isr = new InputStreamReader(in);

                StringBuilder sb1 = new StringBuilder();
                int data = isr.read();
                while (data != -1) {
                    sb1.append((char) data);
                    data = isr.read();
                }
//                isr.close();
//                c.disconnect();

                JSONArray ja = new JSONArray(sb1.toString());
                int count = 0;
                Gson gson = new Gson();
                List<Person> pl = new ArrayList<>();
                for(int i=0; i < ja.length();i++)
                {
                    JSONObject jo = ja.getJSONObject(i);
                    Person p = gson.fromJson(jo.toString(), Person.class);
                    pl.add(p);
                }
                //ARR String res = sb1.toString();
                return pl;

            }
            catch (Exception e) {

                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Person> pl) {
            super.onPostExecute(pl);
            pd.cancel();
            listView.setAdapter(new PersonAdapter(getBaseContext(), pl));
            //Toast.makeText(PersonList.this, pl.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
        listView = (ListView) findViewById(R.id.personlist);
       boolean FromCreatePackageButton = getIntent().getBooleanExtra(Constants.FromCreatePackageButton,false);

        if (FromCreatePackageButton){
            listView.setAdapter(new PersonAdapter(this, Person.listAll(Person.class)));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(PersonList.this, CreatePackage.class);
                    i.putExtra(Constants.personID, id);
                    startActivity(i);
                }
            });
        }
        else {

            boolean getPersonsFromWebService = true;

           if (true) {
               AsyncTaskRunner astr = new AsyncTaskRunner();
               pd = ProgressDialog.show(this, "Loading", "Wait while we get people...");
               astr.execute(" "); //pass in url for web service
           }
            else {
               listView.setAdapter(new PersonAdapter(this, Person.listAll(Person.class)));
           }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(PersonList.this, MainActivity.class);
                    i.putExtra(Constants.personID, id);
                    startActivity(i);
                }
            });
        }
    }
}
