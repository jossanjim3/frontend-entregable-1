package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import us.master.entregable1.entity.Trip;

public class SplashActivity extends AppCompatActivity {

    private int SPLASH_DURATION = 1000; // miliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Constantes.trips = Trip.generateTrips();
                Constantes.trips = new ArrayList<>();
                Constantes.rellenaMapIdCiudad();
                // startActivity(new Intent(SplashActivity.this,MainActivity.class));
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        },SPLASH_DURATION);
    }
}
