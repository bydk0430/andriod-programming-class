package com.example.student.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.OutputStream;

public class MessageActivity extends AppCompatActivity {
    TextView tv_log;
    Button btn_send;
    EditText et_msg;
    BluetoothSocket connSocket;
    boolean bRead = true;
    Handler writeHandler;

    MsgThread msgThread;
    WriteThread writeThread;
    ReadThread readThread;
    StringBuffer sb;

    final int SEND_MESSAGE = 100;
    final int RECEIVED_MESSAGE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tv_log = (TextView)findViewById(R.id.tv_log);

        btn_send = (Button)findViewById(R.id.btn_send);

        et_msg = (EditText)findViewById(R.id.et_msg);

        //  서버와 클라이언트가 연결된 bluetoothSocket 객체
        connSocket = MainActivity.connSocket;

        //  전달할 메시지들을 저장할 Stringbuffer 객체
        sb = new StringBuffer();

        //  메시지 송수신을 위한 처리를 담당하는 스레드 생성 및 기동
        msgThread = new MsgThread();
        msgThread.start();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send_msg = et_msg.getText().toString() + "\n";
                if(writeHandler != null) {
                    Message msg = new Message();
                    msg.obj = send_msg;
                    writeHandler.sendMessage(msg);
                }
            }
        });
    }

    class MsgThread extends Thread {
        @Override
        public void run() {
            try {
                //  기존에 readThread가 있다면 중지
                if(readThread != null) {
                    readThread.interrupt();
                }
                //  데이터를 수신하기 위한 readThread 생성
                readThread = new ReadThread(connSocket);
                readThread.start();

                //  기존에 writeThread가 있다면 중지
                if(writeHandler != null) {
                    //  writeThread 내부에서 looper를 활용하고 있으므로
                    //  looper를 종료해 주어야 한다
                    writeHandler.getLooper().quit();
               }

               //   데이터를 송신하기 위한 writeThread 생성
                writeThread = new WriteThread(connSocket);
                writeThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //  송수신된 메시지를 화면에 TextView에 출력하기 위한 Handler
    Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SEND_MESSAGE:
                    sb.append("me > "+(String)msg.obj);
                    tv_log.setText(sb);
                    break;
                case RECEIVED_MESSAGE:
                    sb.append("you > "+(String)msg.obj);
                    tv_log.setText(sb);
                    break;
            }
        }
    };

    //  작성한 메시지를 전송하는 스레드, 기기 외부와 통신해야하므로 Thread로 구성
    class WriteThread extends Thread {
        BluetoothSocket socket;
        OutputStream os = null;

        public WriteThread(BluetoothSocket socket) {
            //  통신을 위한 bluetoothSocket 객체를 받는다
            this.socket = socket;
            try {
                //  bluetoothsocket객체에서 outputstream을 생성한다
                os = socket.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Looper.prepare();
            // 메시지를 받으면, 처리하는 핸들러
            writeHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        //  주어진 데이터를 outputStream에 전달하여 상대측에 송신한다
                        os.write(((String)msg.obj).getBytes());
                        os.flush();

                        //  전송한 데이터를 messageActivity안의 textview에 출력하기 위해 메시지를 전달한다
                        Message msg_to_acti = new Message();
                        msg_to_acti.what = 200;
                        msg_to_acti.obj = msg.obj;
                        msgHandler.sendMessage(msg_to_acti);
                   } catch (Exception e) {
                        e.printStackTrace();
                        writeHandler.getLooper().quit();
                    }
               }
            };

            Looper.loop();
        }
    }

    //  상대로 부터 메시지를 전달하기 위해 동작하는 스레드드
    class ReadThread extends Thread {
        BluetoothSocket socket;
        BufferedInputStream bis = null;

        public ReadThread(BluetoothSocket socket) {
            this.socket = socket;
            try {
                // bluetoothSocket에서 bufferedInputStream을 생성
                bis = new BufferedInputStream(
                        socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted() && bRead) {
                try {
                    // 데이터를 임시로 저장할 버퍼를 만든다.
                    byte[] buf = new byte[1024];
                    // 버퍼에 데이터를 읽어온다.
                    int bytes = bis.read(buf);
                    // 읽어온 문자열 형태로 저장한다.
                    String read_str = new String(buf, 0, bytes);

                    // 읽어온 MessageActivity 안의 listview에 적용하기 위해 헨들러에 메시지를 전달한다
                    Message msg = new Message();
                    msg.what = 100;
                    msg.obj = read_str;
                    msgHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    bRead = false;
                }
            }
        }
    }
}


























