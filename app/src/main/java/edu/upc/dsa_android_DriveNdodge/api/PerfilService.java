package edu.upc.dsa_android_DriveNdodge.api;

import edu.upc.dsa_android_DriveNdodge.models.UsrProfile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PerfilService {
    @GET("/v1/perfil/{username}")
    Call<UsrProfile> getProfile(@Path("username") String username);
}
