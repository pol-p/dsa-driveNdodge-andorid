package edu.upc.dsa_android_DriveNdodge.models;

public class Partida {
    private String username;
    private int puntos;
    private int monedas;

    public Partida(String username, int puntos, int monedas) {
        this.username = username;
        this.puntos = puntos;
        this.monedas = monedas;
    }

    public Partida() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }
}