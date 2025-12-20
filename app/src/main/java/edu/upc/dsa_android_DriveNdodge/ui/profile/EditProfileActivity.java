package edu.upc.dsa_android_DriveNdodge.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.PerfilService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.UsrProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etFecha;
    private Button btnSave, btnCancel;
    private PerfilService perfilService;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etNombre = findViewById(R.id.etEditNombre);
        etApellido = findViewById(R.id.etEditApellido);
        etEmail = findViewById(R.id.etEditEmail);
        etFecha = findViewById(R.id.etEditFecha);
        btnSave = findViewById(R.id.btnSaveChanges);
        btnCancel = findViewById(R.id.btnCancelEdit);

        perfilService = RetrofitClient.getClient().create(PerfilService.class);

        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("username");

            String n = intent.getStringExtra("nombre");
            String a = intent.getStringExtra("apellido");
            String e = intent.getStringExtra("email");
            String f = intent.getStringExtra("fecha");

            // Usamos "" por si llega null para que no falle --> Si n no es null sera n else sera ""
            etNombre.setText(n != null ? n : "");
            etApellido.setText(a != null ? a : "");
            etEmail.setText(e != null ? e : "");
            etFecha.setText(f != null ? f : "");
        }

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        UsrProfile u = new UsrProfile();
        u.setNombre(etNombre.getText().toString());
        u.setApellido(etApellido.getText().toString());
        u.setEmail(etEmail.getText().toString());
        u.setFechaNacimiento(etFecha.getText().toString());

        Call<UsrProfile> call = perfilService.updateProfile(username, u);
        call.enqueue(new Callback<UsrProfile>() {
            @Override
            public void onResponse(Call<UsrProfile> call, Response<UsrProfile> response) {
                if (response.isSuccessful()) {
                    // EXITO: Avisamos a la pantalla anterior
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Cerramos esta pantalla
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsrProfile> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}