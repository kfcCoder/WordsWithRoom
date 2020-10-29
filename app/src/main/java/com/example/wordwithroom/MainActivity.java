package com.example.wordwithroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import static com.example.wordwithroom.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    /**
     * 1. two adapters; one with data, the other one add data later -> 底層持久數據可共用(因為共用ViewModel)
     */

    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        mNavController = Navigation.findNavController(findViewById(R.id.fragment)); // R.id.fragment is NavHost
        NavigationUI.setupActionBarWithNavController(this, mNavController);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mNavController.navigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() { // 顯示標題欄的返回鍵號
        mNavController.navigateUp();
        return super.onSupportNavigateUp();
    }


}