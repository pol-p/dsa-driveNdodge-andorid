package edu.upc.dsa_android_DriveNdodge.api;

import java.util.List;

import edu.upc.dsa_android_DriveNdodge.models.Evento;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventService {

    @GET("/v1/events")
    Call<List<Evento>> getEventos();

    @GET("/v1/eventos/list")
    Call<List<Evento>> getEvents();
    @POST("/v1/events/register/{username}/{eventId}")
    Call<Void> registerEvento(@Path("username") String username, @Path("eventId") int eventId);
}
