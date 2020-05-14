package com.example.hungry_fish;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    LinearLayout layout;
    LinearLayout[] row = new LinearLayout[3];
    ImageButton[] button = new ImageButton[3];
    int width, height, i = 0;
    static boolean playSound = true, playMusic = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        setContentView(R.layout.activity_splash);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.drawable.splash);
        createArtifacts();

        button[0].setId(0);
        button[0].setBackgroundResource(R.drawable.sound);
        button[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playSound) {
                    button[0].setBackgroundResource(R.drawable.no_sound);
                    playSound = false;
                } else {
                    button[0].setBackgroundResource(R.drawable.sound);
                    playSound = true;
                }
            }
        });

        button[1].setBackgroundResource(R.drawable.music);
        button[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playMusic) {
                    button[1].setBackgroundResource(R.drawable.no_music);
                    playMusic = false;
                } else {
                    button[1].setBackgroundResource(R.drawable.music);
                    playMusic = true;
                }
            }
        });

        row[0].addView(button[0]);
        row[0].addView(button[1]);

        button[2].setBackgroundResource(R.drawable.play);
        button[2].setScaleType(ImageView.ScaleType.FIT_CENTER);
        button[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
        row[2].addView(button[2]);

        addRows();
        setContentView(layout);
    }

    public void createArtifacts() {
        for(int i = 0; i < row.length; i++){
            row[i] = new LinearLayout(this);
            row[i].setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                button[i] = new ImageButton(this);
                int buttonht = 15, buttonWt = 15;
                if(i == row.length - 1) {
                    button[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
                else {
                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams( width / buttonWt, height / buttonht);
                    button[i].setLayoutParams(ll);
                }
            }
    }

    public void addRows() {
        for(int i = 0; i < row.length; i++)
        {
            layout.addView(row[i]);
        }
    }
}