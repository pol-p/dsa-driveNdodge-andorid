package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.ClanService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClanDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDesc;
    private ImageView ivProfile;
    private RecyclerView recyclerMembers;
    private ClanService clanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_detail);

        tvTitle = findViewById(R.id.tvClanTitle);
        tvDesc = findViewById(R.id.tvClanDesc);
        ivProfile = findViewById(R.id.ivClanProfile);
        recyclerMembers = findViewById(R.id.recyclerMembers);

        recyclerMembers.setLayoutManager(new LinearLayoutManager(this));
        clanService = RetrofitClient.getClient().create(ClanService.class);
        findViewById(R.id.btnBackClan).setOnClickListener(v -> finish());

        String nombre = getIntent().getStringExtra("clanNombre");
        String desc = getIntent().getStringExtra("clanDescripcion");
        String imagen = getIntent().getStringExtra("clanImagen");

        tvTitle.setText(nombre != null ? nombre : "");
        tvDesc.setText(desc != null ? desc : "");

        if (imagen == null || imagen.isEmpty()) {
            imagen = "img/clan/clan_default.png";
        }

        Picasso.get()
                .load(RetrofitClient.getBaseUrl() + imagen)
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(ivProfile);

        if (nombre != null) {
            loadMembers(nombre);
        }
    }

    private void loadMembers(String clanName) {
        clanService.getMembers(clanName).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerMembers.setAdapter(
                            new ClanMembersAdapter(response.body())
                    );
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ClanDetailActivity.this,
                        "Error al cargar miembros", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
