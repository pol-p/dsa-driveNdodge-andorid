package edu.upc.dsa_android_DriveNdodge.api;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.models.UsrRanking;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RankingService {
    @GET("/v1/ranking/lista")
    Call<List<UsrRanking>> getRanking();
}
