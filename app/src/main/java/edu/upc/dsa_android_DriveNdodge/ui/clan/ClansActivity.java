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
    private String selectedImageName = "clan_default.png";

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
            showCreateClanDialog();
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
                        Intent intent = new Intent(ClansActivity.this, ClanDetailActivity.class);
                        intent.putExtra("clanNombre", clan.getNombre());
                        intent.putExtra("clanDescripcion", clan.getDescripcion());
                        intent.putExtra("clanImagen", clan.getImagen());
                        startActivity(intent);
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

    private void showCreateClanDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_create_clan, null);

        final android.widget.EditText etName = view.findViewById(R.id.etClanName);
        final android.widget.EditText etDesc = view.findViewById(R.id.etClanDesc);

        android.widget.ImageView imgDef = view.findViewById(R.id.imgDefault);
        android.widget.ImageView img1 = view.findViewById(R.id.imgClan1);
        android.widget.ImageView img2 = view.findViewById(R.id.imgClan2);
        android.widget.ImageView img3 = view.findViewById(R.id.imgClan3);
        android.widget.ImageView[] images = {imgDef, img1, img2, img3};

        View.OnClickListener imgListener = v -> {
            for (android.widget.ImageView img : images) img.setBackground(null);
            v.setBackgroundResource(R.drawable.orangerectanglebttn);
            if (v.getTag() != null) selectedImageName = v.getTag().toString();
        };

        imgDef.setOnClickListener(imgListener);
        img1.setOnClickListener(imgListener);
        img2.setOnClickListener(imgListener);
        img3.setOnClickListener(imgListener);
        selectedImageName = "clan_default.png"; // Reset

        builder.setView(view);

        builder.setPositiveButton("CREAR", null);
        builder.setNegativeButton("CANCELAR", (dialog, which) -> dialog.dismiss());

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (!name.isEmpty() && !desc.isEmpty()) {
                createNewClan(name, desc, selectedImageName);
                dialog.dismiss();
            } else {
                Toast.makeText(ClansActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewClan(String name, String desc, String imgName) {
        progressBar.setVisibility(View.VISIBLE);

        Clan newClan = new Clan(name, desc, imgName);

        clanService.createClan(newClan).enqueue(new Callback<Clan>() {
            @Override
            public void onResponse(Call<Clan> call, Response<Clan> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(ClansActivity.this, "¡Clan creado!", Toast.LENGTH_SHORT).show();
                    loadClans(); // Recargamos la lista al instante
                } else {
                    Toast.makeText(ClansActivity.this, "Error: Nombre duplicado o datos mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Clan> call, Throwable t) {
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