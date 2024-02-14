package org.example.controler;
import org.example.BlogControler;
import org.example.models.usuario;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.*;
public class loginControler {
    Javalin app;

    public loginControler(Javalin app){
        this.app = app;
    }
    public void rutas(){
        app.routes(()->{
            path("/login",()->{
                get("/",ctx->{
                    ctx.redirect("/login/iniciarSesion");
                });
                get("/iniciarSesion",ctx->{
                    Map<String,Object>models = new HashMap<>();
                    models.put("titulo","Iniciar Sesion");
                    models.put("action","/login/iniciarSesion");
                    ctx.render("/templates/login.html",models);
                });
                get("/registrarse",ctx->{
                    Map<String,Object> models= new HashMap<>();
                    models.put("titulo","Registrarse");
                    models.put("action","/login/registrarse");
                    ctx.render("/templates/login.html",models);
                });
                get("/cerrarSesion",ctx->{
                    ctx.sessionAttribute("usuario",null);
                    ctx.redirect("/");
                });
                post("/iniciarSesion",ctx->{
                    String usuario= ctx.formParam("usuario");
                    String password= ctx.formParam("password");
                    usuario user  = BlogControler.getInstance().buscarUsuarioByUserName(usuario);
                    if(user != null && user.getPassword().equals(password)){
                        ctx.sessionAttribute("usuario",user);
                        ctx.redirect("/");
                    }else {
                        Map<String, Object> models = new HashMap<>();
                        models.put("titulo", "Iniciar Sesion");
                        models.put("action", "/login/iniciarSesion");
                        models.put("mensaje", "no existe el usuario, intente de nuevo o registrate ");
                        models.put("error", true);
                        models.put("/templates/login.html", models);
                    }

                });
                post("/registrarse",ctx->{
                    String nombre = ctx.formParam("nombre");
                    String usuario = ctx.formParam("usuario");
                    String password = ctx.formParam("password");
                    ctx.sessionAttribute("usuario",null);
                    if(nombre.equalsIgnoreCase("")|| usuario.equalsIgnoreCase("")|| password.equalsIgnoreCase("")){
                        Map<String,Object> models = new HashMap<>();
                        models.put("titulo","Registrarse");
                        models.put("action","/login/registrarse");
                        models.put("mensaje","No puede dejar campos vacios");
                        models.put("error",true);
                        ctx.render("/templates/login.html",models);
                    }else{
                        usuario user = new usuario(usuario,password,nombre,false,false);
                        BlogControler.getInstance().insertarUsuario(user);
                        ctx.sessionAttribute("usuario",user);
                        ctx.redirect("/");
                    }
                });


            });
        });
    }

}
