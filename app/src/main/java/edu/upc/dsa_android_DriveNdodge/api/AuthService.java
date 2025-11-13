package edu.upc.dsa_android_DriveNdodge.api;

import edu.upc.dsa_android_DriveNdodge.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface AuthService {
    @POST("/dsaApp/auth/register")
    Call<Usuario> register(@Body Usuario usuario);

    @POST("/dsaApp/auth/login")
    Call<Usuario> login(@Body Usuario usuario);
}
