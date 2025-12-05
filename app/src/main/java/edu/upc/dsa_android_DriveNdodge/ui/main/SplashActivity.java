package edu.upc.dsa_android_DriveNdodge.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import edu.upc.dsa_android_DriveNdodge.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkSessionSharedPref();
            }
        }, 2000);
    }

    private void checkSessionSharedPref(){
        SharedPreferences pref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String username = pref.getString("username", null);

        Intent intent;
        if(username != null && !username.isEmpty()){
            Log.d("SplashActivity", "Usuario encontrado, abriendo PortalPageActivity");
            intent = new Intent(SplashActivity.this, PortalPageActivity.class);
        } else {
            Log.d("SplashActivity", "No hay usuario, abriendo MainActivity");
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
