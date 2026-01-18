package edu.upc.dsa_android_DriveNdodge.models;

public class ClanRanking {
    private String nombre;
    private String imagen;
    private double puntosTotales;

    public ClanRanking(String nombre, String imagen, double puntosTotales) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.puntosTotales = puntosTotales;
    }

    public ClanRanking() {
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(double puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public String getNombre() {
        return nombre;
    }
    public String getImagen() {
        return imagen;
    }
    public int getPuntos() {
        return (int) puntosTotales;
    }

}
