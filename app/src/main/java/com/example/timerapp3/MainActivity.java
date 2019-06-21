package com.example.timerapp3;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH) + 1;
    int date = now.get(Calendar.DATE);


    Drawable startbuttonclicked;

    private Button StartBtn, StopBtn, RecordBtn, PauseBtn;
    private TextView TimeText, RecordTextView, day, startTime;
    private Thread timeThread1 = null;
    private Boolean isRunning = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView day =(TextView)findViewById(R.id.day);
        day.setText(" " + year + "." + month + "." + date + " ");


        StartBtn = (Button)findViewById(R.id.startbtn);
        StopBtn = (Button)findViewById(R.id.stopbtn);
        RecordBtn = (Button)findViewById(R.id.recordbtn);
        PauseBtn = (Button)findViewById(R.id.pausebtn);
        TimeText = (TextView)findViewById(R.id.timeText);
        RecordTextView = (TextView)findViewById(R.id.recordView);
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                StopBtn.setVisibility(View.VISIBLE);
                RecordBtn.setVisibility(View.VISIBLE);
                PauseBtn.setVisibility(View.VISIBLE);

                timeThread1 = new Thread(new timeThread1());
                timeThread1.start();

            }
        });

        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                RecordBtn.setVisibility(View.GONE);
                StartBtn.setVisibility(View.VISIBLE);
                PauseBtn.setVisibility(View.GONE);
                RecordTextView.setText("");
                timeThread1.interrupt();
            }
        });

        RecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordTextView.setText(RecordTextView.getText()+TimeText.getText().toString() + "\n");
            }
        });

        PauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;
                if (isRunning) {
  //                  PauseBtn.setText("일시정지");
                    PauseBtn.setBackgroundResource(R.drawable.pausebuttonclicked);

                } else {
//                    PauseBtn.setText("시작");
                    PauseBtn.setBackgroundResource(R.drawable.startbuttonclicked);
                }
            }
        });


    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 360;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);
            TimeText.setText(result);
        }
    };

    public class timeThread1 implements Runnable {
        @Override
        public void run() {
            int i = 0;
            while (true) {
                while (isRunning) {
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TimeText.setText("");
                                TimeText.setText("00:00:00:00");
                            }
                        });
                        return;
                    }
                }
            }
        }
    }
}