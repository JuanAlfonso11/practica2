package org.example.models;

import java.util.Date;
import java.text.SimpleDateFormat;
public class comentario {

    private long id;
    private String comentario;
    private usuario autor;
    private Date fecha;
    private Articulo articulo;

    public comentario(long id, String comentario, usuario autor, Articulo articulo) {
        this.id = id;
        this.comentario = comentario;
        this.autor = autor;
        this.articulo = articulo;
        this.fecha = new Date();
    }
    public String fechaFormateada(){
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formateador.format(fecha);
        return fechaFormateada;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public usuario getAutor() {
        return autor;
    }
    public void setAutor(usuario autor) {
        this.autor = autor;
    }
    public Articulo getArticulo() {
        return articulo;
    }
    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

}
