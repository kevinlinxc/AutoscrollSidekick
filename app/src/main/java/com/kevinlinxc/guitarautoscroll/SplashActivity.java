package com.kevinlinxc.guitarautoscroll;

import android.content.Intent;
import android.os.Bundle;

import com.kevinlinxc.guitarautoscroll.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

