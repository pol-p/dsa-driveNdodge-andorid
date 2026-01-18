package edu.upc.dsa_android_DriveNdodge.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.PerfilService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.UsrProfile;
import edu.upc.dsa_android_DriveNdodge.ui.main.PortalPageActivity;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView tvUsernameTitle, tvFullName, tvEmail, tvBirthDate, tvCoins, tvHighScore, tvClanName;
    private ProgressBar progressBar;
    private String username;
    private PerfilService perfilService;
    private ImageButton btnEditProfile;
    private UsrProfile currentProfile;
    private ImageView ivClanLogo;
    private static final int EDIT_PROFILE_REQUEST = 101;
    private static final int CLAN_DETAIL_REQUEST = 102;
    private static final String BASE_URL_IMG = RetrofitClient.getBaseUrl() + "img/avatar/";
    private static final String BASE_URL_CLAN = RetrofitClient.getBaseUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        tvUsernameTitle = findViewById(R.id.tvUsernameTitle);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvCoins = findViewById(R.id.tvCoins);
        tvHighScore = findViewById(R.id.tvHighScore);
        progressBar = findViewById(R.id.progressBarProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        tvClanName = findViewById(R.id.tvClanName);
        ivClanLogo = findViewById(R.id.ivClanLogo);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getIntent().getStringExtra("visitUser") != null) {
                finish();
            } else {
                Intent intent = new Intent(this, PortalPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        String sessionUser = prefs.getString("username", null);
        String visitUser = getIntent().getStringExtra("visitUser");

        if (visitUser != null && !visitUser.isEmpty()) {
            username = visitUser; // Perfdil de otro
        } else {
            username = sessionUser; // Modo Mi Perfil
        }

        btnEditProfile.setVisibility(View.GONE);

        if (sessionUser != null && username != null) {
            if (sessionUser.equals(username)) {
                btnEditProfile.setVisibility(View.VISIBLE);
            }
        }
        perfilService = RetrofitClient.getClient().create(PerfilService.class);

        if (username != null) {
            loadUserProfile();
        } else {
            ToastUtils.show(this, "Error: No hay sesión iniciada", Toast.LENGTH_SHORT);
            finish();
        }
    }


    private void loadUserProfile() {
        progressBar.setVisibility(View.VISIBLE);

        perfilService.getProfile(username).enqueue(new Callback<UsrProfile>() {
            @Override
            public void onResponse(Call<UsrProfile> call, Response<UsrProfile> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    UsrProfile profile = response.body();
                    currentProfile = response.body();
                    Log.i("ViewProfileActivity", "Email devuelto: " + profile.getEmail());
                    updateUI(profile);
                } else {
                    ToastUtils.show(ViewProfileActivity.this, "Error al cargar perfil", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<UsrProfile> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.show(ViewProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT);
                Log.e("ViewProfileActivity", "Error", t);
            }
        });
    }

    private void updateUI(UsrProfile p) {
        tvUsernameTitle.setText("@" + p.getUsername());

        String nombreCompleto = p.getNombre() + " " + p.getApellido();
        tvFullName.setText(nombreCompleto);
        tvEmail.setText(p.getEmail());

        if (p.getFechaNacimiento() == null) {
            p.setFechaNacimiento("No disponible");
        }
        String fecha = p.getFechaNacimiento();
        tvBirthDate.setText(fecha);

        tvCoins.setText(String.valueOf(p.getMonedas()));
        tvHighScore.setText(String.valueOf(p.getMejorPuntuacion()));

        ImageView ivProfilePic = findViewById(R.id.ivProfilePic);
        String nombreAvatar = p.getImagenPerfil();

        if (nombreAvatar == null || nombreAvatar.isEmpty()) {
            nombreAvatar = "avatar_default.webp";
        }
        Picasso.get().load(BASE_URL_IMG + nombreAvatar).placeholder(R.drawable.logo).error(R.mipmap.ic_launcher_round).fit().centerCrop().into(ivProfilePic);

        String finalNombreAvatar = nombreAvatar;

        if (p.getClanNombre() != null && !p.getClanNombre().isEmpty() && !p.getClanNombre().equals("Sin Clan")) {
            // Si tiene clan
            tvClanName.setText(p.getClanNombre());
            ivClanLogo.setVisibility(View.VISIBLE);

            String imagenClan = p.getClanImagen();
            if (imagenClan == null || imagenClan.isEmpty()) {
                imagenClan = "clan_default.png";
            }
            Picasso.get().load(BASE_URL_CLAN + imagenClan).placeholder(R.drawable.logo).error(R.mipmap.ic_launcher).fit().centerCrop().into(ivClanLogo);

            View.OnClickListener irAlClan = v -> {
                Intent intent = new Intent(ViewProfileActivity.this, edu.upc.dsa_android_DriveNdodge.ui.clan.ClanDetailActivity.class);
                intent.putExtra("clanNombre", p.getClanNombre());

                startActivityForResult(intent, CLAN_DETAIL_REQUEST);
            };

            tvClanName.setOnClickListener(irAlClan);
            ivClanLogo.setOnClickListener(irAlClan);
        } else {
            // Si no tiene clan
            tvClanName.setText("Sin Clan");
            ivClanLogo.setVisibility(View.GONE);

            tvClanName.setOnClickListener(null);
            ivClanLogo.setOnClickListener(null);
        }

        btnEditProfile.setOnClickListener(v -> {
            if (currentProfile != null) {
                Intent i = new Intent(ViewProfileActivity.this, EditProfileActivity.class);
                i.putExtra("username", currentProfile.getUsername());
                i.putExtra("nombre", currentProfile.getNombre());
                i.putExtra("apellido", currentProfile.getApellido());
                i.putExtra("email", currentProfile.getEmail());
                i.putExtra("fecha", currentProfile.getFechaNacimiento());
                i.putExtra("imagenPerfil", finalNombreAvatar);

                startActivityForResult(i, EDIT_PROFILE_REQUEST);
            }
        });
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == EDIT_PROFILE_REQUEST) {
                if (resultCode == RESULT_OK) {
                    // Si la edición fue bien, recargamos los datos del servidor
                    ToastUtils.show(this, "Perfil actualizado, recargando...", Toast.LENGTH_SHORT);
                    loadUserProfile();
                }
            }
            if (requestCode == CLAN_DETAIL_REQUEST) {
                if (resultCode == RESULT_OK) {
                    loadUserProfile();
                }
            }
        }

}