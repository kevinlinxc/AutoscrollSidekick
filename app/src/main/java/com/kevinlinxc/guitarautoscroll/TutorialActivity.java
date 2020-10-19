package com.kevinlinxc.guitarautoscroll;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import static android.content.ContentValues.TAG;

public class TutorialActivity extends AppCompatActivity {

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private int numberscreens = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ViewPager viewPager = (ViewPager) findViewById(R.id.tutorialViewPager);
        viewPager.setAdapter(new TutorialPagerAdapter(this));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Remove shadow from action bar (nav bar)
            actionBar.setElevation(0);

            // Hide action bar title
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Button ib = (Button) findViewById(R.id.closeTutorial);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "Back button of tutorial pressed");
                backToActivity();
            }
        });

        // Hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void createDots(int index){
        if(Dots_Layout !=null){
            Dots_Layout.removeAllViews();
            dots = new ImageView[numberscreens];

            for(int i = 0; i<numberscreens;i++){
                dots[i] = new ImageView(this);
                if(i == index){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,
                        R.drawable.active_dots));
                }
                else{
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,
                        R.drawable.inactive_dots));
                }
                LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4,0,4,0);
                Dots_Layout.addView(dots[i],params);
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void backToActivity() {
        String fromActivity = getIntent().getStringExtra("fromActivity");
        //Log.d(TAG, fromActivity);
        if (fromActivity.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
