package edu.upc.dsa_android_DriveNdodge.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.ui.auth.LoginActivity;
import edu.upc.dsa_android_DriveNdodge.ui.auth.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private Button loginBttn;
    private Button registerBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBttn = findViewById(R.id.loginBttn);
        registerBttn = findViewById(R.id.registerBttn);

        loginBttn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerBttn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
