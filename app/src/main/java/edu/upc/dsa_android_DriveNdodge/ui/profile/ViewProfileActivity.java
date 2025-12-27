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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView tvUsernameTitle, tvFullName, tvEmail, tvBirthDate, tvCoins, tvHighScore;
    private ProgressBar progressBar;
    private String username;
    private PerfilService perfilService;
    private ImageButton btnEditProfile;
    private UsrProfile currentProfile;
    private static final int EDIT_PROFILE_REQUEST = 101;
    private static final String BASE_URL_IMG = RetrofitClient.getBaseUrl() + "img/avatar/";
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


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PortalPageActivity.class);
            startActivity(intent);
            finish();
        });

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = prefs.getString("username", null);

        perfilService = RetrofitClient.getClient().create(PerfilService.class);

        if (username != null) {
            loadUserProfile();
        } else {
            Toast.makeText(this, "Error: No hay sesión iniciada", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ViewProfileActivity.this, "Error al cargar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsrProfile> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
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
        Picasso.get().load(BASE_URL_IMG + nombreAvatar).placeholder(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round).fit().centerCrop().into(ivProfilePic);

        String finalNombreAvatar = nombreAvatar;

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
                    Toast.makeText(this, "Perfil actualizado, recargando...", Toast.LENGTH_SHORT).show();
                    loadUserProfile();
                }
            }
        }

}