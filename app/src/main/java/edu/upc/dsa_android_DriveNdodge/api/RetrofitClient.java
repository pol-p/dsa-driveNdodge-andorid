package edu.upc.dsa_android_DriveNdodge.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static Retrofit retrofit;

    // Si fas servir emulador android --> 10.0.2.2 (es un alias que apunta a localhost)
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    // Si fas servir dispositiu extern contectat al PC --> @IP del teu PC
    //private static final String BASE_URL = "http://172.20.10.3:8080/";
  //private static final String BASE_URL = "http://192.168.1.101:8080/";
    // private static final String BASE_URL = "https://dsa5.upc.edu/";


    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
