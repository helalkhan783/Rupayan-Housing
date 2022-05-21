package com.rupayan_housing.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rupayan_housing.R;
import com.rupayan_housing.localDatabase.MyDatabaseHelper;
import com.rupayan_housing.localDatabase.PreferenceManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(MainActivity.this);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);    }


       /*

        *//**
         * create a background Thread For handle token validation
         *//*
        MyThread myTask = new MyThread();
        Timer myTimer = new Timer();
        int time = (1000 * 60) * 58;
        myTimer.schedule(myTask, time, time);//start scheduler

        */


    }

    /**
     * create a task for delete user credentials after a limited time (Sequentially)
     */
    class MyThread extends TimerTask {

        @Override
        public void run() {
            try {
                PreferenceManager.getInstance(MainActivity.this).deleteUserCredentials();
                PreferenceManager.getInstance(MainActivity.this).deleteUserPermission();
                PreferenceManager.getInstance(MainActivity.this).deletePassword();
                PreferenceManager.getInstance(MainActivity.this).deletePassword();
            } catch (Exception e) {
                Log.d("ERROR", e.getLocalizedMessage());
            }
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDatabaseHelper.deleteAllData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//myDatabaseHelper.deleteAllData();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        myDatabaseHelper.deleteAllData();
    }
}