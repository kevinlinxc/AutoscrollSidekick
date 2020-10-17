package com.kevinlinxc.guitarautoscroll;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.kevinlinxc.guitarautoscroll.CustomClasses.ASApplication;
import com.kevinlinxc.guitarautoscroll.CustomClasses.Tab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity  implements ImageAdapter.ItemClickListener{

    private RecyclerView rV;
    private ImageAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    private static final int GALLERY=1;
    private ProgressDialog mBusyProgress = null;
    private Context mContext = this;
    public static List<Tab> mTabs;
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);
        getSupportActionBar().setElevation(0);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewImageTapped(view);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            }
        });
        firstTime = getSharedPreferences("PREFERENCES", MODE_PRIVATE)
            .getBoolean("firstTime", true);
        getSharedPreferences("PREFERENCES", MODE_PRIVATE).edit()
            .putBoolean("firstTime", false).apply();

        rV= (RecyclerView) findViewById(R.id.imagesRecyclerView);
        rV.setLayoutManager(layoutManager);
        loadData();
        setRecycler();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (firstTime){
            startTutorial();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.clear_tabs){
            clearTabs();
        }

        if(id == R.id.help){
            startTutorial();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onNewImageTapped(View v){
        requestMultiplePermissions();
        choosePhotoFromGallery();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //allows selection of multiple images, which I'm trying to use for pic stitching
        //galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //This sends info to onActivityResult
        startActivityForResult(galleryIntent, GALLERY);
    }

    //handles the result of the picture that is chosen/taken
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                final Uri contentURI = data.getData();
                final CoordinatorLayout cLayout =
                    (CoordinatorLayout) findViewById(R.id.mainActivityLayout);
                showBusyIndicator();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            final Bitmap galleryPic =
                                MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), contentURI);
                            cLayout.post(new Runnable() {
                                public void run() {
                                    String[] paths = saveToInternalStorage(galleryPic);
                                    Date c = Calendar.getInstance().getTime();
                                    //code to change a date to string
                                    Tab tab = new Tab("Untitled",c,paths[0], paths[1],80);

                                    mTabs.add(tab);
                                    Intent intent = new Intent(mContext, ScrollActivity.class);
                                    intent.putExtra(ScrollActivity.PATH, paths[1]);
                                    intent.putExtra(ScrollActivity.INDEX, mTabs.indexOf(tab));
                                    intent.putExtra(ScrollActivity.SPEED,tab.getPixelSpeed());
                                    runOnUiThread(new Thread(new Runnable() {
                                        public void run() {
                                            hideBusyIndicator();
                                        }
                                    }));
                                    startActivity(intent);
                                }
                            });
                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setRecycler();
    }

    private String[] saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"tab.jpg"+mTabs.size());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String [] paths = {mypath.getAbsolutePath(), directory.getAbsolutePath(),};
        return paths;
    }
    //permissions necessary for camera
    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        Log.d("dev", "All permissions are granted!");
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        Log.d(TAG, "Permission Denied");
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    token.continuePermissionRequest();
                }
            }).
            withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                }
            })
            .onSameThread()
            .check();
    }

    private void showBusyIndicator() {
        this.mBusyProgress = new ProgressDialog(this);
        this.mBusyProgress.setTitle("Busy");
        this.mBusyProgress.setMessage("Processing...");
        this.mBusyProgress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        this.mBusyProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.mBusyProgress.getWindow().
            setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        this.mBusyProgress.show();
    }

    private void hideBusyIndicator() {
        this.mBusyProgress.dismiss();
        this.mBusyProgress = null;
    }

    @Override
    public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, ScrollActivity.class);
                intent.putExtra(ScrollActivity.PATH, mTabs.get(position).getDirectoryPath());
                intent.putExtra(ScrollActivity.INDEX, position);
                intent.putExtra(ScrollActivity.SPEED, mTabs.get(position).getPixelSpeed());
                startActivity(intent);
    }
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",
            MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mTabs);
        editor.putString("tab list", json);
        editor.apply();
        ASApplication.setmTabs(mTabs);
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",
            MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("tab list", null);
        Type type = new TypeToken<List<Tab>>() {}.getType();
        mTabs = gson.fromJson(json, type);
        ASApplication.setmTabs(mTabs);

        if (mTabs == null){
            mTabs = new ArrayList<>();
            Log.d("Main Activity","There were no prexisting tabs");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    public void clearTabs(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        if (dir.isDirectory()){
            File[] children = dir.listFiles();
            Log.d("MainActivity","Files being deleted: "+children);
            for (int i = 0; i<children.length;i++){
                children[i].delete();
            }
        }
        mTabs.clear();
        setRecycler();
    }

    public void setRecycler(){
        mAdapter= new ImageAdapter(this,mTabs);
        mAdapter.setClickListener(this);
        rV.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void startHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void startTutorial() {
        Intent intent = new Intent(this, TutorialActivity.class);
        intent.putExtra("fromActivity", "MainActivity");
        startActivity(intent);
    }
}
