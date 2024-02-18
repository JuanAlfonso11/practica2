package org.example.controler;
import io.javalin.Javalin;
import org.example.BlogControler;
import org.example.models.*;
import java.util.HashMap;
import java.util.Map;
import static io.javalin.apibuilder.ApiBuilder.*;
public class controladorpublicacion {
    Javalin app;
    public controladorpublicacion(Javalin app){
        this.app = app;
    }
    public void rutas(){
        app.routes(()->{
            path("/publicacion/",()->{
                get("/{articulo}",ctx->{
                    if (ctx.pathParam("articulo") != null) {
                        Long articuloId = ctx.pathParamAsClass("articulo",Long.class).get();
                        if (BlogControler.getInstance().buscarArticuloById(articuloId)!=null){
                            Articulo temp = BlogControler.getInstance().buscarArticuloById(articuloId);
                            Map<String,Object> models = new HashMap<>();
                            models.put("titulo",temp.getTitulo());
                            models.put("autor",temp.getAutor());
                            models.put("fecha",temp.fechaFormateada());
                            models.put("cuerpo",temp.getCuerpo());
                            models.put("lista",temp.getListaComentarios());

                            usuario user = ctx.sessionAttribute("usuario");
                            if(user!=null){
                                models.put("tipo", "Logout");
                                models.put("sitio", "/login/cerrarSesion");
                            }else {
                                models.put("tipo", "Login");
                                models.put("sitio", "/login/iniciarSesion");
                            }
                            if(user!=null){
                                models.put("condicion",true);
                            } else {
                                models.put("condicion",false);
                            }
                            ctx.render("/templates/publicacion.html",models);
                        }else {
                            ctx.redirect("/");
                        }
                    }
                });
                post("/{articulo}/comment", ctx->{
                    if (ctx.pathParam("articulo") != null) {
                        Long articuloId = ctx.pathParamAsClass("articulo",Long.class).get();
                        usuario user = ctx.sessionAttribute("usuario");
                        Articulo temp = BlogControler.getInstance().buscarArticuloById(articuloId);
                        comentario com = new comentario(temp.generarCommentId(),ctx.formParam("texto"),user,temp);
                        System.out.println(com);
                        BlogControler.getInstance().crearComentario(articuloId,com);
                        ctx.redirect("/publicacion/"+ctx.pathParam("articulo"));
                    }
                });
            });
        });
    }
}
