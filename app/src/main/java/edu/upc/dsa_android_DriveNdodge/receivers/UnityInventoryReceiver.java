package edu.upc.dsa_android_DriveNdodge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UnityInventoryReceiver extends BroadcastReceiver {

    public static final String ACTION = "edu.upc.dsa_android_DriveNdodge.ACTION_UNITY_INVENTORY";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        if (!ACTION.equals(intent.getAction())) return;

        String username = intent.getStringExtra("username");
        int magnet = intent.getIntExtra("magnet", -1);
        int shield = intent.getIntExtra("shield", -1);
        int doubler = intent.getIntExtra("doubler", -1);

        Log.d("UnityInventoryReceiver",
                "Recibido inventario Unity -> user=" + username +
                        " magnet=" + magnet + " shield=" + shield + " doubler=" + doubler);

        if (username == null || magnet < 0 || shield < 0 || doubler < 0) return;

        // Guardar en prefs (persistente)
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("inv_magnet", magnet)
                .putInt("inv_shield", shield)
                .putInt("inv_doubler", doubler)
                .apply();

        // (Opcional) aquí llamaríais al backend para actualizar inventario.
        // Si me pasas el endpoint, lo integro con Retrofit.
    }
}
