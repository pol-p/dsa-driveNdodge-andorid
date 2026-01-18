package edu.upc.dsa_android_DriveNdodge.ui.clan;

import android.os.Bundle;
import android.util.Log;
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
import edu.upc.dsa_android_DriveNdodge.models.Clan;
import edu.upc.dsa_android_DriveNdodge.models.UsrClan;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
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
        findViewById(R.id.btnBackClan).setOnClickListener(v -> finish());

        recyclerMembers.setLayoutManager(new LinearLayoutManager(this));
        clanService = RetrofitClient.getClient().create(ClanService.class);

        clanName = getIntent().getStringExtra("clanNombre");
        username = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("username", null);

        if (clanName != null) {
            tvTitle.setText(clanName);
            ivProfile.setImageResource(R.mipmap.ic_launcher_round); // Imagen temporal


            loadClanInfo(clanName);
            loadMembers(clanName);
        }

        btnJoinLeave.setOnClickListener(v -> {
            if (isMember) {
                leaveClan();
            } else{
                joinClan();
            }
        });
    }

    private void loadClanInfo(String name) {
        // Log para confirmar que entramos aquí
        android.util.Log.e("CLAN_DEBUG", "--> Pidiendo info del clan: " + name);

        clanService.getClanInfo(name).enqueue(new Callback<Clan>() {
            @Override
            public void onResponse(Call<Clan> call, Response<Clan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Clan clan = response.body();

                    tvDesc.setText(clan.getDescripcion());

                    String imgPath = clan.getImagen();

                    if (imgPath == null || imgPath.isEmpty()) {
                        imgPath = "img/clan/clan_default.png";
                    }
                    Picasso.get().load(RetrofitClient.getBaseUrl() + imgPath).placeholder(R.mipmap.ic_launcher_round).error(R.drawable.redrectanglebttn).fit().centerCrop().into(ivProfile);
                } else {
                    android.util.Log.e("CLAN_DEBUG", "--> Error Servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Clan> call, Throwable t) {
                android.util.Log.e("CLAN_DEBUG", "--> Error Conexión: " + t.getMessage());
                ToastUtils.show(ClanDetailActivity.this, "Error de red", Toast.LENGTH_SHORT);
            }
        });
    }

    private void loadMembers(String clanName) {
        clanService.getMembers(clanName).enqueue(new Callback<List<UsrClan>>() {
            @Override
            public void onResponse(Call<List<UsrClan>> call, Response<List<UsrClan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UsrClan> members = response.body();

                    ClanMembersAdapter adapter = new ClanMembersAdapter(members, new ClanMembersAdapter.OnMemberClickListener() {
                        @Override
                        public void onMemberClick(String usernameClicked) {
                            android.content.Intent intent = new android.content.Intent(ClanDetailActivity.this, edu.upc.dsa_android_DriveNdodge.ui.profile.ViewProfileActivity.class);
                            intent.putExtra("visitUser", usernameClicked); // Pasamos la clave "visitUser"
                            startActivity(intent);
                        }
                    });

                    recyclerMembers.setAdapter(
                            adapter
                    );
                    isMember = false;
                    for (UsrClan u : members) {
                        if (u.getUsername().equals(username)) {
                            isMember = true;
                            break;
                        }
                    }
                    updateJoinLeaveButton();
                }
            }

            @Override
            public void onFailure(Call<List<UsrClan>> call, Throwable t) {
                ToastUtils.show(ClanDetailActivity.this,
                        "Error al cargar miembros", Toast.LENGTH_SHORT);
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
                    ToastUtils.show(ClanDetailActivity.this, "Te has unido al clan", Toast.LENGTH_SHORT);
                    loadMembers(clanName);

                } else if (response.code() == 409) {
                    ToastUtils.show(ClanDetailActivity.this, "No puedes unirte a un clan si ya eres miembro de otro", Toast.LENGTH_LONG);

                } else if (response.code() == 400) {
                    ToastUtils.show(ClanDetailActivity.this, "Usuario no válido", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ToastUtils.show(ClanDetailActivity.this, "Error de conexión", Toast.LENGTH_SHORT);
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
                    ToastUtils.show(ClanDetailActivity.this, "Has salido del clan", Toast.LENGTH_SHORT);
                    loadMembers(clanName);
                    setResult(RESULT_OK);
                    finish();

                } else if (response.code() == 400) {
                    ToastUtils.show(ClanDetailActivity.this, "Usuario no válido", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ToastUtils.show(ClanDetailActivity.this, "Error de conexión", Toast.LENGTH_SHORT);
            }
        });
    }

}
