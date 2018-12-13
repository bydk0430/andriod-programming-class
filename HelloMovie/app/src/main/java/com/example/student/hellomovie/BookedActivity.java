package com.example.student.hellomovie;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BookedActivity extends AppCompatActivity {

    Button btn_call,btn_web,btn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        btn_call = (Button)findViewById(R.id.btn_call);
        btn_web = (Button)findViewById(R.id.btn_web);
        btn_map = (Button)findViewById(R.id.btn_map);

        IntentBtnListener intentBtnListener = new IntentBtnListener();

        btn_call.setOnClickListener(intentBtnListener);
        btn_web.setOnClickListener(intentBtnListener);
        btn_map.setOnClickListener(intentBtnListener);

    }

    class IntentBtnListener implements View.OnClickListener {
        Intent intent = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn_call:
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("tel:012-3456-7890"));
                    break;
                case R.id.btn_web:
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.cgv.co.kr"));
                    break;
                case R.id.btn_map:
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("geo:36.6349120,127.4869820"));
                    break;

            }
            if(intent != null) {
                startActivity(intent);
            }
        }
    }
}
