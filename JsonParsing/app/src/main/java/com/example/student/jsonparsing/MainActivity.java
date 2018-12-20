package com.example.student.jsonparsing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_show = (TextView)findViewById(R.id.tv_show);

        String json =
                "{" +
                 "\"user\": \"gildong\", " +
                 "\"color\": [\"red\", \"green\",\"blue\"]" +
                  "}";

        try {
            JSONObject root = new JSONObject(json);

            String user_name = root.getString("user");
            JSONArray colors = root.getJSONArray("color");

            String first = colors.getString(0);
            String second = colors.getString(1);
            String third = colors.getString(2);

            for(int i=0;i<colors.length();i++) {
                Log.d("show colors", colors.getString(i));
            }

            String result = "user : " + user_name +
                    "\ncolor1 : " + first +
                    "\ncolor2 : " + second +
                    "\ncolor3 : " + third;

            tv_show.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
