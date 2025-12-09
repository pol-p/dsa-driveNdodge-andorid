package edu.upc.dsa_android_DriveNdodge.ui.inventario;

import android.content.Context;
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

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.ItemInventario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmptyView;
    private Button btnVolver;
    private ShopService shopService;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        recyclerView = findViewById(R.id.recyclerViewInventario);
        progressBar = findViewById(R.id.progressBarInventario);
        tvEmptyView = findViewById(R.id.tvEmptyView);
        btnVolver = findViewById(R.id.btnVolver);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        username = prefs.getString("username", null);
        shopService = RetrofitClient.getClient().create(ShopService.class);
        btnVolver.setOnClickListener(v -> finish());

        if (username != null) {
            loadInventario();
        } else {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadInventario() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyView.setVisibility(View.GONE);

        shopService.getInventario(username).enqueue(new Callback<List<ItemInventario>>() {
            @Override
            public void onResponse(Call<List<ItemInventario>> call, Response<List<ItemInventario>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    List<ItemInventario> items = response.body();

                    if (items != null && !items.isEmpty()) {
                        InventarioAdapter adapter = new InventarioAdapter(items);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(InventarioActivity.this, "Error al cargar inventario: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ItemInventario>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("InventarioActivity", "Error network", t);
                Toast.makeText(InventarioActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}