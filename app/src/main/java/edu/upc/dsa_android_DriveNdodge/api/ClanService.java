package edu.upc.dsa_android_DriveNdodge.api;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.models.Clan;
import edu.upc.dsa_android_DriveNdodge.models.ClanCreationRequest;
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

    @POST("/v1/clan/create")
    Call<Clan> createClan(@Body ClanCreationRequest request);

    @GET("/v1/clan/{clanName}/members")
    Call<List<Usuario>> getMembers(@Path("clanName") String clanName);

    @PUT("/v1/clan/join/{clanName}")
    Call<Void> joinClan(@Path("clanName") String clanName, @Body Usuario u);

    @PUT("/v1/clan/leave")
    Call<Void> leaveClan(@Body Usuario u);

    @GET("/v1/clan/{clanName}")
    Call<Clan> getClanInfo(@Path("clanName") String clanName);

}