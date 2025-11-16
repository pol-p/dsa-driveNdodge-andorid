package edu.upc.dsa_android_DriveNdodge.UserInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.upc.dsa_android_DriveNdodge.MainActivity;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.stream.Collectors;


public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity"; // TAG per filtrar al Logcat
    private ListView itemsListView; // lista que se mostrara visualmente a la pantalla
    private List<Item> items = new ArrayList<>();  // list que se llena con los items que se reciben del servidor
    private String username; // username persona que ha inixiado sesión
    private TextView monedasActuales; // valor se actualiza cuando compras un item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // recuperamos username persona que ha iniciado sesión
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = prefs.getString("username", null);
        Log.d(TAG, "Username actual: " + username);

        itemsListView = findViewById(R.id.itemsListView);
        monedasActuales = findViewById(R.id.monedasActuales);

        loadCoins();
        loadItems();

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadItems() {
        // creamos servicio retrofit
        ShopService shopService = RetrofitClient.getClient().create(ShopService.class);
        // llamar al endpoint getItems y ejecutar peticion HTTP GET
        Log.d(TAG, "Cargando items desde backend...");
        shopService.getItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //  backend responde con una lista de items, la guardamos en "items"
                    items = response.body();
                    Log.d(TAG, "Items recibidos: " + items.size());

                    // ArrayAdapter classe que nos sirve para conectar la lista de datos del backend a una lista que se podra mostrar visualmente
                    // de momento sirve para mostrarlo visualmente en formato lista
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ShopActivity.this,
                            android.R.layout.simple_list_item_1,
                            items.stream()
                                    .map(i -> i.getNombre() + " - " + i.getPrecio() + " monedas")
                                    .collect(Collectors.toList())

                    );
                    // guardamos los items adaptados a la lista que se mostrara en pantalla
                    itemsListView.setAdapter(adapter);

                    // click listener para cuando se pulse un item de la lista y se llama la funcion buiItem
                    itemsListView.setOnItemClickListener((parent, view, position, id) -> {
                        Log.d(TAG, "Item seleccionado: " + items.get(position).getNombre() + " (ID: " + items.get(position).getId() + ")");
                        buyItem(items.get(position).getId());
                    });

                } else {
                    Log.e(TAG, "Error al cargar items: " + response.code());
                    Toast.makeText(ShopActivity.this, "Error al cargar items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al cargar items", t);
                Toast.makeText(ShopActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCoins() {
        // creamos servicio retrofit
        ShopService shopService = RetrofitClient.getClient().create(ShopService.class);
        // llamar al endpoint getMonedas y ejecutar peticion HTTP GET
        Log.d(TAG, "Cargando monedas para usuario: " + username);
        shopService.getMonedas(username).enqueue(new Callback<ShopService.MonedasResponse>() {
            @Override
            public void onResponse(Call<ShopService.MonedasResponse> call, Response<ShopService.MonedasResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    int monedas = response.body().getMonedas();  // cojemos el valot de dentro el objecto JSON
                    Log.d("ShopActivity", "Monedas desde backend: " + monedas);
                    monedasActuales.setText("Monedas: " + monedas);
                } else {
                    Toast.makeText(ShopActivity.this, "Error al cargar monedas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShopService.MonedasResponse> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void buyItem(int itemId) {
        // creamos servicio retrofit
        ShopService shopService = RetrofitClient.getClient().create(ShopService.class);
        // llamar al endpoint buyItem y ejecutar peticion HTTP POST
        Log.d(TAG, "Intentando comprar item " + itemId + " para usuario: " + username);
        shopService.buyItem(itemId, username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "Compra realizada con éxito: item " + itemId);
                    Toast.makeText(ShopActivity.this, "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
                    loadCoins();
                } else {
                    Log.e(TAG, "Error en la compra del item " + itemId + " - Código: " + response.code());
                    Toast.makeText(ShopActivity.this, "Error en la compra", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error de conexión al comprar item " + itemId, t);
                Toast.makeText(ShopActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void logout(){
        Log.d("SplashActivity", "Cerrando sesión...");

        getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().clear().apply();

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

        // Ir a pantalla del MainActivity
        Intent intent = new Intent(ShopActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
