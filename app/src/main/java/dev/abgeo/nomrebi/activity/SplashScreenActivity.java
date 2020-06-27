package dev.abgeo.nomrebi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Calendar;

import dev.abgeo.nomrebi.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView tvCopyright = findViewById(R.id.tvCopyright);
        tvCopyright.setText(String.format(getResources().getString(R.string.app_copyright), Calendar.getInstance().get(Calendar.YEAR)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        }, ((int) ((Math.random() * 2) + 1) * 1000));
    }

}
