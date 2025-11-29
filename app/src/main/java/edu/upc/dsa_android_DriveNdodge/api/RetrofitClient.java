package edu.upc.dsa_android_DriveNdodge.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit;

    // Si fas servir emulador android --> 10.0.2.2 (es un alias que apunta a localhost)
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    // Si fas servir dispositiu extern contectat al PC --> @IP del teu PC
    //private static final String BASE_URL = "http://172.20.10.3:8080/";


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
