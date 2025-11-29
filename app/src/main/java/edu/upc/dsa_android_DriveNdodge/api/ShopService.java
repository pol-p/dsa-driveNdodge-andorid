package edu.upc.dsa_android_DriveNdodge.api;

import com.google.gson.annotations.SerializedName;

import edu.upc.dsa_android_DriveNdodge.models.Item;
import edu.upc.dsa_android_DriveNdodge.models.UserProfile;
import edu.upc.dsa_android_DriveNdodge.models.UsrRanking;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface ShopService {

    @GET("/v1/shop/items")
    Call<List<Item>> getItems();

    @POST("/v1/shop/buy/{id}")
    Call<Void> buyItem(@Path("id") Integer itemId, @Body String username);

    public class MonedasResponse {
        @SerializedName("coins") // Quitar en un futuro error en backend pasamos el objeto de las moendas con el nombre coins {try / catch} Linea 87 el shopService
        private int monedas;

        public int getMonedas() { return monedas; }
        public void setMonedas(int monedas) { this.monedas = monedas; }
    }
    @GET("/v1/shop/monedas/{username}")
    Call<MonedasResponse> getMonedas(@Path("username") String username);

    @GET("/v1/shop/ranking")
    Call<List<UsrRanking>> getRanking();

    @GET("/v1/shop/perfil/{username}")
    Call<UserProfile> getProfile(@Path("username") String username);
}
