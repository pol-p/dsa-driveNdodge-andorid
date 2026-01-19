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

public class UnityRunCoinsReceiver extends BroadcastReceiver {

    public static final String ACTION = "edu.upc.dsa_android_DriveNdodge.ACTION_UNITY_RUN_COINS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ACTION.equals(intent.getAction())) return;

        String username = intent.getStringExtra("username");
        // Este receiver recibe el SCORE (puntuación)
        int score = intent.getIntExtra("run_coins", 0);

        Log.d("UnityRunCoinsReceiver", "Recibido -> User: " + username + " | Score: " + score);

        if (username == null || score <= 0) return;
        enviarScoreAlBackend(username, score);
    }

    private void enviarScoreAlBackend(String username, int score) {
        GameService service = RetrofitClient.getClient().create(GameService.class);

        Partida partida = new Partida(username, score, 0);

        service.updateScore(partida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("UnityRunCoinsReceiver", "🏆 Intento de récord (" + score + ") enviado.");
                } else {
                    Log.e("UnityRunCoinsReceiver", "Error servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UnityRunCoinsReceiver", "Fallo de red al guardar score");
            }
        });
    }
}