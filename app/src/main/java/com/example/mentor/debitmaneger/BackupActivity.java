package com.example.mentor.debitmaneger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;

public class BackupActivity extends AppCompatActivity {

    DatabaseHelpher databaseHelpher;
    Button buttonbackup,buttonimport;
    String PathHolder,cdate;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        databaseHelpher = new DatabaseHelpher(this);
        buttonbackup = (Button)findViewById(R.id.btnbackup);
        buttonimport = (Button)findViewById(R.id.btnimport);

        Date d = new Date();
        CharSequence g = DateFormat.format("dd/MM/yyyy", d.getTime());
        cdate = g.toString();

        buttonbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                {
                    if (checkPermission())
                    {

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.setType("folder/*");
                        startActivityForResult(intent, 7);
                        // Code for above or equal 23 API Oriented Device
                        // Your Permission granted already .Do next code
                        backup();
                    } else {
                        requestPermission(); // Code for permission
                    }
                }
                else
                {

                    // Code for Below 23 API Oriented Device
                    // Do next code
                }

            }
        });

        buttonimport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(copyDatabase()) {
                    Toast.makeText(BackupActivity.this, "Data Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BackupActivity.this, "Copy data error", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(BackupActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(BackupActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(BackupActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(BackupActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 7:

                if(resultCode==RESULT_OK){

                    PathHolder = data.getData().getPath();
                    //PathHolder = data.getDataString();

                }

                break;

        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA );
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }



    public void backup() {
        try {
            final String inFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + "DebitCredit";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            String path = dbFile.getAbsolutePath();
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            String outFileName = DatabaseHelpher.DBLOCATIONBACKUP + "/" + "DebitorCreditor" + cdate + ".db"; // output file name
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[3000];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            Toast.makeText(BackupActivity.this, "Backup Successfully",Toast.LENGTH_LONG).show();
            // Close the streams
            output.flush();
            output.close();
            fis.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean copyDatabase() {
        File dbFile = Environment.getExternalStorageDirectory();
        String path = dbFile.getAbsolutePath() + "/" + "DebitManeger.db";
        if (!path.endsWith(".db")) {
            path += ".db" ;
        }
        try {
            //File myFile = new File(path);
            FileInputStream inputStream = new FileInputStream(path);
            String outFileName = DatabaseHelpher.DBLOCATION + "/" + DatabaseHelpher.DATABSE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[3000];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
