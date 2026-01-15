package edu.upc.dsa_android_DriveNdodge.models;

import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;

public class Evento {
    private int id;
    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String imagen;

    public Evento() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public String getImagen() {
        if (imagen == null || imagen.isEmpty()) return null;
        if (imagen.startsWith("http")) return imagen;
        return RetrofitClient.getBaseUrl() + imagen;
    }

    public void setImagen(String imagen) { this.imagen = imagen; }
}
