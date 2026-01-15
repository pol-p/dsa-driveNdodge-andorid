package edu.upc.dsa_android_DriveNdodge.api;

import edu.upc.dsa_android_DriveNdodge.models.ChatRequest;
import edu.upc.dsa_android_DriveNdodge.models.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatService {
    @POST("/v1/chat")
    Call<MessageResponse> sendMessage(@Body ChatRequest request);
}