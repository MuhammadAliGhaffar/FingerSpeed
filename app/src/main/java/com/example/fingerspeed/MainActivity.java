package com.example.fingerspeed;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtTimer,txtAThousand;
    private Button btnTap;

    private CountDownTimer countDownTimer;

    private long initialCountDownMillis=60000;
    private int timerInterval=1000;
    private int remainingTime=60;

    private int aThousand=1000;

    private final String REMAINING_TIME_KEY = "remaining timimg key";
    private final String A_THOUSAND_KEY = "a thousand key";


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(REMAINING_TIME_KEY,remainingTime);
        outState.putInt(A_THOUSAND_KEY,aThousand);
        countDownTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer=findViewById(R.id.txtTimer);
        txtAThousand=findViewById(R.id.txtAThousand);
        btnTap=findViewById(R.id.btnTap);

        txtAThousand.setText(aThousand+"");

        if(savedInstanceState != null){
            remainingTime=savedInstanceState.getInt(REMAINING_TIME_KEY);
            aThousand=savedInstanceState.getInt(A_THOUSAND_KEY);

            restoreTheGame();

        }



        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aThousand--;//aThousand=aThousand-1;
                txtAThousand.setText(aThousand+"");

                if(remainingTime>0 && aThousand<=0){
                    Toast.makeText(MainActivity.this,"Congratulations",Toast.LENGTH_LONG).show();

                    showAlert("Congratulations","Please reset the Game");

                }

            }
        });

        if(savedInstanceState==null) {
            countDownTimer = new CountDownTimer(initialCountDownMillis, timerInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTime = (int) millisUntilFinished / 1000;
                    txtTimer.setText("Timer :" + remainingTime);

                }

                @Override
                public void onFinish() {
                    Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_LONG).show();

                    showAlert("Not interesting", "Would you like to try again?");


                }
            };

            countDownTimer.start();
        }



    }

    private void restoreTheGame() {

        int restoreRemainingTime=remainingTime;
        int restoreAThousand=aThousand;

        txtTimer.setText("Timer :"+restoreRemainingTime);
        txtAThousand.setText(restoreAThousand+"");

        countDownTimer=new CountDownTimer((long)remainingTime*1000,timerInterval) {
            @Override
            public void onTick(long millisUntilFinish) {
                remainingTime=(int)millisUntilFinish/1000;
                txtTimer.setText("Timer :"+remainingTime);


            }

            @Override
            public void onFinish() {
                showAlert("Finished","Would you like to reset the game?");

            }
        };
        countDownTimer.start();
    }

    private void resetTheGame(){

        if(countDownTimer!=null){
            countDownTimer.cancel();

            countDownTimer=null;
        }
        aThousand=1000;
        txtAThousand.setText(aThousand+"");

        txtTimer.setText("Timer :"+remainingTime);

        countDownTimer =new CountDownTimer(initialCountDownMillis,timerInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime=(int) millisUntilFinished/1000;
                txtTimer.setText("Timer :"+remainingTime);

            }

            @Override
            public void onFinish() {
                showAlert("Finished","Would you like to reset the game?");
            }
        };

        countDownTimer.start();



    }

    private void showAlert(String title,String message){

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetTheGame();
                    }
                })
                .show();
        alertDialog.setCancelable(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.info_item){
            Toast.makeText(MainActivity.this, "This is the current version of your game. Check out Google Play and make sure your playing the latest version of the game."+BuildConfig.VERSION_NAME, Toast.LENGTH_LONG).show();

        }
        return true;
    }
}