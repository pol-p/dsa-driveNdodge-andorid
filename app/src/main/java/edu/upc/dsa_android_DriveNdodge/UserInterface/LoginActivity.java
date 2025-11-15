package edu.upc.dsa_android_DriveNdodge.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.AuthService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameIn, passwordIn;
    private Button loginBttn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        loginBttn = findViewById(R.id.loginBttn);

        loginBttn.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String username = usernameIn.getText().toString().toLowerCase();
        String password = passwordIn.getText().toString();

        if(username.isEmpty()||password.isEmpty()) {
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(username, password);

        // creamos servicio retrofit
        AuthService authService = RetrofitClient.getClient().create(AuthService.class);

        // llamar al endpoint login y ejecutar peticion HTTP POST
        authService.login(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    // servidor devuelve codigo 2xx
                    Toast.makeText(LoginActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //servidor devuelve codigo de error 4xx o 500
                    Toast.makeText(LoginActivity.this, "Error: usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
