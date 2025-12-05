package edu.upc.dsa_android_DriveNdodge.ui.shop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.ui.main.MainActivity;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.Item;
import edu.upc.dsa_android_DriveNdodge.models.MonedasResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView monedasTextView;
    private String username;
    private ShopService shopService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // 1. Obtener usuario
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = prefs.getString("username", null);

        // 2. Inicializar Vistas
        monedasTextView = findViewById(R.id.monedasActuales);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerViewShop);

        // Configur el LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());

        // 3. Inicializar Retrofit
        shopService = RetrofitClient.getClient().create(ShopService.class);

        // 4. Cagrar datos iniciales
        loadCoins();
        loadItems();
    }

    private void loadItems() {
        progressBar.setVisibility(View.VISIBLE); // MOSTRAR RUEDA de loadBar

        shopService.getItems().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                progressBar.setVisibility(View.GONE); // OCULTAR RUEDA

                if (response.isSuccessful() && response.body() != null) {
                    List<Item> listaItems = response.body();

                    // Crear Adapter y definir qué pasa al hacer click
                    ShopAdapter adapter = new ShopAdapter(listaItems, new ShopAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Item item) {
                            // Lógica de compra al tocar un elemento
                            comprarItem(item);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ShopActivity.this, "Error al cargar items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ShopActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                Log.e("ShopActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void comprarItem(Item item) {
        progressBar.setVisibility(View.VISIBLE); // Feedback visual inmediato

        shopService.buyItem(item.getId(), username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ShopActivity.this, "¡Comprado " + item.getNombre() + "!", Toast.LENGTH_SHORT).show();
                    loadCoins(); // Actualizar monedas
                } else {
                    // Código 404 o 500 del backend
                    Toast.makeText(ShopActivity.this, "Error: Saldo insuficiente o fallo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ShopActivity.this, "Error de red al comprar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCoins() {
        shopService.getMonedas(username).enqueue(new Callback<MonedasResponse>() {
            @Override
            public void onResponse(Call<MonedasResponse> call, Response<MonedasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int cantidad = response.body().getMonedas();
                    monedasTextView.setText("Monedas: " + cantidad);
                }
            }
            @Override
            public void onFailure(Call<MonedasResponse> call, Throwable t) {
                Log.e("ShopActivity", "Error cargando monedas");
            }
        });
    }

    private void logout() {
        getSharedPreferences("MyAppPrefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}