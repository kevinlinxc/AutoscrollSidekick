package com.example.guitarautoscroll;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity  implements imageAdapter.ItemClickListener{

    private RecyclerView recyclerView;
    private imageAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final int GALLERY=1;
    private ProgressDialog mBusyProgress = null;
    private Context mContext = this;
    private List<Tab> mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        RecyclerView rV= (RecyclerView) findViewById(R.id.imagesRecyclerView);
        rV.setLayoutManager(new LinearLayoutManager(this));
        mAdapter= new imageAdapter(this,mTabs);
        mAdapter.setClickListener(this);
        rV.setAdapter(mAdapter);
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

        return super.onOptionsItemSelected(item);
    }

    private void onNewImageTapped(View v){
        requestMultiplePermissions();
        choosePhotoFromGallery();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //This sends info to onActivityResult
        startActivityForResult(galleryIntent, GALLERY);
    }

    //handles the result of the picture that is chosen/taken
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
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
                                    String path = saveToInternalStorage(galleryPic);
                                    Date c = Calendar.getInstance().getTime();
                                    System.out.println("Current time = "+c);
                                    //code to change a date to string
                                    Tab tab = new Tab("Untitled",c,path);
                                    mTabs.add(tab);
                                    Intent intent = new Intent(mContext, ScrollActivity.class);
                                    intent.putExtra(ScrollActivity.PATH, path);
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
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"tab.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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
        Toast.makeText(this,
            "You clicked " + mAdapter.getItem(position) + " on row number " + position,
            Toast.LENGTH_SHORT).show();
    }
}
