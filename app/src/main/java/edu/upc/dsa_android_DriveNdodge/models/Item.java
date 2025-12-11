package edu.upc.dsa_android_DriveNdodge.models;

import edu.upc.dsa_android_DriveNdodge.api.RetrofitClient;

public class Item {

    private Integer id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String imagen;

    public Item(){} // para deserializar
    public Item(Integer id, String nombre, String descripcion, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getImagen() {
        if (imagen == null || imagen.isEmpty()){
            return null;
        }

        if (imagen.startsWith("http")){
            return imagen;
        }
        return RetrofitClient.getBaseUrl() + imagen;
    }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public int getPrecio() {return precio;}
    public void setPrecio(int precio) {this.precio = precio;}
}
