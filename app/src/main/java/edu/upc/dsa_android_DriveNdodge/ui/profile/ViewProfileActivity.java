package edu.upc.dsa_android_DriveNdodge.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.api.ShopService;
import edu.upc.dsa_android_DriveNdodge.models.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView tvUsernameTitle, tvFullName, tvEmail, tvBirthDate, tvCoins, tvHighScore;
    private ProgressBar progressBar;
    private String username;

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

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        username = prefs.getString("username", null);

        if (username != null) {
            loadUserProfile();
        } else {
            Toast.makeText(this, "Error: No hay sesión iniciada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserProfile() {
        progressBar.setVisibility(View.VISIBLE);

        ShopService service = RetrofitClient.getClient().create(ShopService.class);
        Call<UserProfile> call = service.getProfile(username);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    UserProfile profile = response.body();
                    updateUI(profile);
                } else {
                    Toast.makeText(ViewProfileActivity.this, "Error al cargar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                Log.e("ViewProfileActivity", "Error", t);
            }
        });
    }

    private void updateUI(UserProfile p) {
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
    }
}