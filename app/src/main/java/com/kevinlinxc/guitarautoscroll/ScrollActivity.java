package com.kevinlinxc.guitarautoscroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kevinlinxc.guitarautoscroll.CustomClasses.ASApplication;
import com.kevinlinxc.guitarautoscroll.CustomClasses.Tab;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class ScrollActivity extends AppCompatActivity {

    public static final String PATH = "path";
    public static final String INDEX = "index";
    public static final String SPEED = "speed";
    public boolean playing=false;
    private ScrollView imageScroll;
    private Context mContext=this;
    private final Handler handler = new Handler();
    private int index;
    private Toolbar toolbar;
    private int speed;
    private List<Tab> mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        String path = getIntent().getStringExtra(PATH);
        Log.d("ScrollActivity","Pulling image from path:" + path);
        index = getIntent().getIntExtra(INDEX,0);
        speed = getIntent().getIntExtra(SPEED,80);
        loadImageFromStorage(path);
        imageScroll = (ScrollView)findViewById(R.id.imageScroll);
        mTabs= ASApplication.getmTabs();

        toolbar = (Toolbar) findViewById(R.id.toolbarScroll);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        final SeekBar sbar = (SeekBar) findViewById(R.id.sliderScroll);
        sbar.setProgress(speed);
        final TextView leftTV = (TextView)findViewById(R.id.leftScrollTV);
        leftTV.setText(getResources().getString(R.string.pixels)+sbar.getProgress());
        sbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                speed = pval;
                leftTV.setText(getResources().getString(R.string.pixels)+sbar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                leftTV.setText(getResources().getString(R.string.pixels)+sbar.getProgress());
                mTabs.get(index).setPixelSpeed(sbar.getProgress());
            }
        });
        final FloatingActionButton fab = findViewById(R.id.playFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playing){
                    Log.d("ScrollActivity","Clicked play");
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.icon_pause));
                    fab.setBackgroundColor(getResources().getColor(R.color.mainOrange));
                    playing=true;
                    getSupportActionBar().hide();
                    updateScroll();
                }else if(playing){
                    Log.d("ScrollActivity","Clicked pause");
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.icon_triangle));
                    playing=false;
                    getSupportActionBar().show();
                    updateScroll();

                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mTabs= ASApplication.getmTabs();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "tab.jpg"+index);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            PhotoView imageHolder = (PhotoView) findViewById(R.id.imagePage);
            imageHolder.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void updateScroll() {
        if (playing) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (playing) {
                        imageScroll.smoothScrollBy(0, Math.round(speed/40)
                        );
                    } else {
                        imageScroll.smoothScrollBy(0, 0);
                    }
                    handler.postDelayed(this, 25);
                }
            }, 500);
        } else {
            imageScroll.smoothScrollBy(0, 0);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        ASApplication.setmTabs(mTabs);
    }

    @Override
    public void onPause(){
        super.onPause();
        ASApplication.setmTabs(mTabs);
    }
}
