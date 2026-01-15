package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.ClanService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Clan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClansActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClanAdapter adapter;
    private ProgressBar progressBar;
    private ClanService clanService;
    private Button btnCreateClan, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clans);

        // 1. Vincular Vistas
        recyclerView = findViewById(R.id.recyclerViewClans);
        progressBar = findViewById(R.id.progressBarClans);
        btnCreateClan = findViewById(R.id.btnCreateClan);
        btnBack = findViewById(R.id.btnBackClans);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        clanService = RetrofitClient.getClient().create(ClanService.class);
        btnBack.setOnClickListener(v -> finish());

        btnCreateClan.setOnClickListener(v -> {
            Toast.makeText(this, "Función Crear Clan: Próximamente", Toast.LENGTH_SHORT).show();
        });

        loadClans();
    }

    private void loadClans() {
        progressBar.setVisibility(View.VISIBLE);

        Call<List<Clan>> call = clanService.getAllClans();
        call.enqueue(new Callback<List<Clan>>() {
            @Override
            public void onResponse(Call<List<Clan>> call, Response<List<Clan>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Clan> clans = response.body();

                    adapter = new ClanAdapter(clans, ClansActivity.this, clan -> {
                        // CLICK EN UN CLAN -> IR A DETALLE
                        // Intent intent = new Intent(ClansActivity.this, ClanDetailActivity.class);
                        // intent.putExtra("clanName", clan.getNombre());
                        // startActivity(intent);
                        Toast.makeText(ClansActivity.this, "Has pulsado: " + clan.getNombre(), Toast.LENGTH_SHORT).show();
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ClansActivity.this, "Error al cargar clanes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Clan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ClansActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter == null) {
            loadClans();
        }
    }
}