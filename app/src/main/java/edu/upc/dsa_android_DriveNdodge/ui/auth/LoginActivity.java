package edu.upc.dsa_android_DriveNdodge.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.ui.main.MainActivity;
import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.AuthService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;

import edu.upc.dsa_android_DriveNdodge.ui.main.PortalPageActivity;
import edu.upc.dsa_android_DriveNdodge.ui.utils.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity"; // TAG per filtrar al Logcat

    private EditText usernameIn, passwordIn;
    private Button loginBttn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        loginBttn = findViewById(R.id.loginBttn);
        progressBar = findViewById(R.id.progressBar);

        loginBttn.setOnClickListener(v -> doLogin());

        Button backBttn = findViewById(R.id.backBttn);
        backBttn.setOnClickListener(v -> {
            Log.i(TAG, "Volviendo al MainActivity");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void doLogin() {

        String username = usernameIn.getText().toString().toLowerCase();
        String password = passwordIn.getText().toString();

        Log.i(TAG, "Iniciando login con username: " + username);

        if(username.isEmpty() || password.isEmpty()) {
            Log.i(TAG, "Campos vacíos: username o password");
            ToastUtils.show(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT);
            return;
        }
        progressBar.setVisibility(View.VISIBLE); // MOSTRAR RUEDA de loadBar

        Usuario usuario = new Usuario(username, password);

        AuthService authService = RetrofitClient.getClient().create(AuthService.class);
        authService.login(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                progressBar.setVisibility(View.GONE); // OCULTAR RUEDA
                if (response.isSuccessful()) {
                    Log.i(TAG, "Login exitoso para usuario: " + username);

                    // guardamos valor de username para pasarlo al ShopActivity
                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("username", username)
                            .apply();

                    ToastUtils.show(LoginActivity.this, "Se ha iniciado sesión correctamente", Toast.LENGTH_SHORT);

                    // redirigir a ShopActivity
                    Intent intent = new Intent(LoginActivity.this, PortalPageActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i(TAG, "Login fallido: usuario o contraseña incorrectos");
                    ToastUtils.show(LoginActivity.this, "Error: usuario o contraseña incorrectos", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error de conexión al hacer login", t);
                ToastUtils.show(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }
}
