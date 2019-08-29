package com.example.guitarautoscroll;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class ScrollActivity extends AppCompatActivity {

    public static final String PATH = "Path";
    public boolean playing=false;
    private ScrollView imageScroll;
    private Context mContext=this;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        String path = getIntent().getStringExtra(PATH);
        Log.d("ScrollActivity","Pulling image from path:" + path);

        loadImageFromStorage(path);
        imageScroll = (ScrollView)findViewById(R.id.imageScroll);
        final FloatingActionButton fab = findViewById(R.id.playFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!playing){
                    Log.d("ScrollActivity","Clicked play");
                    //this code never runs
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.icon_pause));
                    fab.setBackgroundColor(getResources().getColor(R.color.mainOrange));
                    playing=true;
                    updateScroll();
                }else if(playing){
                    Log.d("ScrollActivity","Clicked pause");
                    //this code also never runs
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.icon_triangle));
                    playing=false;
                    updateScroll();

                }
            }
        });
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "tab.jpg");
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
                        imageScroll.smoothScrollBy(0, 2
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
}
