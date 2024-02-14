package org.example.controler;
import org.example.BlogControler;
import org.example.models.usuario;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.javalin.apibuilder.ApiBuilder.*;
public class ControladorUsuario {
    Javalin app;

    public ControladorUsuario(Javalin app){
        this.app = app;
    }
    public void rutas(){
        app.routes(()->{
            path("/manejadorUsuarios/",()->{
                get("/",ctx->{
                    ctx.redirect("/manejadorUsuarios/listar");
                });
                get("/listar",ctx->{
                    List<usuario> lista= BlogControler.getInstance().getUsuarios();
                    Map<String,Object> models = new HashMap<>();
                    models.put("titulo","Listar Usuarios");
                    models.put("lista",lista);
                    ctx.render("/templates/manejadorUsuarios/listar.html",models);
                });
                get("/crear",ctx->{
                    Map<String,Object> models = new HashMap<>();
                    models.put("titulo","Crear Usuario");
                    models.put("action","/manejadorUsuarios/crear");
                    ctx.render("/templates/manejadorUsuarios/crear.html",models);
                });
                post("/crear",ctx->{
                    boolean autor;
                    if(ctx.formParam("autor")!=null){
                        autor=true;
                    }else{
                        autor=false;
                    }
                    boolean admin;
                    if(ctx.formParam("admin")!=null){
                        admin=true;
                    }else{
                        admin=false;
                    }
                    usuario usertemp = new usuario(ctx.formParam("nombre"),ctx.formParam("usuario"),ctx.formParam("password"),autor,admin);
                    BlogControler.getInstance().insertarUsuario(usertemp);
                    ctx.redirect("/manejadorUsuarios");
                });
            });
        });
        app.post("/autenticar",ctxContext->{
            String nombreUsuario = ctxContext.formParam("usuario");
            String password = ctxContext.formParam("password");
            usuario user = BlogControler.getInstance().autenticarUsuario(nombreUsuario,password);
            ctxContext.sessionAttribute("usuario",user);
            ctxContext.redirect("/");
        });
        app.get("/crearusuario",ctxContext->{
           boolean autor=false;
           if(ctxContext.queryParam("autor")==null){
               autor=false;
           }
              else{
                autor=true;
              }
              if(BlogControler.getInstance().buscarUsuarioByUserName(ctxContext.queryParam("usuario"))==null){
                  usuario user = new usuario(ctxContext.queryParam("usuario"),ctxContext.queryParam("user"),ctxContext.queryParam("password"),false,autor);

                  BlogControler.getInstance().insertarUsuario(user);
                  ctxContext.sessionAttribute("usuario",user);
                    ctxContext.redirect("/");
              }
              else{
                  ctxContext.redirect("/registrarse");
              }
        });
    }
}
