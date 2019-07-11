package com.myapplicationdev.p09quiz;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button save,read,show;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.editText);
        save = findViewById(R.id.btnSave);
        read = findViewById(R.id.btnRead);
        tv = findViewById(R.id.tv);
        show = findViewById(R.id.btnShow);



        if (checkPermission()) {
            String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
            File folder = new File(folderLocation);
            if (folder.exists() == false) {
                boolean result = folder.mkdir();
                if (result == true) {
                    Log.d("File Read/Write", "Folder created");
                }
            }
        } else {
            String msg = "Permission not granted to retrieve location info";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                    File targetFile_I = new File(folderLocation, "quiz.txt");
                    FileWriter writer_I = new FileWriter(targetFile_I, false);
                    String response = et.getText().toString();
                    writer_I.write(response);
                    writer_I.flush();
                    writer_I.close();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                    File targetFile = new File(folderLocation, "quiz.txt");

                    if (targetFile.exists() == true) {
                        String data = "";
                        try {
                            FileReader reader = new FileReader(targetFile);
                            BufferedReader br = new BufferedReader(reader);

                            String line = br.readLine();
                            while (line != null) {
                                data += line + "\n";
                                line = br.readLine();
                            }
                            br.close();
                            reader.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Log.d("Content", data);
                        tv.setText(data);
                    }
                } else {
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = et.getText().toString();
                String lat = response.split(",")[0].trim();
                String lng = response.split(",")[1].trim();

                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                startActivity(i);
            }
        });
    }

    private boolean checkPermission() {
        int permissionCheck_write = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_read = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck_write == PermissionChecker.PERMISSION_GRANTED && permissionCheck_read == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
