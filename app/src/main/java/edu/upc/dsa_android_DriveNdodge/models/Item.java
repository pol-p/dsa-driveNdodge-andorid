package edu.upc.dsa_android_DriveNdodge.models;

public class Item {

    private Integer id;
    private String nombre;
    private String descripcion;
    private int precio;

    public Item(){} // para deserializar
    public Item(Integer id, String nombre, String descripcion, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public int getPrecio() {return precio;}
    public void setPrecio(int precio) {this.precio = precio;}
}
