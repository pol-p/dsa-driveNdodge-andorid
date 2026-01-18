package edu.upc.dsa_android_DriveNdodge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UnityCoinsReceiver extends BroadcastReceiver {

    public static final String ACTION = "edu.upc.dsa_android_DriveNdodge.ACTION_UNITY_COINS";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        if (!ACTION.equals(intent.getAction())) return;

        int totalCoins = intent.getIntExtra("coins_total", -1);
        String username = intent.getStringExtra("username");

        Log.d("UnityCoinsReceiver", "Recibido broadcast: username=" + username + " coins_total=" + totalCoins);

        if (totalCoins < 0 || username == null) return;

        // Guardarlo en SharedPreferences del Android app (persistente)
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("coins_total_from_unity", totalCoins)
                .apply();

        // (Opcional) aquí podrías llamar al backend para actualizar monedas
        // Pero eso ya depende de vuestro endpoint. Si lo tenéis, me lo pasas y lo integro.
    }
}
