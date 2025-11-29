package edu.upc.dsa_android_DriveNdodge.models;

public class Usuario {

    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private String fechaNacimiento;

    // constructor para el register
    public Usuario(String username, String password, String nombre, String apellido, String email, String fechaNacimiento) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
    }
    // constructor para el login
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}
    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}




}
