package com.example.visortia;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Sorter extends AppCompatActivity {

    public SharedPreferences pref;
    public static int cursor;
    public static int num_del;
    ArrayList<File> allFiles;

    Button dl_left;
    Button dl_right;
    Button bt_prev;
    Button bt_next;

    /**
     * https://stackoverflow.com/a/42156914
     * Apparently this is necessary after API 23???
     */
    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;



    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorter);

        requestForPermission();

        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Index all files, save in external xml
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", Context.MODE_PRIVATE);
        cursor = pref.getInt("cursor", 0);
        // https://stackoverflow.com/a/21810308
        pref.edit().putInt("cursor", cursor).commit();

        onStart();
    }

    protected void onStart()
    {
        super.onStart();
        setContentView(R.layout.sorter);


        //Do I really need to declare this every method?
        requestForPermission();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", Context.MODE_PRIVATE);
        cursor = pref.getInt("cursor", 0);
        num_del = pref.getInt("num_int", 0);

        new Thread(new Runnable() {
            public void run() {

//                for(int i = 0; i <  in dir)
//                {
//
//                }
//                File folder = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
                File folder = new File("/sdcard/DCIM/Camera/");
                Log.i("folder: ", folder.getAbsolutePath());
                //TODO: Change to accommodate for pref later
                allFiles = new ArrayList<>(); //Contains all img locations
//                Log.v("Files",folder.exists()+"");
//                Log.v("Files",folder.isDirectory()+"");
//                Log.v("Files",folder.listFiles()+"");
//                Log.v("Files",folder.canRead()+"");
//                if(folder.listFiles() != null)
                allFiles.addAll(Arrays.asList(folder.listFiles()));

                Log.i("Does this work?", "??");
                //DEBUGGING PURPOSES
                for (File name : allFiles){ //It's called for-each, come on me
                    Log.i("File name: ", name.getAbsolutePath());
                }

//                /**
//                 * https://stackoverflow.com/a/44587950
//                 */
//                File app_dir = new File("/data/Visortia/");
//                if(!app_dir.exists()) app_dir.mkdir();
////                try{
//                    File dir_xml = new File(app_dir, "dir.xml");
//                    FileWriter writer = new FileWriter(dir_xml);
//                    for (File name : allFiles) {
//                    // for(int i = 0; i < allFiles.length; i++) {
//                        writer.write(name.getPath()); //Save image path in dir.xml (why again?)
//                    }
//                    writer.flush();
//                    writer.close();
////                } catch(Exception e) {
////                    Log.e("IOException", "Exception in create new File(");
////                }
            }
        }).start();

        // ADDED EFLAKSDFJ
        Map<String, ?> allEntries = pref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }

        try{
            Toast.makeText(Sorter.this, "Loading image...",
                    Toast.LENGTH_SHORT).show();
//            Log.v("Cursor", getString(cursor));
//            Log.i("File name VIEWER: ", allFiles.get(cursor).toString());
            load_image(allFiles.get(cursor)); //TODO: CHANGE BACK TO CURSOR LATER
        } catch(Exception e) {
            Log.e("IOException", "Exception in load list lkasdjflkajsdf");
        }

        // load_image((File) sset.toArray()[cursor]);
//        }
        //"data/data/Visortia/image_libraries.xml".getResources()
        //TODO: convert this line into XML-friendly

        dl_left = (Button) findViewById(R.id.delete_left);
        dl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFiles.get(cursor).delete();
                Toast.makeText(Sorter.this, "Successfully deleted left!",
                        Toast.LENGTH_SHORT).show();
                setContentView(R.layout.sorter);
                onStart();
            }
        });

        dl_right = (Button) findViewById(R.id.delete_right);
        dl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFiles.get(cursor).delete();
                Toast.makeText(Sorter.this, "Successfully deleted right!",
                        Toast.LENGTH_SHORT).show();
                setContentView(R.layout.sorter);
                onStart();
            }
        });

        bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                Toast.makeText(Sorter.this, "Successfully moved next!",
                        Toast.LENGTH_SHORT).show();
                setContentView(R.layout.sorter);
                onStart();
            }
        });

        bt_prev = (Button) findViewById(R.id.bt_prev);
        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
                Toast.makeText(Sorter.this, "Successfully moved prev!",
                        Toast.LENGTH_SHORT).show();
                setContentView(R.layout.sorter);
                onStart();
            }
        });
    }

    /**
     * Back button in Toolbar
     * http://www.freakyjolly.com/how-to-add-back-arrow-in-android-activity/
     * ABSOLUTELY NECESSARY, DO NOT REMOVE!!!
     */

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * All image-related work goes here
     */



    private ImageView mImageView;

    public void load_image(File imgfile)
    {
        /**
         * https://stackoverflow.com/a/39244349
         */
        if (imgfile.exists()) {
            mImageView = (ImageView) findViewById(R.id.imageView1);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap selectDrawable = BitmapFactory.decodeFile(imgfile.getAbsolutePath(), options);
            mImageView.setImageBitmap(selectDrawable);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "File not Exist", Toast.LENGTH_SHORT).show();
        }
    }

    public void prev() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", Context.MODE_PRIVATE);

        if(cursor > 0) {
            cursor--;
        }
        else {cursor = allFiles.size() - 1;}
        // https://stackoverflow.com/a/21810308
        pref.edit().putInt("cursor", cursor).commit();
        if(cursor <= 0 || cursor >= allFiles.size()) {
            Toast.makeText(Sorter.this, "Cursor error", Toast.LENGTH_SHORT).show();
            cursor = 0;
        }
    }

    public void next() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("mPref", Context.MODE_PRIVATE);
        Log.d("allFiles.size()", String.valueOf(allFiles.size()));
        if(cursor < allFiles.size() - 1) //{
            cursor ++; //return; }
        else {cursor = 0;}

        // https://stackoverflow.com/a/21810308
        pref.edit().putInt("cursor", cursor).commit();

        if(cursor >= allFiles.size()) {
            Toast.makeText(Sorter.this, "Cursor error", Toast.LENGTH_SHORT).show();
            cursor = 0;
        }
    }
}
