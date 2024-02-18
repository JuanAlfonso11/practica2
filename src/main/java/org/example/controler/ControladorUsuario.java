package org.example.controler;
import org.example.BlogControler;
import org.example.models.usuario;
import io.javalin.Javalin;
import servicios.usuarioServices;

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
//                    boolean autor;
//                    if(ctx.formParam("autor")!=null){
//                        autor=true;
//                    }else{
//                        autor=false;
//                    }
//                    boolean admin;
//                    if(ctx.formParam("admin")!=null){
//                        admin=true;
//                    }else{
//                        admin=false;
//                    }

                    boolean autor= ctx.formParam("autor")!=null;
                    boolean admin= ctx.formParam("admin")!=null;
                    usuario usertemp = new usuario(ctx.formParam("username"),ctx.formParam("nombre"),ctx.formParam("password"),autor,admin);
                    BlogControler.getInstance().insertarUsuario(usertemp);
                    System.out.println(usertemp);
                    usuarioServices.getInstance().addUsuario(usertemp);
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
                  System.out.println(user);
                  BlogControler.getInstance().insertarUsuario(user);
                  ctxContext.sessionAttribute("usuario",user);

                    ctxContext.redirect("/");
              }
              else{
                  ctxContext.redirect("/registrarse");
              }
        });
        app.get("/editar/{usuarioID}",ctxContext->{
            usuario user = BlogControler.getInstance().buscarUsuarioByUserName(ctxContext.pathParam("usuarioID"));
            Map<String,Object> models = new HashMap<>();
            models.put("titulo","Editar Usuario");
            models.put("action","/manejadorUsuarios/editar");
            models.put("usuario",user);
            ctxContext.render("/templates/manejadorUsuarios/crear.html",models);
        });

        app.post("/manejadorUsuarios/editar",ctxContext->{
            boolean autor;
            if(ctxContext.formParam("autor")!=null){
                autor=true;
            }else{
                autor=false;
            }
            boolean admin;
            if(ctxContext.formParam("admin")!=null){
                admin=true;
            }else{
                admin=false;
            }
            usuario user = BlogControler.getInstance().buscarUsuarioByUserName(ctxContext.formParam("usuario"));
            user.setNombre(ctxContext.formParam("nombre"));
            user.setUsername(ctxContext.formParam("usuario"));
            user.setPassword(ctxContext.formParam("password"));
            user.setAutor(autor);
            user.setAdmin(admin);
            BlogControler.getInstance().actualizarUsuario(user);
            usuarioServices.getInstance().modificarUsuario(user);
            ctxContext.redirect("/manejadorUsuarios/listar");
        });
        app.get("/eliminar/{usuarioID}",ctxContext->{
            BlogControler.getInstance().buscarUsuarioByUserName(ctxContext.pathParam("usuarioID"));
            ctxContext.redirect("/manejadorUsuarios");
        });
    }
}
