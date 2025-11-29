package edu.upc.dsa_android_DriveNdodge.models;

public class UserProfile {
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private int monedas;
    private int mejorPuntuacion;
    private String fechaNacimiento;

    public UserProfile() {
    }

    public UserProfile(String username, String nombre, String apellido, String email, int monedas, int mejorPuntuacion, String fechaNacimiento) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.monedas = monedas;
        this.mejorPuntuacion = mejorPuntuacion;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public int getMonedas() { return monedas; }
    public int getMejorPuntuacion() { return mejorPuntuacion; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMonedas(int monedas) {
        this.monedas = monedas;
    }

    public void setMejorPuntuacion(int mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

}