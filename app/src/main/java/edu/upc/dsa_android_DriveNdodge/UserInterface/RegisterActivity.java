package edu.upc.dsa_android_DriveNdodge.UserInterface;

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
public class RegisterActivity extends AppCompatActivity{
    private EditText usernameIn;
    private EditText passwordIn;
    private Button registerBttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        registerBttn = findViewById(R.id.registerBttn);

        registerBttn.setOnClickListener(v -> doRegister());
    }

    private void doRegister(){
        String username=usernameIn.getText().toString();
        String password=passwordIn.getText().toString();

        if(username.isEmpty()||password.isEmpty()){
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario(username,password);

        // creamos servicio retrofit
        AuthService authService = RetrofitClient.getClient().create(AuthService.class);

        // llamar al endpoint register y ejecutar peticion HTTP POST
        authService.register(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    // servidor devuelve codigo 2xx
                    Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //servidor devuelve codigo 4xx o 500
                    Toast.makeText(RegisterActivity.this, "Error: parametros mal pasado o usuario ya existente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
