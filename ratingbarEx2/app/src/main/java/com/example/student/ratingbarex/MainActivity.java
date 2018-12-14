package com.example.student.ratingbarex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    RatingBar ratingBar1, ratingBar2;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingBar1 = (RatingBar)findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar)findViewById(R.id.ratingBar2);
        tv_result = (TextView)findViewById(R.id.tv_result);

        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                switch (ratingBar.getId()) {
                    case R.id.ratingBar1:
                        Toast.makeText(MainActivity.this, "첫번째 점수는 : " + v,
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        tv_result.setText("두번째의 점수는 : " + ratingBar2.getRating());
    }
}
