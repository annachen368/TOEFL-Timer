package org.annachen.toefltimer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    boolean timer_stop = true;
    CountDownTimer countDownTimer;
    EditText prep;
    EditText resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prep = (EditText) findViewById(R.id.prep_time);
        resp = (EditText) findViewById(R.id.respon_time);

        prep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        resp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void start_btn(View view) {
        if(prep.getText().toString().length() < 1 || resp.getText().toString().length() < 1) {
            Toast.makeText(getApplicationContext(),"Please enter seconds",Toast.LENGTH_LONG).show();
            return;
        }

        if(timer_stop) {
            timer_stop = false;
            Button button = (Button)findViewById(R.id.button);
            button.setText("CLEAR");
            final TextView timer = (TextView) findViewById(R.id.timer);

            final long prep_t = Long.parseLong(prep.getText().toString()) * 1000;
            final long resp_t = Long.parseLong(resp.getText().toString()) * 1000;

            countDownTimer = new CountDownTimer(prep_t+1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("timer", "start"+millisUntilFinished / 1000);
                    int total_sec = (int) (millisUntilFinished / 1000)-1;
                    int seconds = total_sec%60;
                    int hours = total_sec / 3600;
                    int minutes = (total_sec % 3600) / 60;
                    timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }

                @Override
                public void onFinish() {

                    TextView textView = (TextView) findViewById(R.id.message);
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.speak_cutted);
                    mediaPlayer.start();
                    textView.setText("RESPONSE TIME");

                    try {
                        this.wait((long) 4000);
                    }catch(InterruptedException e) {

                    }

                    countDownTimer = new CountDownTimer(resp_t+1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.i("timer", "start"+millisUntilFinished / 1000);
                            int total_sec = (int) (millisUntilFinished / 1000)-1;
                            int seconds = total_sec%60;
                            int hours = total_sec / 3600;
                            int minutes = (total_sec % 3600) / 60;
                            timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                        }

                        @Override
                        public void onFinish() {
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ting);
                            mediaPlayer.start();
                            Button button = (Button)findViewById(R.id.button);
                            button.setText("START");
                            timer_stop = true;
                        }
                    }.start();
                }
            }.start();

        }else {
            Button button = (Button)findViewById(R.id.button);
            button.setText("START");
            TextView timer = (TextView) findViewById(R.id.timer);
            timer.setText("00:00:00");
            timer_stop = true;
            countDownTimer.cancel();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
