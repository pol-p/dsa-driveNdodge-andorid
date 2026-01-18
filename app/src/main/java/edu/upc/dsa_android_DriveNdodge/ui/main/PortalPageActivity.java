package edu.upc.dsa_android_DriveNdodge.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.ItemInventario;
import edu.upc.dsa_android_DriveNdodge.ui.clan.ClanActivity;
import edu.upc.dsa_android_DriveNdodge.ui.evento.EventosActivity;
import edu.upc.dsa_android_DriveNdodge.models.MonedasResponse;
import edu.upc.dsa_android_DriveNdodge.ui.chat.ChatActivity;
import edu.upc.dsa_android_DriveNdodge.ui.profile.ViewProfileActivity;
import edu.upc.dsa_android_DriveNdodge.ui.ranking.ViewRankingActivity;
import edu.upc.dsa_android_DriveNdodge.ui.shop.ShopActivity;
import edu.upc.dsa_android_DriveNdodge.ui.inventario.InventarioActivity;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortalPageActivity extends AppCompatActivity {

    private Button shopBttn, perfilBttn, rankBttn, inventoryBttn, playBttn, clansBttn, eventsBttn;

    private static final String UNITY_PACKAGE = "com.DefaultCompany.CarGame2Dd";

    private String username;

    private ShopService shopService;

    private int coinsValue = 0;

    // ===== INVENTARIO (3 items) =====
    private int magnetCount = 0;
    private int shieldCount = 0;
    private int doublerCount = 0;

    private boolean coinsLoaded = false;
    private boolean invLoaded = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portalpage);

        handleCoinsFromUnity(getIntent());


        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = prefs.getString("username", null);

        playBttn = findViewById(R.id.playBttn);
        shopBttn = findViewById(R.id.shopBttn);
        perfilBttn = findViewById(R.id.perfilBttn);
        rankBttn = findViewById(R.id.rankBttn);
        inventoryBttn = findViewById(R.id.inventoryBttn);
        clansBttn = findViewById(R.id.clansBttn);
        eventsBttn = findViewById(R.id.eventsBttn);
        View btnChat = findViewById(R.id.btn_chat_custom);

        shopService = RetrofitClient.getClient().create(ShopService.class);

        // Cargar datos al entrar
        if (username != null && !username.isEmpty()) {
            loadCoins(username);
            loadInventario(username);
        } else {
            coinsValue = 0;
            coinsLoaded = true;
            invLoaded = true; // no hay user, pero así no se queda bloqueado
        }

        // ===== BOTÓN JUGAR =====
        playBttn.setOnClickListener(v -> {

            if (username == null || username.isEmpty()) {
                Toast.makeText(this, "Error: vuelve a iniciar sesión", Toast.LENGTH_LONG).show();
                return;
            }

            // Evitar lanzar Unity si aún no han llegado datos
            if (!coinsLoaded || !invLoaded) {
                Toast.makeText(this, "Cargando datos del usuario... espera 1 segundo", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Log.d("UNITY_DEBUG", "Abriendo UnityPlayerGameActivity...");
                Log.d("UNITY_DEBUG", "Enviando username=" + username + " coins=" + coinsValue +
                        " inv: magnet=" + magnetCount + " shield=" + shieldCount + " doubler=" + doublerCount);

                Intent intent = new Intent();
                intent.setClassName(
                        UNITY_PACKAGE,
                        "com.unity3d.player.UnityPlayerGameActivity"
                );

                // Android -> Unity
                intent.putExtra("username", username);
                intent.putExtra("coins", coinsValue);

                // Inventario -> JSON pequeño
                InventoryPayload payload = new InventoryPayload(magnetCount, shieldCount, doublerCount);
                String invJson = new Gson().toJson(payload);
                intent.putExtra("inv_json", invJson);

                Log.d("UNITY_DEBUG", invJson);

                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this, "Error al abrir el juego Unity", Toast.LENGTH_LONG).show();
                Log.e("UNITY_DEBUG", "Error abriendo UnityPlayerGameActivity", e);
            }
        });

        shopBttn.setOnClickListener(v -> startActivity(new Intent(this, ShopActivity.class)));
        perfilBttn.setOnClickListener(v -> startActivity(new Intent(this, ViewProfileActivity.class)));
        rankBttn.setOnClickListener(v -> startActivity(new Intent(this, ViewRankingActivity.class)));
        inventoryBttn.setOnClickListener(v -> startActivity(new Intent(this, InventarioActivity.class)));
        eventsBttn.setOnClickListener(v -> {
            Intent intent = new Intent(PortalPageActivity.this, EventosActivity.class);
            startActivity(intent);
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        clansBttn.setOnClickListener(v ->
                startActivity(new Intent(this, edu.upc.dsa_android_DriveNdodge.ui.clan.ClanActivity.class))
        );

        btnChat.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (username != null && !username.isEmpty()) {
            loadCoins(username);
            loadInventario(username);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleCoinsFromUnity(intent);
    }

    private void handleCoinsFromUnity(Intent intent) {
        if (intent == null) return;

        if (intent.hasExtra("dinero")) {
            int totalCoins = intent.getIntExtra("dinero", -1);
            Log.d("UNITY_RETURN", "Recibido dinero desde Unity: " + totalCoins);

            if (totalCoins >= 0) {
                // 1) Guardarlo localmente si queréis
                getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                        .edit()
                        .putInt("coins_total", totalCoins)
                        .apply();

                // 2) Aquí es donde normalmente actualizas backend:
                // - O llamas a un endpoint "setCoins" / "addCoins"
                // Como no me has pasado endpoint de update coins, lo dejo preparado:
                // updateCoinsOnServer(username, totalCoins);

                Toast.makeText(this, "Monedas actualizadas: " + totalCoins, Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void loadCoins(String username) {
        coinsLoaded = false;

        shopService.getMonedas(username).enqueue(new Callback<MonedasResponse>() {
            @Override
            public void onResponse(Call<MonedasResponse> call, Response<MonedasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    coinsValue = response.body().getMonedas();
                } else {
                    coinsValue = 0;
                }
                coinsLoaded = true;
                Log.d("PortalPageActivity", "Coins cargadas: " + coinsValue);
            }

            @Override
            public void onFailure(Call<MonedasResponse> call, Throwable t) {
                coinsValue = 0;
                coinsLoaded = true;
                Log.e("PortalPageActivity", "Fallo monedas", t);
            }
        });
    }

    private void loadInventario(String username) {
        invLoaded = false;

        shopService.getInventario(username).enqueue(new Callback<List<ItemInventario>>() {
            @Override
            public void onResponse(Call<List<ItemInventario>> call, Response<List<ItemInventario>> response) {
                magnetCount = 0;
                shieldCount = 0;
                doublerCount = 0;

                if (response.isSuccessful() && response.body() != null) {
                    List<ItemInventario> items = response.body();

                    // Mapear por nombre (más robusto que ID si cambia)
                    for (ItemInventario it : items) {
                        String name = (it.getNombre() == null) ? "" : it.getNombre().toLowerCase();

                        if (name.contains("magnet") || name.contains("imán") || name.contains("iman")) {
                            magnetCount = it.getCantidad();
                        } else if (name.contains("shield") || name.contains("escudo")) {
                            shieldCount = it.getCantidad();
                        } else if (name.contains("double") || name.contains("duplic") || name.contains("x2")) {
                            doublerCount = it.getCantidad();
                        }
                    }
                }

                invLoaded = true;
                Log.d("PortalPageActivity", "Inventario cargado -> magnet=" + magnetCount +
                        " shield=" + shieldCount + " doubler=" + doublerCount);
            }

            @Override
            public void onFailure(Call<List<ItemInventario>> call, Throwable t) {
                magnetCount = 0;
                shieldCount = 0;
                doublerCount = 0;
                invLoaded = true;
                Log.e("PortalPageActivity", "Fallo inventario", t);
            }
        });
    }

    private void logout() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    // Payload mínimo que mandamos a Unity
    private static class InventoryPayload {
        int magnet;
        int shield;
        int doubler;

        InventoryPayload(int magnet, int shield, int doubler) {
            this.magnet = magnet;
            this.shield = shield;
            this.doubler = doubler;
        }
    }
}
