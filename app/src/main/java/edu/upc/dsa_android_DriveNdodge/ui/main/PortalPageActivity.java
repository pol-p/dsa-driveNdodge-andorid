package edu.upc.dsa_android_DriveNdodge.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.ui.chat.ChatActivity;
import edu.upc.dsa_android_DriveNdodge.ui.profile.ViewProfileActivity;
import edu.upc.dsa_android_DriveNdodge.ui.ranking.ViewRankingActivity;
import edu.upc.dsa_android_DriveNdodge.ui.shop.ShopActivity;
import edu.upc.dsa_android_DriveNdodge.ui.inventario.InventarioActivity;
import edu.upc.dsa_android_DriveNdodge.models.*;


public class PortalPageActivity extends AppCompatActivity {

    private Button shopBttn, perfilBttn, rankBttn, inventoryBttn, playBttn, clansBttn;


    private String money;

    // 👉 Package del juego Unity (CompanyName + ProductName)
    private static final String UNITY_PACKAGE = "com.DefaultCompany.CarGame2Dd";

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portalpage);

        // -------- Recuperar usuario logueado --------
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = prefs.getString("username", null);

        // -------- Referencias UI --------
        playBttn = findViewById(R.id.playBttn);
        shopBttn = findViewById(R.id.shopBttn);
        perfilBttn = findViewById(R.id.perfilBttn);
        rankBttn = findViewById(R.id.rankBttn);
        inventoryBttn = findViewById(R.id.inventoryBttn);
        clansBttn = findViewById(R.id.clansBttn);
        View btnChat = findViewById(R.id.btn_chat_custom);

        // -------- BOTÓN JUGAR → abrir APK Unity --------
        playBttn.setOnClickListener(v -> {

            if (username == null) {
                Toast.makeText(this, "Error: vuelve a iniciar sesión", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                // Android resuelve automáticamente la Activity principal del juego
                Intent launchIntent =
                        getPackageManager().getLaunchIntentForPackage(UNITY_PACKAGE);

                if (launchIntent == null) {
                    Toast.makeText(
                            this,
                            "No se encontró la app del juego Unity instalada",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }

                // ---------- LOG: nos dice qué Activity real usa Unity ----------
                Log.d(
                        "UNITY_LAUNCH",
                        "Resolved Unity activity = " +
                                launchIntent.getComponent().flattenToShortString()
                );

                // user android to unity
                launchIntent.putExtra("user", username);

                startActivity(launchIntent);

            } catch (Exception e) {

                Toast.makeText(
                        getApplicationContext(),
                        "Error al abrir el juego Unity",
                        Toast.LENGTH_SHORT
                ).show();

                Log.e("UnityLaunchError", "Error launching Unity", e);
            }
        });

        // -------- BOTÓN TIENDA --------
        shopBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ShopActivity.class);
            startActivity(intent);
        });

        // -------- BOTÓN PERFIL --------
        perfilBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ViewProfileActivity.class);
            startActivity(intent);
        });

        // -------- BOTÓN RANKING --------
        rankBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ViewRankingActivity.class);
            startActivity(intent);
        });

        // -------- BOTÓN INVENTARIO --------
        inventoryBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, InventarioActivity.class);
            startActivity(intent);
        });

        // -------- BOTÓN LOGOUT --------
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        clansBttn.setOnClickListener(v -> {
                Intent intent = new Intent(PortalPageActivity.this, edu.upc.dsa_android_DriveNdodge.ui.clan.ClansActivity.class);
                startActivity(intent);
        });

        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, ChatActivity.class);
            startActivity(intent);
        });

    }

    private void logout() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}