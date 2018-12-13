package com.example.student.intentarray;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {

    String val_str;
    int val_int;
    boolean val_bool;
    double val_double;
    float val_float;
    int[] arr_int;
    boolean[] arr_bool;
    double[] arr_double;
    float[] arr_float;

    TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        tv_show = (TextView)findViewById(R.id.tv_show);

        Intent intent = getIntent();
        if(intent != null) {
            val_str = intent.getStringExtra("str_value");
            val_int = intent.getIntExtra("int_value", -1);
            val_bool 	= intent.getBooleanExtra("bool_value", false);
            val_double 	= intent.getDoubleExtra("double_value", -1);
            val_float 	= intent.getFloatExtra("float_value", -1);
            arr_int 	= intent.getIntArrayExtra("int_arr");
            arr_bool 	= intent.getBooleanArrayExtra("bool_arr");
            arr_double 	= intent.getDoubleArrayExtra("double_arr");
            arr_float 	= intent.getFloatArrayExtra("float_arr");

            tv_show.setText(
                    "전달된 String 값 : " + val_str + "\n" +
                            "전달된 int 값 : " + val_int + "\n" +
                            "전달된 boolean 값 : " + val_bool + "\n" +
                            "전달된 double 값 : " + val_double + "\n" +
                            "전달된 float 값 : " + val_float + "\n" +
                            "전달된 int 배열 값 : " + arr_int + "\n" +
                            "전달된 boolean 배열 값 : " + arr_bool[0] + "\t" + arr_bool[1] + "\t" + arr_bool[2] + "\n" +
                            "전달된 double 배열 값 : " + arr_double[0] + "\t" + arr_double[1] + "\t" + arr_double[2] + "\n" +
                            "전달된 float 배열 값 : " + arr_float[0] + "\t" + arr_float[1] + "\t" + arr_float[2] + "\n" );


        }
    }
}
