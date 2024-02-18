package org.example.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Etiqueta")
public class Etiqueta {
    @Id
    private long id;
    private String etiqueta;

    @ManyToOne
    //@JoinColumn(name="articulo_id")
    private Articulo articulo;

    public Etiqueta(long id, String etiqueta) {
        this.id = id;
        this.etiqueta = etiqueta;
    }

    public Etiqueta(String etiqueta) {

    }

    public Etiqueta() {

    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEtiqueta() {
        return etiqueta;
    }
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
