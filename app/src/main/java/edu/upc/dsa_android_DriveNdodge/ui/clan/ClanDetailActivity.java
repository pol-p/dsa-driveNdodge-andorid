package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.os.Bundle;
import android.widget.Button;
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
    private Button btnJoinLeave;
    private String clanName;
    private String username;
    private boolean isMember = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_detail);

        tvTitle = findViewById(R.id.tvClanTitle);
        tvDesc = findViewById(R.id.tvClanDesc);
        ivProfile = findViewById(R.id.ivClanProfile);
        recyclerMembers = findViewById(R.id.recyclerMembers);
        btnJoinLeave = findViewById(R.id.btnJoinLeaveClan);

        btnJoinLeave.setOnClickListener(v -> {
            if (isMember) {
                leaveClan();
            } else {
                joinClan();
            }
        });



        clanName = getIntent().getStringExtra("clanNombre");
        username = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("username", null);


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
                    List<Usuario> members = response.body();
                    recyclerMembers.setAdapter(
                            new ClanMembersAdapter(response.body())
                    );
                    isMember = false;
                    for (Usuario u : members) {
                        if (u.getUsername().equals(username)) {
                            isMember = true;
                            break;
                        }
                    }
                    updateJoinLeaveButton();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ClanDetailActivity.this,
                        "Error al cargar miembros", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateJoinLeaveButton() {
        if (isMember) {
            btnJoinLeave.setText("SALIR DEL CLAN");
            btnJoinLeave.setBackgroundResource(R.drawable.redrectanglebttn);
        } else {
            btnJoinLeave.setText("UNIRSE AL CLAN");
            btnJoinLeave.setBackgroundResource(R.drawable.bluerectanglebttn);
        }
    }

    private void joinClan() {
        Usuario u = new Usuario();
        u.setUsername(username);

        clanService.joinClan(clanName, u).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(
                            ClanDetailActivity.this,
                            "Te has unido al clan",
                            Toast.LENGTH_SHORT
                    ).show();
                    loadMembers(clanName);

                } else if (response.code() == 409) {
                    Toast.makeText(
                            ClanDetailActivity.this,
                            "No puedes unirte a un clan si ya eres miembro de otro",
                            Toast.LENGTH_LONG
                    ).show();

                } else if (response.code() == 400) {
                    Toast.makeText(
                            ClanDetailActivity.this,
                            "Usuario no válido",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(
                        ClanDetailActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void leaveClan() {
        Usuario u = new Usuario();
        u.setUsername(username);

        clanService.leaveClan(u).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(
                            ClanDetailActivity.this,
                            "Has salido del clan",
                            Toast.LENGTH_SHORT
                    ).show();
                    loadMembers(clanName);
                    setResult(RESULT_OK);
                    finish();

                } else if (response.code() == 400) {
                    Toast.makeText(
                            ClanDetailActivity.this,
                            "Usuario no válido",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(
                        ClanDetailActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

}
