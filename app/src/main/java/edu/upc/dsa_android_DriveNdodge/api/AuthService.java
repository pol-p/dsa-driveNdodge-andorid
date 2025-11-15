package edu.upc.dsa_android_DriveNdodge.api;

import edu.upc.dsa_android_DriveNdodge.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface AuthService {

    // Registrar un nuevo usuario
    @POST("/v1/auth/register")
    Call<Usuario> register(@Body Usuario usuario);

    // Iniciar sesión
    @POST("/v1/auth/login")
    Call<Usuario> login(@Body Usuario usuario);
}
