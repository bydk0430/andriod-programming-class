package com.example.student.hellomovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText et_loginId, et_loginPw;
    Button btn_login;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        et_loginId = (EditText)findViewById(R.id.et_loginId);
        et_loginPw = (EditText)findViewById(R.id.et_loginPw);

        btn_login = (Button)findViewById(R.id.btn_login);

        sharedPref = getSharedPreferences("login_info", Context.MODE_PRIVATE);

        if(!sharedPref.getString("autoLogin", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this,
                                MovieListActivity.class);
            startActivity(intent);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(authUser(et_loginId.getText().toString(),
                        et_loginPw.getText().toString())) {
                    if (et_loginId.equals("") && !et_loginPw.equals("")) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("autoLogin", et_loginId.getText().toString());
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this,
                                MovieListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private boolean authUser(String id, String pw) {
        if(id.equals("")) {
            Toast.makeText(getApplicationContext(), "ID를 입력해주세요",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if(pw.equals("")) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // 비밀번호와 패스워드를 검증한다. 현재는 임시 코드
        if(id.equals("user") && pw.equals("1234")) {
            // 아이디와 비밀번호가 맞는 경우
            return true;
        } else {
            // 아이디와 비밀번호가 다른 경우
            return false;
        }
    }

}
