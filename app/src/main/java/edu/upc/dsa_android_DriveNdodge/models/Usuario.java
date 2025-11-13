package edu.upc.dsa_android_DriveNdodge.models;

public class Usuario {

    private String username;
    private String password;
    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {return username;}
    public String getPassword() {return password;}


}
