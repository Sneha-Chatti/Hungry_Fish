package com.example.hungry_fish;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    LinearLayout layout;
    LinearLayout[] row = new LinearLayout[4];
    ImageView title;
    TextView label;
    Button[] button = new Button[2];
    int width, height;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;

                layout = new LinearLayout(getBaseContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setBackgroundResource(R.drawable.splash);

                createArtifacts();

                title.setBackgroundResource(R.drawable.game_over);
                row[0].addView(title);

                label.setText("YOUR SCORE:"+HungryFish.scorePts);
                label.setTypeface(Typeface.DEFAULT_BOLD);
                label.setTextColor(Color.BLACK);
                label.setTextSize(width/25);
                label.setGravity(Gravity.CENTER_HORIZONTAL);
                row[1].addView(label);

                button[0].setBackgroundResource(R.drawable.reset);
                button[0].setText("RESET");
                button[0].setTextSize(height/30);
                button[0].setTextColor(Color.BLACK);
                button[0].setTypeface(Typeface.DEFAULT_BOLD);
                button[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread th = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent mainIntent = new Intent(GameOverActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                            }
                        });
                        th.start();
                        th.interrupt();
                    }
                });
                row[2].addView(button[0]);

                button[1].setBackgroundResource(R.drawable.exit);
                button[1].setText("EXIT");
                button[1].setTextSize(height/30);
                button[1].setTextColor(Color.BLACK);
                button[1].setTypeface(Typeface.DEFAULT_BOLD);
                button[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
                row[3].addView(button[1]);

                addRows();

                setContentView(layout);
            }

            public void createArtifacts() {
                for(int i = 0; i < row.length; i++){
                    row[i] = new LinearLayout(getBaseContext());
                    row[i].setLayoutParams(new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                    if( i == 0) {
                        title = new ImageView(getBaseContext());
                        title.setLayoutParams(new LinearLayout.LayoutParams(width, height/4));
                    }
                    else if(i == 1) {
                        label = new TextView(getBaseContext());
                        label.setLayoutParams(new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT));
                    }
                    else if(i >= 2){
                        button[i - 2] = new Button(getBaseContext());
                        button[i - 2].setLayoutParams(new LinearLayout.LayoutParams(width, height/6));
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
