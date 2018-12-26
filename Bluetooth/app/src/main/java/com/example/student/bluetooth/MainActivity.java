package com.example.student.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    Button btn_scan, btn_discover;
    ArrayList<BluetoothDevice> device_list;
    boolean bPerm;
    ListView lv_device;
    DeviceAdapter deviceAdapter;
    boolean bConn = false;
    boolean bSelect = false;
    AlertDialog selectDialog;
    BluetoothServerSocket serverSocket;
    static BluetoothSocket connSocket;
    BluetoothDevice targetDevice;
    final UUID MY_UUID = UUID.fromString("00001111-1010-1010-1010-12345678ABCD");
    ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  권한 관련 설정
        setPermission(new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        });

        //  뷰 객체 생성
        btn_discover = (Button) findViewById(R.id.btn_discover);
        btn_scan = (Button) findViewById(R.id.btn_scan);

        lv_device = (ListView)findViewById(R.id.lv_device);

        //  버튼 리스너 등록
        btn_discover.setOnClickListener(new BtnListener());
        btn_scan.setOnClickListener(new BtnListener());

        //  검색된 블루투스 기기 정보를 저장하기 위한 Arraylist
        device_list = new ArrayList<BluetoothDevice>();

        //  검색된 블루투스 기기 정보를 listview에 표시하기 위한 Adapter 생성 및 등록
        deviceAdapter = new DeviceAdapter(
                MainActivity.this, R.layout.item_device,
                device_list);
        lv_device.setAdapter(deviceAdapter);

        //  리스트뷰 리스너 등록
        lv_device.setOnItemClickListener(new ItemListener());

        //  블루투스 지원 여부 검사
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null) {
            //  블루투스 활성화
            bluetoothAdapter.enable();

            Toast.makeText(MainActivity.this,   "서버 동작",
                    Toast.LENGTH_SHORT).show();

            //  앱이 시작되면 서버 동작을 하기 위한 스레드를 구동한다
            serverThread = new ServerThread();
            serverThread.start();
        } else {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않음",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    class ItemListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
            //  사용자가 선택한 블루투스 기기의 정보를 Arraylist에서 가져온다
            targetDevice = device_list.get(i);

            Toast.makeText(MainActivity.this,"클라이언트 동작",
                    Toast.LENGTH_SHORT).show();

            //  클라이언트 역할로 접속하기 위한 Thread를 기동한다
            ClientThread clientThread = new ClientThread();
            clientThread.start();

            //  기존의 서버 역할로 접속하기 위한 스레드는 종료
            serverThread.interrupt();
        }
    }

    //  서버 역할을 하기 위한 스레드
    class  ServerThread extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                //  연결 여부 및 블루투스 활성화 검사
                if(!bConn && bluetoothAdapter.isEnabled()) {
                    try {
                        //  서버 동작을 위한 BluetoothServerSocket 객체를 생성
                        serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("MyBluetooth", MY_UUID);

                        //  연결을 기다리다가 클라이언트가 연결되면 BluetoothSocket을 생성
                        //  BluetoothSocket 객체로 통신을 할 수 있다
                        connSocket = serverSocket.accept();

                        if(connSocket != null) {
                            //  메시지를 주고 받기 위해 MessageActivity로 이동
                            Intent intent = new Intent(MainActivity.this,
                                    MessageActivity.class);
                            startActivity(intent);
                            bConn = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //  클라이언트 역할을 하기 위한 스레드
    class ClientThread extends Thread {
        @Override
        public void run() {
            try {
                //  기기 검색을 중지함
                bluetoothAdapter.cancelDiscovery();

                //  선택한 기기 정보를 활용하여 상호간에 통신을 위한 BluetoothSocket 객체를 얻는다
                connSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);

                //  서버와 연결
                connSocket.connect();

                bConn = true;

                //  데이터를 주고 받을 수 있는 MessageActivity로 이동
                Intent intent = new Intent(MainActivity.this,
                        MessageActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_discover:     //  기기 검색 허용
                    Toast.makeText(getApplicationContext(),
                            "다른기기가 스마트폰을 검색하여 인지할 수 있음",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000);
                    startActivity(intent);
                    break;
                case R.id.btn_scan:     //  페어링 기기 검색
                    Toast.makeText(getApplicationContext(),"페어링된 기기를 검색",
                            Toast.LENGTH_SHORT).show();

                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    device_list.clear();

                    if(devices.size()>0) {
                        Iterator<BluetoothDevice> iter = devices.iterator();
                        while (iter.hasNext()) {
                            BluetoothDevice d = iter.next();
                            //  디바이스의 목록을 저장
                            device_list.add(d);
                            Log.d("BLUETEST", "name : " + d.getName() +
                            " addr : " + d.getAddress());
                        }
                        deviceAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "검색된 기기가 없음",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void setPermission(String[] perm) {
        boolean bPerm = false;

        for (int i = 0; i < perm.length; i++) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), perm[i])
                    != PackageManager.PERMISSION_GRANTED) {
                bPerm = false;
            }
        }

        if(!bPerm) {
            ActivityCompat.requestPermissions(
                    this, perm, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean bPerm = true;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            for(int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    bPerm = false;
                }
            }
        }
        this.bPerm = bPerm;
    }
}

