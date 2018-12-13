package com.example.student.intentquiz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);

        IntentBtnListener intentBtnListener = new IntentBtnListener();

        button.setOnClickListener(intentBtnListener);
    }

    class IntentBtnListener implements View.OnClickListener {
        Intent intent = null;

        @Override
        public void onClick(View v) {
            String numbers = "tel:" + editText.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(numbers));
            startActivity(intent);
            }
        }
    }

