package com.example.android.androidcourse2;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PersonalInfoActivity extends AppCompatActivity {

    final String ACTIVITY_LIFECYCLE = "Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Toast.makeText(this, "OnCreate event in PersonalInfoActivity", Toast.LENGTH_LONG).show();
        Log.i(ACTIVITY_LIFECYCLE, "onCreate");

        final ActionBar actionBar = getActionBar();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("http://www.google.com"));
//        startActivity(intent);
    //    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
     //   TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
     //   tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ACTIVITY_LIFECYCLE, "onRestart");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(ACTIVITY_LIFECYCLE, "onConfigurationChanged");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_LIFECYCLE, "onStart");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast t = new Toast(this);
        t.setDuration(Toast.LENGTH_LONG);
        t.setView(View.inflate(this, R.layout.custom_toast, null));
        t.show();
        Log.i(ACTIVITY_LIFECYCLE, "onDestroy");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_LIFECYCLE, "onResume");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_LIFECYCLE, "onStop");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_LIFECYCLE, "onPause");
    }


    public void startNewActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
