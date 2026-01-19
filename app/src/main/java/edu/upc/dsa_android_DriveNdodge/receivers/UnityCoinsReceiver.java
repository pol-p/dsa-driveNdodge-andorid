package edu.upc.dsa_android_DriveNdodge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler; // <--- IMPORTANTE
import android.os.Looper;  // <--- IMPORTANTE
import android.util.Log;

import edu.upc.dsa_android_DriveNdodge.api.GameService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Partida;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnityCoinsReceiver extends BroadcastReceiver {

    public static final String ACTION = "edu.upc.dsa_android_DriveNdodge.ACTION_UNITY_COINS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ACTION.equals(intent.getAction())) return;

        String username = intent.getStringExtra("username");
        int totalCoins = intent.getIntExtra("coins_total", -1);

        Log.d("UnityCoinsReceiver", "Recibido -> User: " + username + " | Coins: " + totalCoins);

        if (username == null || totalCoins < 0) return;

        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("coins_total_from_unity", totalCoins)
                .apply();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            enviarMonedasAlBackend(username, totalCoins);
        }, 1000);
    }

    private void enviarMonedasAlBackend(String username, int coins) {
        GameService service = RetrofitClient.getClient().create(GameService.class);

        Partida partida = new Partida(username, 0, coins);

        service.updateCoins(partida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("UnityCoinsReceiver", "💰 Monedas (" + coins + ") sincronizadas OK.");
                } else {
                    Log.e("UnityCoinsReceiver", "Error servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UnityCoinsReceiver", "Fallo de red al guardar monedas");
            }
        });
    }
}