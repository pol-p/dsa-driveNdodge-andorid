package edu.upc.dsa_android_DriveNdodge.api;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.models.Clan;
import edu.upc.dsa_android_DriveNdodge.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClanService {

    @GET("/v1/clan/all")
    Call<List<Clan>> getAllClans();

}