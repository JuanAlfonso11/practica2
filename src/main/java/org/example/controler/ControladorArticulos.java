package org.example.controler;
import org.example.BlogControler;
import org.example.models.Articulo;
import org.example.models.Etiqueta;
import org.example.models.usuario;
import org.example.models.comentario;
import io.javalin.Javalin;

import java.util.*;
import java.text.SimpleDateFormat;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ControladorArticulos {
    Javalin app;
    public ControladorArticulos(Javalin app){
        this.app = app;
    }
    public void  rutas(){
        app.routes(()->{
            path("/postear/",()->{
                before(ctx->{
                    usuario user = ctx.sessionAttribute("usuario");
                    if (!user.isAutor()) {
                        if(ctx.path().startsWith("/postear/")){
                            ctx.redirect("/noentocntrado");
                        }
                    }
                });
                get("/",ctx->{
                    ctx.redirect("/postear/listar");
                });
                get("listar",ctx->{
                    List<Articulo> lista= null;
                    Map<String,Object>models = new HashMap<>();
                    usuario user = ctx.sessionAttribute("usuario");
                    if(BlogControler.getInstance().buscarUsuarioByUserName(user.getUsername()).isAdmin()){
                        lista = BlogControler.getInstance().getArticulos();
                    }else if (BlogControler.getInstance().buscarUsuarioByUserName(user.getUsername()).isAutor()){
                        lista = BlogControler.getInstance().busarArticulosByuername(user.getUsername());
                    }
                    models.put("titulo","Listar Articulos");
                    models.put("lista",lista);
                    ctx.render("/templates/postear/listar.html",models);
                });
                get("/crear",ctx->{
                    Map<String,Object>models = new HashMap<>();
                    models.put("titulo","Crear Articulo");
                    models.put("action","/postar/crear");
                    models.put("dart",BlogControler.getInstance().crearIdArticulo());
                    ctx.render("/templates/postear/crear.html",models);
                });
                post("/crear",ctx->{
                    usuario user = ctx.sessionAttribute("usuario");
                    String[] etiquetasArray = ctx.formParam("etiquetas").split(",");
                    ArrayList<Etiqueta> etiquetas = new ArrayList<>();
                    for(int i=0;i<etiquetasArray.length;i++){
                        Etiqueta etiqueta =new Etiqueta((long) i,etiquetasArray[i]);
                        etiquetas.add(etiqueta);
                    }
                    Articulo temp = new Articulo(Long.parseLong(ctx.formParam("dart")),ctx.formParam("titulo"),ctx.formParam("cuerpo"),user);
                    temp.setListaEtiquetas(etiquetas);
                    BlogControler.getInstance().insertarArticulo(temp);
                    ctx.redirect("/postear");
                });
                get("/eliminar/{articuloid}",ctx->{
                    BlogControler.getInstance().eliminarArticuloById(ctx.pathParamAsClass("articuloid",long.class).get());
                    ctx.redirect("/postear");
                });
                get("/editar/{articuloid}",ctx->{
                    Articulo nuevo = BlogControler.getInstance().buscarArticuloById(ctx.pathParamAsClass("articuloid",long.class).get());
                    Map<String,Object>models = new HashMap<>();
                    models.put("titulo","Editar Articulo");
                    models.put("action","/postar/editar");
                    models.put("articulo",nuevo);
                    models.put("dart",ctx.pathParamAsClass("articuloid",long.class).get());
                    ctx.render("/templates/postear/crear.html",models);
                });
                post("/editar",ctx->{
                    String[] etiquetasArray = ctx.formParam("etiquetas").split(",");
                    ArrayList<Etiqueta> etiquetas = new ArrayList<>();
                    for(int i=0;i<etiquetasArray.length;i++){
                        Etiqueta etiqueta =new Etiqueta((long) i,etiquetasArray[i]);
                        etiquetas.add(etiqueta);
                    }
                    System.out.println(BlogControler.getInstance().buscarArticuloById(Long.parseLong(ctx.formParam("dart"))).getAutor());
                    Articulo temp = new Articulo(Long.parseLong(ctx.formParam("dart")),ctx.formParam("titulo"),ctx.formParam("cuerpo"),BlogControler.getInstance().buscarArticuloById(Long.parseLong(ctx.formParam("dart"))).getAutor());
                    temp.setListaEtiquetas(etiquetas);
                    BlogControler.getInstance().actualizarArticulo(temp);
                    ctx.redirect("/postear");
                });
            });
        });

    }
}
