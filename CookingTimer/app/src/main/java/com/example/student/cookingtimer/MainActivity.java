package com.example.student.cookingtimer;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 컴포넌트의 객체를 저장할 수 있는 변수
    EditText editText_min, editText_sec;
    TextView textView_time;
    Button button_start, button_stop,
            button_30s, button_10m, button_30m, button_reset;
    ProgressBar progressBar_main;
    // 분, 초를 저장하는 변수
    int min, sec;
    // 시간 처리를 해주는 AsyncTask 변수
    CookTimerAsyncTask timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setComponents();
    }

    public String printMin(int Min) {
        Log.d("number", Integer.toString(min));
        if(min < 10) {
            return "0"+Integer.toString(min);
        }
        return Integer.toString(min);
    }

    public String printSec(int sec) {
        if(sec<10) {
            return "0"+Integer.toString(sec);
        }
        return Integer.toString(sec);
    }

    public void setComponents() {
        // 컴포넌트의 객체를 만든다
        progressBar_main = (ProgressBar)findViewById(R.id.progressBar_main);
        editText_min = (EditText)findViewById(R.id.editText_min);
        editText_sec = (EditText)findViewById(R.id.editText_sec);
        textView_time = (TextView)findViewById(R.id.textView_time);
        button_start = (Button)findViewById(R.id.button_start);
        button_stop = (Button)findViewById(R.id.button_stop);
        button_reset = (Button)findViewById(R.id.button_reset);
        button_10m = (Button)findViewById(R.id.button_10m);
        button_30s = (Button)findViewById(R.id.button_30s);
        button_30m = (Button)findViewById(R.id.button_30m);

        button_30s.setOnClickListener(new View.OnClickListener() {
            String temp_sec, temp_min;
            @Override
            public void onClick(View v) {
                temp_min = editText_min.getText().toString();
                temp_sec = editText_sec.getText().toString();

                if(temp_sec.equals("")) temp_sec = "0";
                if(temp_min.equals("")) temp_min = "0";

                int add_min = Integer.valueOf(temp_min);
                int add_sec = Integer.valueOf(temp_sec) + 30;

                if(add_sec >= 60) {
                    add_min = add_min + (add_sec/60);
                    add_sec = add_sec % 60;
                }
                editText_min.setText(Integer.toString(add_min));
                editText_sec.setText(Integer.toString(add_sec));
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_time.setText("00:00");
                progressBar_main.setProgress(0);
                min = 0;
                sec = 0;
                editText_sec.setText("");
                editText_min.setText("");
                if(timer != null) {
                    timer.cancel(false);
                }
            }
        });

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  입력받은 분을 변수에 저장
                //  분은 입력을 안하고 초만 입력한 경우
                if(editText_min.getText().toString().equals("")) {
                    min = 0;
                } else {    //  분을 입력한 경우
                    min = Integer.parseInt(editText_min.getText().toString());
                    //  숫자 > 문자열 로 바꾸는 방법은?
                    //  1. +연산자를 활용한다 >> 1 + "" = "1"
                    //  2. toString()함수를 활용한다 >> Integer.toString(숫자)

                }

                //  입력받은 초를 변수에 저장
                //  초는 입력을 안하고 분만 입력한 경우
                if(editText_sec.getText().toString().equals("")) {
                    sec = 0;
                } else {    //  초를 입력한 경우
                    sec = Integer.parseInt(editText_sec.getText().toString());

                }

                //  분과 초를 다 입력하지 않은 경우
                if(sec == 0 && min == 0) {
                    return;
                }

                //  입력한 시간에 따라 프로그래스 바를 설정하자
                //  입력한 분과 초를 합산하여 초 단위로 변환한다
                int convert_sec = (min * 60) + sec;
                //  프로그래스 바에 변환한 초를 설정한다
                progressBar_main.setMax(convert_sec);
                progressBar_main.setProgress(0);

                //  1초씩 세면서 프로그래스바와 텍스트 뷰를 수정한다
                timer = new CookTimerAsyncTask();
                timer.execute();
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer != null) {
                    timer.cancel(false);
                }
            }
        });
    }

    // AsyncTask<A, B, C>
    // A : doInBackground()함수의 매개변수
    // B : onProgressUpdate()함수의 매개변수
    // C : onPostExcute()함수의 매개변수, doInBackground()함수의 리턴값
    // 예시
    /*
    public class BasicForm extends AsyncTask<Void, Integer, String> {
        protected void onPreExecute() {}
        protected String doInBackground(Void... arg) {
            return "hi";
        }
        protected void onProgressUpdate(Integer i) { }
        protected void onPostExecute(String result) {}
    }
     */

    public class CookTimerAsyncTask extends AsyncTask<Void, Void, Void> {
        //  AsyncTask를 실행하기 전
        @Override
        protected void onPreExecute() {
            textView_time.setText(printMin(min) + " : " +printSec(sec));
        }
        //  AsyncTask를 실행하는 중
       @Override
        protected Void doInBackground(Void... voids) {
            //  isCancelled()함수로 사용자가 취소했으면, true
           //   사용자가 취소하지 않으면, false
           while (isCancelled() == false) {

               try {
                   //   Logcat 창에 메시지를 출력하는 함수 Log.d()
                   //   매개변수 1 : 메시지의 이름
                   //   매개변수 2 : 출력할 메시지
                   Log.d("time_test", String.valueOf(sec));
                   //   1초  = 1000밀리로 동안 대기
                   Thread.sleep(1000);
                   //   1초 후 초를 1 뺀다 == 1초가 지났다
                   sec = sec - 1;

                   //   1을 뺀 결과가 0보다 작은 경우 분을 1빼고
                   //   초는 59로 저장한다
                   //   시계의 분침, 초침의 동작을 표현
                   if(sec<0) {
                       min = min - 1;
                       sec = 59;
                   }

                   //   분과 초를 1씩 빼가면서 분과 초의 값이 0인 경우는
                   //   타이머가 시간을 다 세었음을 의미 while문을 탈출한다
                   if(sec == 0 && min == 0) {
                       break;
                   }

                   //   onProgressUpdate()함수를 호출해주는 publishProgress()함수
                   publishProgress();
               } catch (Exception e) {
                   Log.d("CookingTimer", "doInBackground()");
               }
           }
           return null;
       }

        @Override
        protected void onProgressUpdate(Void... values) {
            //  프로그래스바를 1증가 시킨다
            progressBar_main.incrementProgressBy(1);
            Log.d("time_test_print",String.valueOf(sec));
            //  텍스트 뷰에 분과 초를 다시 출력
            textView_time.setText(printMin(min) + " : " +
                                printSec(sec));
        }
        //  모든 작업을 완료했을 때 수행하는 onPostExecute()함수
        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar_main.incrementProgressBy(1);
            textView_time.setText("00:00");
            Toast.makeText(getApplicationContext(),
                    "타이머가 완료되었습니다",
                    Toast.LENGTH_LONG).show();
        }
        //  작업도중에 사용자가 취소명령어 cancel()함수를 수행했을 떄
        @Override
        protected void onCancelled() {
            textView_time.setText("00:00");
            progressBar_main.setProgress(0);
            Toast.makeText(getApplicationContext(),
                    "타이머가 취소 되었습니다",
                    Toast.LENGTH_LONG).show();

        }
    }
}



























