package edu.upc.dsa_android_DriveNdodge.api;

import edu.upc.dsa_android_DriveNdodge.models.InventarioRequest;
import edu.upc.dsa_android_DriveNdodge.models.Partida;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GameService {
    @POST("/v1/game/partida")
    Call<Void> saveGame(@Body Partida partida);
    @POST("/v1/game/inventario")
    Call<Void> updateInventario(@Body InventarioRequest request);
}