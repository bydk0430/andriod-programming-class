package com.example.student.realm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    EditText et_title,et_number;
    Button btn_add,btn_select,btn_update,btn_del,btn_delAll;
    TextView tv_select;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_title = (EditText)findViewById(R.id.et_title);
        et_number = (EditText)findViewById(R.id.et_number);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_select = (Button) findViewById(R.id.btn_select);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_delAll = (Button) findViewById(R.id.btn_delAll);

        tv_select = (TextView) findViewById(R.id.tv_select);
        Realm.init(MainActivity.this);

        MyBtnListener btnListener = new MyBtnListener();

        btn_add.setOnClickListener(btnListener);
        btn_select.setOnClickListener(btnListener);
        btn_update.setOnClickListener(btnListener);
        btn_del.setOnClickListener(btnListener);
        btn_delAll.setOnClickListener(btnListener);

    }

    class MyBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            final String title = et_title.getText().toString();
            final String number = et_number.getText().toString();

            switch (v.getId()) {
                case R.id.btn_add:
                    if (!title.equals("") && !number.equals("")) {
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MovieVO movieVO = realm.createObject(MovieVO.class);
                                movieVO.setTitle(title);
                                movieVO.setNumber(Integer.parseInt(number));
                            }
                        });
                    }
                    break;
                case R.id.btn_select:
                    RealmResults<MovieVO> results = mRealm.where(MovieVO.class).findAll();
                    String str = "";

                    if(results.size() > 0) {
                        for (int i = 0; i < results.size(); i++) {
                            str += ("number : " + results.get(i).getNumber() + " title : "
                                    + results.get(i).getTitle() + "\n");
                        }
                    } else {
                        str += "no data";
                    }
                    tv_select.setText(str);
                    break;
                case R.id.btn_update:
                    if(!number.equals("")) {
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MovieVO target = mRealm.where(MovieVO.class).equalTo(
                                        "number", Integer.valueOf(number)).findFirst();
                                target.setTitle(title);
                            }
                        });
                    }
                    break;
                case R.id.btn_del:
                    if(!number.equals("")) {
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MovieVO target = mRealm.where(MovieVO.class).equalTo(
                                        "number", Integer.valueOf(number)).findFirst();
                                target.deleteFromRealm();
                            }
                        });
                    }
                    break;
                case R.id.btn_delAll:
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            mRealm.delete(MovieVO.class);
                        }
                    });
                    break;
            }
        }
    }


}
