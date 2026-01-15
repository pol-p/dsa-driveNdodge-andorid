package edu.upc.dsa_android_DriveNdodge.models;
import com.google.gson.annotations.SerializedName;

public class ChatRequest {
    @SerializedName("prompt")
    private String message;

    public ChatRequest(String message) {
        this.message = message;
    }

    public ChatRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}