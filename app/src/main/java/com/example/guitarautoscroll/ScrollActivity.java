package com.example.guitarautoscroll;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ScrollActivity extends AppCompatActivity {

    public static final String PATH = "Path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        String path = getIntent().getStringExtra(PATH);
        loadImageFromStorage(path);
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

}
