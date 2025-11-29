package edu.upc.dsa_android_DriveNdodge.models;

public class UsrRanking {
    private String username;
    private String nombre;

    private Integer mejorPuntuacion;

    public UsrRanking() {
    }

    public UsrRanking(String username, String nombre, Integer mejorPuntuacion) {
        this.username = username;
        this.nombre = nombre;
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMejorPuntuacion() {
        return mejorPuntuacion;
    }

    public void setMejorPuntuacion(Integer mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }
}
