package com.example.student.permission2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    EditText et_content;
    Button btn_save, btn_img;

    boolean fileReadPermission;
    boolean fileWritePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_content = (EditText)findViewById(R.id.et_content);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_img = (Button)findViewById(R.id.btn_img);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            fileReadPermission = true;
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            fileWritePermission = true;
        }

        if(!fileReadPermission && !fileWritePermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },200);
        }

        btn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OutputStream outputStream = null;

                try {
                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/myApp";
                    File dir = new File(dirPath);

                    if(!dir.exists()) {
                        dir.mkdir();
                    }

                    File file = new File(dirPath+"/myImage.png");

                    if(!file.exists()) {
                        file.createNewFile();
                    }

                    outputStream = new FileOutputStream(file);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                            R.drawable.smile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                    outputStream.close();

                    Intent intent = new Intent(MainActivity.this, ReadImgFileActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();

                if(fileReadPermission && fileWritePermission) {
                    String state = Environment.getExternalStorageState();

                    if(state.equals(Environment.MEDIA_MOUNTED)) {

                        FileWriter writer;

                        try {
                            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                    + "/myApp";
                            File dir = new File(dirPath);

                            if(!dir.exists()) {
                                dir.mkdir();
                            }

                            File file = new File(dir + "/myfile.txt");

                            if(!file.exists()) {
                                file.createNewFile();
                            }

                            writer = new FileWriter(file, true);
                            writer.write(content);
                            writer.flush();
                            writer.close();

                            Intent intent = new Intent(MainActivity.this,
                                    ReadFileActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "SD 카드가 마운트 되지 않았습니다",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "퍼미션이 부여되지 않았습니다",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fileReadPermission = true;
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fileWritePermission = true;
            }
        }
    }
}
