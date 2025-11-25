package edu.upc.dsa_android_DriveNdodge.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upc.dsa_android_DriveNdodge.R;
import edu.upc.dsa_android_DriveNdodge.api.AuthService;
import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity"; // TAG per filtrar al Logcat

    private EditText usernameIn, passwordIn, passwordConfirmIn, nombreIn, apellidoIn, gmailIn;
    private EditText diaIn, mesIn, anoIn;
    private Button registerBttn;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        passwordConfirmIn = findViewById(R.id.passwordConfirmIn);
        nombreIn = findViewById(R.id.nombreIn);
        apellidoIn = findViewById(R.id.apellidoIn);
        gmailIn = findViewById(R.id.gmailIn);
        diaIn = findViewById(R.id.diaIn);
        mesIn = findViewById(R.id.mesIn);
        anoIn = findViewById(R.id.anoIn);
        registerBttn = findViewById(R.id.registerBttn);

        progressBar = findViewById(R.id.progressBar);

        registerBttn.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
        String username = usernameIn.getText().toString().toLowerCase();
        String password = passwordIn.getText().toString();
        String passwordConfirm = passwordConfirmIn.getText().toString();
        String nombre = nombreIn.getText().toString();
        String apellido = apellidoIn.getText().toString();
        String gmail = gmailIn.getText().toString().toLowerCase();
        String dia = diaIn.getText().toString();
        String mes = mesIn.getText().toString();
        String ano = anoIn.getText().toString();

        progressBar.setVisibility(View.VISIBLE); // MOSTRAR RUEDA de loadBar

        Log.i(TAG, "Intentando registrar usuario: " + username);

        if(username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() ||
                nombre.isEmpty() || apellido.isEmpty() || gmail.isEmpty() ||
                dia.isEmpty() || mes.isEmpty() || ano.isEmpty()) {
            Log.i(TAG, "Campos vacíos en el formulario de registro");
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(passwordConfirm)) {
            Log.i(TAG, "Contraseñas no coinciden para usuario: " + username);
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        String fechaNacimiento = String.format("%02d-%02d-%s", Integer.parseInt(dia),
                Integer.parseInt(mes), ano);

        Usuario usuario = new Usuario(username, password, nombre, apellido, gmail, fechaNacimiento);

        AuthService authService = RetrofitClient.getClient().create(AuthService.class);
        authService.register(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                progressBar.setVisibility(View.GONE); // OCULTAR RUEDA
                if (response.isSuccessful()) {
                    Log.i(TAG, "Registro exitoso para usuario: " + username);
                    Toast.makeText(RegisterActivity.this, "Registro exitoso, ¡bienvenido!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i(TAG, "Error en registro: parámetros incorrectos o usuario ya existente");
                    Toast.makeText(RegisterActivity.this, "Error: parámetros mal pasados o usuario ya existente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "Error de conexión al registrar usuario: " + username, t);
                Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
