package com.example.student.mp3player;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean bReadPerm = false;
    boolean bWritePerm = false;
    Button button_play,button_stop,button_prev,button_next;


    MediaPlayer player;

    String[] playlist = {"/ha.mp3", "/po.mp3", "/sen.mp3", "/toto.mp3"};
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPermission();

        button_play = (Button)findViewById(R.id.button_play);
        button_stop = (Button)findViewById(R.id.button_stop);
        button_prev = (Button)findViewById(R.id.button_prev);
        button_next = (Button)findViewById(R.id.button_next);


        button_play.setOnClickListener(new MyButtonListener());
        button_stop.setOnClickListener(new MyButtonListener());
        button_prev.setOnClickListener(new MyButtonListener());
        button_next.setOnClickListener(new MyButtonListener());

        player = new MediaPlayer();

        if(bReadPerm && bWritePerm) {
            String state = Environment.getExternalStorageState();

            if(state.equals(Environment.MEDIA_MOUNTED)) {
                try {
                    String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + playlist[index];

                    player.setDataSource(musicPath);
                    player.prepare();
                    Log.d("PlayMp3", "mp3 file");
                }catch (Exception e) {
                    Log.d("PlayMp3", "mp3 file error");
                }
             }
         }
    }

    class MyButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_play:
                    if(player.isPlaying()) {
                        player.pause();
                        button_play.setText("play");
                    } else  {
                        player.start();
                        button_play.setText("pause");
                    }
                    break;
                case R.id.button_stop:
                    player.stop();
                    try {
                        player.prepare();
                    } catch (Exception e) {
                        Log.d("PlayMp3", "mp3 file error");
                    }
                    break;
                case R.id.button_prev:
                    if(player.isPlaying()){

                        player.stop();
                        player.reset();

                        index--;
                        if(index<0) {
                            index=3;
                        } else {
                            try {
                                String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + playlist[index];

                                player.setDataSource(musicPath);
                                player.prepare();
                                Log.d("PlayMp3", "mp3 file");
                            }catch (Exception e) {
                                Log.d("PlayMp3", "mp3 file error");
                            }

                            player.start();
                        }

                    }
                    break;
                case R.id.button_next:
                    if(player.isPlaying()){

                        player.stop();
                        player.reset();

                        index++;
                        if(index>3) {
                            index=0;
                        } else {
                            try {
                                String musicPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + playlist[index];

                                player.setDataSource(musicPath);
                                player.prepare();
                                Log.d("PlayMp3", "mp3 file");
                            }catch (Exception e) {
                                Log.d("PlayMp3", "mp3 file error");
                            }
                            player.start();
                        }

                    }
                    break;
            }
        }
    }

    private void setPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            bReadPerm = true;
        }

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            bWritePerm = true;
        }

        if(!bReadPerm && !bWritePerm) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 200);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] 									permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bReadPerm = true;
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                bWritePerm = true;
            }
        }
    }
}