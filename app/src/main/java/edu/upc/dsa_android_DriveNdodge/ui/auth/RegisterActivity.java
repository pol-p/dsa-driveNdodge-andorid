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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity"; // TAG per filtrar al Logcat

    private EditText usernameIn, passwordIn, passwordConfirmIn, nombreIn, apellidoIn, gmailIn;
    private EditText diaIn, mesIn, anoIn;
    private Button registerBttn, backBttn;

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
        backBttn = findViewById(R.id.backBttn);

        progressBar = findViewById(R.id.progressBar);

        registerBttn.setOnClickListener(v -> doRegister());
        backBttn.setOnClickListener(v -> {
            Log.i(TAG, "Volviendo al MainActivity");
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
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

        Log.i(TAG, "Intentando registrar usuario: " + username);

        // Validación 1: Campos vacíos
        if(username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() ||
                nombre.isEmpty() || apellido.isEmpty() || gmail.isEmpty() ||
                dia.isEmpty() || mes.isEmpty() || ano.isEmpty()) {
            Log.i(TAG, "Campos vacíos en el formulario de registro");
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación 2: Formato de email válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(gmail).matches()) {
            Log.i(TAG, "Formato de email inválido: " + gmail);
            Toast.makeText(this, "Por favor ingresa un email válido (ej: usuario@gmail.com)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación 3: Contraseñas coincidentes
        if(!password.equals(passwordConfirm)) {
            Log.i(TAG, "Contraseñas no coinciden para usuario: " + username);
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación 6: Fecha válida
        try {
            int diaInt = Integer.parseInt(dia);
            int mesInt = Integer.parseInt(mes);
            int anoInt = Integer.parseInt(ano);

            if (diaInt < 1 || diaInt > 31 || mesInt < 1 || mesInt > 12 || anoInt < 1900 || anoInt > 2025) {
                Log.i(TAG, "Fecha inválida: " + diaInt + "/" + mesInt + "/" + anoInt);
                Toast.makeText(this, "Fecha de nacimiento inválida. Verifica día (1-31), mes (1-12) y año (1900-2025)", Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (NumberFormatException e) {
            Log.e(TAG, "Error al parsear fecha", e);
            Toast.makeText(this, "Fecha de nacimiento inválida. Verifica día (1-31), mes (1-12) y año (1900-2025)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Todas las validaciones pasadas, mostrar progress bar
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        String fechaNacimiento = String.format("%04d-%02d-%02d", Integer.parseInt(ano), Integer.parseInt(mes), Integer.parseInt(dia));
        Usuario usuario = new Usuario(username, password, nombre, apellido, gmail, fechaNacimiento);

        AuthService authService = RetrofitClient.getClient().create(AuthService.class);
        authService.register(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.i(TAG, "Registro exitoso para usuario: " + username);
                    Toast.makeText(RegisterActivity.this, "¡Registro exitoso! Bienvenido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i(TAG, "Error en registro: código " + response.code());
                    Toast.makeText(RegisterActivity.this, "Error: usuario ya existente o datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error de conexión al registrar usuario: " + username, t);
                Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
