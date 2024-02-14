package org.example.controler;
import io.javalin.Javalin;
import org.example.BlogControler;
import org.example.models.*;
import java.util.HashMap;
import java.util.List;
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
                    if (BlogControler.getInstance().buscarArticuloById(ctx.pathParamAsClass("articulo",Long.class).get())!=null){
                        Articulo temp = BlogControler.getInstance().buscarArticuloById(ctx.pathParamAsClass("articulo",Long.class).get());
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

                });
                post("/{articulo}/comment", ctx->{
                    usuario user = ctx.sessionAttribute("usuario");
                    Articulo temp = BlogControler.getInstance().buscarArticuloById(ctx.pathParamAsClass("articulo",Long.class).get());
                    comentario com = new comentario(temp.generarCommentId(),ctx.formParam("texto"),user,temp);
                    BlogControler.getInstance().crearComentario(ctx.pathParamAsClass("articulo",Long.class).get(),com);
                    ctx.redirect("/publicacion/"+ctx.pathParam("articulo"));
                });
            });
        });
    }

}
