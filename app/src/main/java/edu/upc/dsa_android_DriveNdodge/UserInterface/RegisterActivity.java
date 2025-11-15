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

import android.content.Intent;

public class RegisterActivity extends AppCompatActivity{
    private EditText usernameIn;
    private EditText passwordIn;
    private EditText passwordConfirmIn;
    private EditText nombreIn;
    private EditText apellidoIn;
    private EditText gmailIn;

    // pel fechaNacimiento
    private EditText diaIn, mesIn, anoIn;
    private Button registerBttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameIn = findViewById(R.id.usernameIn);
        passwordIn = findViewById(R.id.passwordIn);
        nombreIn = findViewById(R.id.nombreIn);
        apellidoIn = findViewById(R.id.apellidoIn);
        gmailIn = findViewById(R.id.gmailIn);
        passwordIn = findViewById(R.id.passwordIn);
        passwordConfirmIn= findViewById(R.id.passwordConfirmIn);
        // pel fechaNacimiento
        diaIn = findViewById(R.id.diaIn);
        mesIn = findViewById(R.id.mesIn);
        anoIn = findViewById(R.id.anoIn);

        registerBttn = findViewById(R.id.registerBttn);
        registerBttn.setOnClickListener(v -> doRegister());
    }

    private void doRegister(){

        String username=usernameIn.getText().toString().toLowerCase();
        String password=passwordIn.getText().toString();
        String passwordConfirm = passwordConfirmIn.getText().toString();
        String nombre=nombreIn.getText().toString();
        String apellido=apellidoIn.getText().toString();
        String gmail=gmailIn.getText().toString().toLowerCase();
        // pel fechaNacimiento
        String dia = diaIn.getText().toString();
        String mes = mesIn.getText().toString();
        String ano = anoIn.getText().toString();



        if(username.isEmpty()||password.isEmpty()||passwordConfirm.isEmpty()||nombre.isEmpty()||apellido.isEmpty()||gmail.isEmpty()||dia.isEmpty()||mes.isEmpty()||ano.isEmpty()){
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(passwordConfirm)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // combinem per tenir format dd-mm-yyyy
        String fechaNacimiento = String.format("%02d", Integer.parseInt(dia)) + "-" +
                String.format("%02d", Integer.parseInt(mes)) + "-" +
                ano;

        Usuario usuario = new Usuario(username,password,nombre,apellido,gmail,fechaNacimiento);

        // creamos servicio retrofit
        AuthService authService = RetrofitClient.getClient().create(AuthService.class);

        // llamar al endpoint register y ejecutar peticion HTTP POST
        authService.register(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    // servidor devuelve codigo 2xx
                    Toast.makeText(RegisterActivity.this, "Registro exitoso, ¡bienvenido!  ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
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
