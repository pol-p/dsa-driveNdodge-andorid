package edu.upc.dsa_android_DriveNdodge.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.upc.dsa_android_DriveNdodge.api.GameService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.InventarioRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Log.d("UnityReceiver", "Unity dice -> User:" + username + " | M:" + magnet + " S:" + shield + " D:" + doubler);

        if (username == null || magnet < 0 || shield < 0 || doubler < 0) {
            Log.e("UnityReceiver", "Datos inválidos recibidos de Unity");
            return;
        }

        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .edit()
                .putInt("inv_magnet", magnet)
                .putInt("inv_shield", shield)
                .putInt("inv_doubler", doubler)
                .apply();

        actualizarBackend(username, magnet, shield, doubler);
    }

    private void actualizarBackend(String username, int magnet, int shield, int doubler) {
        GameService service = RetrofitClient.getClient().create(GameService.class);

        InventarioRequest request = new InventarioRequest(username, magnet, shield, doubler);

        service.updateInventario(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("UnityReceiver", "Inventario sincronizado con la nube exitosamente.");
                } else {
                    Log.e("UnityReceiver", "Error al guardar en nube: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UnityReceiver", "Fallo de red al sincronizar inventario: " + t.getMessage());
            }
        });
    }
}