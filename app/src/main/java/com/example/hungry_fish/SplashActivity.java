package com.example.hungry_fish;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends Activity {
    LinearLayout layout;
    LinearLayout[] row = new LinearLayout[4];
    ImageView banner;
    Button[] button = new Button[3];
    int width, height, i = 0;
    static boolean playSound = true, playMusic = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundResource(R.drawable.splash);

        createArtifacts();

        banner.setBackgroundResource(R.drawable.banner);
        row[0].addView(banner);

        button[0].setBackgroundResource(R.drawable.play);
        button[0].setText("PLAY");
        button[0].setTextSize(height/40);
        button[0].setTextColor(Color.BLACK);
        button[0].setTypeface(Typeface.DEFAULT_BOLD);
        button[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button[0].setBackgroundResource(R.drawable.pressed);
                button[0].setText("Loading...");
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                ProgressDialog dialog = ProgressDialog.show(SplashActivity.this, "", "Please Wait", true);
            }
        });
        row[1].addView(button[0]);

        button[1].setBackgroundResource(R.drawable.music);
        button[1].setText("Music: ON");
        button[1].setTextSize(height/40);
        button[1].setTextColor(Color.BLACK);
        button[1].setTypeface(Typeface.DEFAULT_BOLD);
        button[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playMusic)
                    button[1].setText("MUSIC: OFF");
                else
                    button[1].setText("MUSIC: ON");
                playMusic = !playMusic;
            }
        });
        row[2].addView(button[1]);

        button[2].setBackgroundResource(R.drawable.sound);
        button[2].setText("SOUND: ON");
        button[2].setTextSize(height/40);
        button[2].setTextColor(Color.BLACK);
        button[2].setTypeface(Typeface.DEFAULT_BOLD);
        button[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playSound)
                    button[2].setText("SOUND: OFF");
                else
                    button[2].setText("SOUND: ON");
                playSound = !playSound;
            }
        });
        row[3].addView(button[2]);

        addRows();
        setContentView(layout);
    }

    public void createArtifacts() {
        for(int i = 0; i < row.length; i++){
            row[i] = new LinearLayout(this);
            row[i].setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            if( i == 0) {
                banner = new ImageView(this);
                banner.setLayoutParams(new LinearLayout.LayoutParams(width, height/4));
            }
            if(i!=0) {
                button[i-1] = new Button(this);
                button[i-1].setLayoutParams(new LinearLayout.LayoutParams(width, height / 8));
            }
            }
    }

    public void addRows() {
        for(int i = 0; i < row.length; i++)
        {
            layout.addView(row[i]);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}