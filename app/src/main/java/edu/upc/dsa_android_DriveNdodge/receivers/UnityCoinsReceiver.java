package edu.upc.dsa_android_DriveNdodge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
        int score = intent.getIntExtra("score", 0);

        Log.d("UnityCoinsReceiver", "Recibido -> User: " + username + " | Score: " + score + " | Coins: " + totalCoins);

        if (username == null) return;

        if (totalCoins >= 0) {
            context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    .edit()
                    .putInt("coins_total_from_unity", totalCoins)
                    .apply();
        }

        if (score > 0 || totalCoins >= 0) {
            enviarPartidaAlBackend(context, username, score, totalCoins);
        }
    }

    private void enviarPartidaAlBackend(Context context, String username, int score, int coins) {
        GameService service = RetrofitClient.getClient().create(GameService.class);

        Partida partida = new Partida(username, score, coins);

        service.saveGame(partida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("UnityCoinsReceiver", "¡Puntos (" + score + ") guardados en la nube!");
                } else {
                    Log.e("UnityCoinsReceiver", "Error servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UnityCoinsReceiver", "Fallo de red al guardar partida");
            }
        });
    }
}