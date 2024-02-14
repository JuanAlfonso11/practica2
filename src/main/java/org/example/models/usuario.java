package org.example.models;

public class usuario {
    private String username;
    private String password;
    private String nombre;

    private boolean admin;
    private boolean autor;


    public usuario(String username, String password, String nombre, boolean admin, boolean autor) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.admin = admin;
        this.autor = autor;
    }
public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAutor() {
        return autor;
    }

    public void setAutor(boolean autor) {
        this.autor = autor;
    }

}