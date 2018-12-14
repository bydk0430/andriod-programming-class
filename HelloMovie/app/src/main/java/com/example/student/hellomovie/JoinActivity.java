package com.example.student.hellomovie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by student on 2018-12-12.
 */

public class JoinActivity extends AppCompatActivity {

    TextView tv_msg;
    EditText et_id, et_pw, et_rePw;
    Button btn_join;

    Boolean isIdChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 2. 컴포넌트의 객체 만들기
        setComponent();

        // 4 리스너의 객체를 만들기
        IdCheckListener idCheckListener = new IdCheckListener();

        //5. 리스너 객체를 컴포넌트에 등록하기
        et_id.setOnFocusChangeListener(idCheckListener);
        et_rePw.setOnFocusChangeListener(new PwCheckListener());
        btn_join.setOnClickListener(new JoinBtnListener());
    }

    //3 리스너의 클래스 만들기
    class JoinBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(isIdChecked) {
                Toast.makeText(JoinActivity.this, "회원이 되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(JoinActivity.this, "올바른 정보를 입력하세요.", Toast.LENGTH_LONG).show();
            }

        }
    }
    class IdCheckListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            String user_input_id = et_id.getText().toString();
            if(user_input_id.equals("")) return;
            int num_of_id = user_input_id.length();

            if(num_of_id >= 5) {
                if(num_of_id <= 12) {
                    tv_msg.setText("정상적인 아이디 입니다.");
                    isIdChecked = true;
                } else {
                    tv_msg.setText("비 정상적인 아이디 입니다.\n(아이디의 글자수가 12보다 작아야합니다.)");
                    isIdChecked = false;
                }
            } else {
                tv_msg.setText("비 정상적인 아이디 입니다.\n(아이디의 글자 수가 5보다 커야합니다.)");
                isIdChecked = false;
            }
        }
    }

    class PwCheckListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            String user_pw = et_pw.getText().toString();
            String user_pw_re = et_rePw.getText().toString();

            if(user_pw.equals(user_pw_re)) {
                tv_msg.setText("정상적인 비밀번호 입니다.");
            } else {
                tv_msg.setText("비밀번호가 일치하지 않습니다.");
                isIdChecked = false;
            }
        }
    }


    private void setComponent() {
        tv_msg = (TextView)findViewById(R.id.tv_msg);
        et_id = (EditText)findViewById(R.id.et_id);
        et_pw = (EditText)findViewById(R.id.et_pw);
        et_rePw = (EditText)findViewById(R.id.et_rePw);
        btn_join = (Button)findViewById(R.id.btn_join);
    }

}