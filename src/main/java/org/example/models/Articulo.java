package org.example.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Articulo")
public class Articulo implements Serializable {
    @Id
    private long id;
    private String titulo;
    private String cuerpo;
    @OneToOne
    private usuario autor;
    private Date fecha;
    @OneToMany
    private List<comentario> listaComentarios;
    @OneToMany
    private List<Etiqueta> listaEtiquetas;

    public Articulo(long id, String titulo, String cuerpo, usuario autor) {
        this.id = id;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.autor = autor;
        this.fecha = new Date();
        this.listaComentarios = new ArrayList<>();
        this.listaEtiquetas = new ArrayList<>();
    }

    public Articulo() {

    }

    public long generarCommentId(){
        long temp = 0;
        if(listaComentarios.isEmpty()){
            return 0;
        }else{
            for (comentario coment: listaComentarios) {
                temp++;
            }
        }
        return temp;
    }
    public void agregarComentario(comentario comment){
        listaComentarios.add(comment);
    }
    public String getCuerpo70(){
        if (cuerpo.length() <= 70) {
            return cuerpo;
        } else {
            return cuerpo.substring(0, 70);
        }
    }
    public String fechaFormateada(){
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formateador.format(fecha);
        return fechaFormateada;
    }
    public void addComentario(comentario co){
       listaComentarios.add(co);
    }
    public String showEtiquetas() {
        if (listaEtiquetas.size() == 1) {
            return listaEtiquetas.get(0).getEtiqueta();
        } else {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < listaEtiquetas.size(); i++) {
                if (i > 0) {
                    str.append(", ");
                }
                str.append(listaEtiquetas.get(i).getEtiqueta());
            }
            return str.toString();
        }
    }
    public void addEtiqueta(Etiqueta et){
        listaEtiquetas.add(et);
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getCuerpo() {
        return cuerpo;
    }
    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }
    public usuario getAutor() {
        return autor;
    }
    public void setAutor(usuario autor) {
        this.autor = autor;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public List<comentario> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(List<comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    public List<Etiqueta> getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(List<Etiqueta> listaEtiquetas) {
        this.listaEtiquetas = listaEtiquetas;
    }


}
