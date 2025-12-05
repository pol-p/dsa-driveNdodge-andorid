package edu.upc.dsa_android_DriveNdodge.models;

import com.google.gson.annotations.SerializedName;

public class MonedasResponse {
    @SerializedName("coins") // Quitar en un futuro error en backend pasamos el objeto de las moendas con el nombre coins {try / catch} Linea 87 el shopService
    private int monedas;

    public int getMonedas() { return monedas; }
    public void setMonedas(int monedas) { this.monedas = monedas; }
}
