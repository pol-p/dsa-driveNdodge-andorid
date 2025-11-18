package edu.upc.dsa_android_DriveNdodge.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "LoginActivity"; // TAG per filtrar al Logcat

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

        Log.d(TAG, "Iniciando login con username: " + username);

        if(username.isEmpty() || password.isEmpty()) {
            Log.d(TAG, "Campos vacíos: username o password");
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
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Login exitoso para usuario: " + username);

                    String returnedUsername = response.body().getUsername();
                    if (returnedUsername == null || returnedUsername.isEmpty()) returnedUsername = username;

                    // guardamos valor de username para pasarlo al ShopActivity
                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("username", returnedUsername)
                            .apply();

                    Toast.makeText(LoginActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT).show();

                    // redirigir a ShopActivity
                    Intent intent = new Intent(LoginActivity.this, ShopActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errBody = "";
                    try { if (response.errorBody() != null) errBody = response.errorBody().string(); } catch (Exception ignored) {}
                    Log.d(TAG, "Login fallido: usuario o contraseña incorrectos - " + errBody);
                    Toast.makeText(LoginActivity.this, "Error: usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "Error de conexión al hacer login", t);
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
