package org.example;

import io.javalin.plugin.bundled.RouteOverviewPlugin;
import  org.example.models.*;
import org.example.controler.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import servicios.DatabaseService;
import servicios.FotoServices;
import servicios.GestionDb;
import servicios.usuarioServices;
import java.util.*;
import static io.javalin.apibuilder.ApiBuilder.*;
import java.util.Date;

public class Main {

    private static String modoConexion = "";
    private  static final List<String>names = new ArrayList<>();
    public static void main(String[] args) {

        if(modoConexion.isEmpty()){
            DatabaseService.getInstancia().init();
        }

        JavalinRenderer.register(new JavalinThymeleaf(), ".html");


        var app = Javalin.create(javalinConfig->{
            BlogControler.getInstance().insertarUsuario(new usuario("admin","admin","admin",true,true));
            BlogControler.getInstance().insertarArticulo(new Articulo(0,"titulo","cuerpo",new usuario("admin","admin","admin",true,true)));

            javalinConfig.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath="/";
                staticFileConfig.directory= "/public";
                staticFileConfig.location = Location.CLASSPATH;

            });
            javalinConfig.plugins.register(new RouteOverviewPlugin("/rutas"));
            javalinConfig.plugins.enableCors(corsContainer ->{
                corsContainer.add(corsPluginConfig ->{
                    corsPluginConfig.anyHost();
                });
            });

        }).start(getHerokuAssignedPort());
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
    static int getHerokuAssignedPort(){
        ProcessBuilder processBuilder = new ProcessBuilder();
        if(processBuilder.environment().get("PORT")!=null){
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000;
    }

    public static String getModoConexion(){
        return modoConexion;
    }
}