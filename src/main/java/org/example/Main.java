package org.example;

import  org.example.models.*;
import org.example.controler.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import java.util.*;
import static io.javalin.apibuilder.ApiBuilder.*;
public class Main {

    private  static final List<String>names = new ArrayList<>();
    public static void main(String[] args) {
        JavalinRenderer.register(new JavalinThymeleaf(), ".html");

        var app = Javalin.create(javalinConfig->{
            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath="/";
                staticFileConfig.directory= "/public";
                staticFileConfig.location = Location.CLASSPATH;

            });

        }).start(7000);
        new ControladorUsuario(app).rutas();
        new ControladorArticulos(app).rutas();
        new loginControler(app).rutas();
        app.get("/",ctx->{
            ctx.redirect("/blog");
        });
        app.get("/noentocntrado",ctx->{
            ctx.render("/templates/404.html");
        });
    }
}