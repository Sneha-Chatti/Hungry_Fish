package com.example.hungry_fish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity {
    // Our object to handle the View
    private HungryFish hungryFish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hungryFish = new HungryFish(this);
        setContentView(hungryFish);
    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(mainIntent);
    }
}