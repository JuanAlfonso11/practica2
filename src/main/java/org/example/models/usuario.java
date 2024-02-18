package org.example.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import org.mindrot.jbcrypt.BCrypt;
@Entity
@Table(name = "usuario")
public class usuario  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "admin")
    private boolean admin;
    @Column(name = "autor")
    private boolean autor;

    @OneToMany(mappedBy = "usuario")
    private  Set<Fotos> fotos;


    public usuario(String username, String nombre, String password, boolean admin, boolean autor) {
        this.username = username;
        this.nombre = nombre;
        this.password = hashPassword(password);
        this.admin = admin;
        this.autor = autor;
    }

    public usuario() {

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


    public void setFoto(String foto) {
    }

    private String hashPassword(String plainTextPassword) {
        // You can adjust the strength factor (12) based on your security requirements
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
}

}