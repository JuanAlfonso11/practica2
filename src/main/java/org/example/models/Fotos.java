package org.example.models;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "fotos")
public class Fotos  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "foto")
    private String foto;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id")
    private Articulo articulo;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private  usuario usuario;

    public Fotos(String foto, String type, Articulo articulo) {
        this.foto = foto;
        this.type = type;
        this.articulo = articulo;
    }

    public Fotos() {

    }

    public Long getId() {
        return id;
    }

    public String getFoto() {
        return foto;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
