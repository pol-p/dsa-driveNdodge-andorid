package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import edu.upc.dsa_android_DriveNdodge.models.ClanCreationRequest;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
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
    private static final int REQUEST_CLAN_DETAIL = 1001;


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
                        startActivityForResult(intent, REQUEST_CLAN_DETAIL);
                        ToastUtils.show(ClansActivity.this, "Has pulsado: " + clan.getNombre(), Toast.LENGTH_SHORT);
                    });

                    recyclerView.setAdapter(adapter);
                } else {
                    ToastUtils.show(ClansActivity.this, "Error al cargar clanes", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Clan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.show(ClansActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT);
            }
        });
    }

    private void showCreateClanDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_create_clan, null);

        final EditText etName = view.findViewById(R.id.etClanName);
        final EditText etDesc = view.findViewById(R.id.etClanDesc);

        ImageView imgDef = view.findViewById(R.id.imgDefault);
        ImageView img1 = view.findViewById(R.id.imgClan1);
        ImageView img2 = view.findViewById(R.id.imgClan2);
        ImageView img3 = view.findViewById(R.id.imgClan3);
        ImageView[] images = {imgDef, img1, img2, img3};

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

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (!name.isEmpty() && !desc.isEmpty()) {
                createNewClan(name, desc, selectedImageName);
                dialog.dismiss();
            } else {
                ToastUtils.show(ClansActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT);
            }
        });
    }

    private void createNewClan(String name, String desc, String imgName) {
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String currentUsername = prefs.getString("username", null);

        if (currentUsername == null) {
            progressBar.setVisibility(View.GONE);
            ToastUtils.show(this, "Error: Sesión caducada. Haz login de nuevo.", Toast.LENGTH_LONG);
            return;
        }

        ClanCreationRequest request = new ClanCreationRequest(name, desc, imgName, currentUsername);

        clanService.createClan(request).enqueue(new Callback<Clan>() {
            @Override
            public void onResponse(Call<Clan> call, Response<Clan> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    ToastUtils.show(ClansActivity.this, "¡Clan creado! Ya eres miembro.", Toast.LENGTH_SHORT);
                    loadClans();

                } else if (response.code() == 409) {
                    ToastUtils.show(ClansActivity.this, "¡Error: Ya perteneces a un clan! Sal primero.", Toast.LENGTH_LONG);

                } else {
                    ToastUtils.show(ClansActivity.this, "Error: Nombre de clan en uso o inválido", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Clan> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.show(ClansActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CLAN_DETAIL && resultCode == RESULT_OK) {
            loadClans();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(adapter == null) {
            loadClans();
        }
    }
}