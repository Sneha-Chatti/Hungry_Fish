package com.example.hungry_fish;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    // Our object to handle the View
    private HungryFish hungryFish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hungryFish = new HungryFish(this);
        setContentView(hungryFish);
    }
}