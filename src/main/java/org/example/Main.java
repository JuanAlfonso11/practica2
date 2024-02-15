package org.example;

import  org.example.models.*;
import org.example.controler.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import java.util.*;
import static io.javalin.apibuilder.ApiBuilder.*;
import java.util.Date;

public class Main {

    private  static final List<String>names = new ArrayList<>();
    public static void main(String[] args) {
        JavalinRenderer.register(new JavalinThymeleaf(), ".html");


        var app = Javalin.create(javalinConfig->{
            BlogControler.getInstance().insertarUsuario(new usuario("admin","admin","admin",true,true));
            BlogControler.getInstance().insertarArticulo(new Articulo(0,"titulo","cuerpo",new usuario("admin","admin","admin",true,true)));

            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath="/";
                staticFileConfig.directory= "/public";
                staticFileConfig.location = Location.CLASSPATH;

            });

        }).start(7000);
        app.get("/",ctx->{
            Map<String,Object> models = new HashMap<>();
            models.put("titulo","Blog");
            models.put("lista",BlogControler.getInstance().getArticulos());
            ctx.render("/templates/blog.html",models);
        });

        new ControladorUsuario(app).rutas();
        new ControladorArticulos(app).rutas();
        new loginControler(app).rutas();
        new controladorpublicacion(app).rutas();

        app.get("/noentocntrado",ctx->{
            ctx.render("/templates/404.html");
        });
    }
}