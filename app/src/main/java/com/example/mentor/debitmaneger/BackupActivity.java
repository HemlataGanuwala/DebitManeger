package com.example.mentor.debitmaneger;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class BackupActivity extends AppCompatActivity {

    DatabaseHelpher databaseHelpher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        databaseHelpher = new DatabaseHelpher(this);


    }

    public void backup() {
        try {
            final String inFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" + "DebitManeger.db";
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            String path = dbFile.getAbsolutePath();
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            String outFileName = DatabaseHelpher.DBLOCATIONBACKUP + "/" + "bck"; // output file name
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
}
