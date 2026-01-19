package edu.upc.dsa_android_DriveNdodge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import edu.upc.dsa_android_DriveNdodge.api.GameService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Partida;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnityRunCoinsReceiver extends BroadcastReceiver {

    public static final String ACTION = "edu.upc.dsa_android_DriveNdodge.ACTION_UNITY_RUN_COINS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ACTION.equals(intent.getAction())) return;

        String username = intent.getStringExtra("username");
        //int totalCoins = intent.getIntExtra("coins_total", -1);
        int score = intent.getIntExtra("run_coins", 0);

        Log.d("UnityRunCoinsReceiver", "Recibido -> User: " + username + " | runcoins: " + score );

        if (username == null) return;

        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        int totalCoins = prefs.getInt("coins_total_from_unity", 0);


        if (score > 0) {
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