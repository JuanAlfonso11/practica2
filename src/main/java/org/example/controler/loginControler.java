package org.example.controler;

import io.javalin.http.Context;
import org.example.BlogControler;
import org.example.models.usuario;
import io.javalin.Javalin;
import java.util.HashMap;
import java.util.Map;
import servicios.usuarioServices;
import org.jasypt.util.text.StrongTextEncryptor;

import static io.javalin.apibuilder.ApiBuilder.*;

public class loginControler {
    Javalin app;

    public static enum TipoUsuario {
        ADMIN, AUTOR, LECTOR
    }

    private StrongTextEncryptor encryptor;
    private static final String COOKIE_NAME = "Blog";

    public loginControler(Javalin app) {
        this.app = app;
    }

    public Boolean VerificarUsuario(String usuario, String password) {
        usuario user = usuarioServices.getInstance().getUsuarioByUsuario(usuario);
        if (user != null) {
            encryptor = new StrongTextEncryptor();
            encryptor.setPassword("PWeb");
            if (encryptor.decrypt(user.getPassword()).equals(password)) {
                return true;
            } else {
                return false;
            }
        } else {
            return null;
        }
    }

    public void rutas() {
        app.routes(() -> {
            app.before(ctx -> {
                handleCookie(ctx);

                String cookie = ctx.cookie(COOKIE_NAME);

                if (cookie != null) {
                    String[] cookieParts = cookie.split("-");
                    if (cookieParts != null && cookieParts.length == 2) {
                        if (cookieParts[0].equals("null") || cookieParts[1].equals("null")) {
                            ctx.sessionAttribute("usuario", null);
                            ctx.sessionAttribute("tipoUsuario", TipoUsuario.LECTOR);
                            return;
                        } else {
                            String usuario = cookieParts[0];
                            String password = cookieParts[1];
                            encryptor = new StrongTextEncryptor();
                            encryptor.setPassword("PWeb");
                            try {
                                usuario = encryptor.decrypt(usuario);
                                password = encryptor.decrypt(password);
                                if (VerificarUsuario(usuario, password) != null && VerificarUsuario(usuario, password)) {
                                    ctx.sessionAttribute("SBlog", cookie);
                                } else {
                                    ctx.cookie(COOKIE_NAME, null); // Invalidate the cookie
                                }
                            } catch (Exception e) {
                                ctx.cookie(COOKIE_NAME, null); // Invalidate the cookie
                            }
                        }
                    }
                } else {
                    System.out.println("No hay cookie");
                }

                String sesionBlog = ctx.sessionAttribute("SBlog");
                if (sesionBlog == null) {
                    ctx.sessionAttribute("SBlog", null);
                } else {
                    String[] sesionParts = sesionBlog.split("-");
                    if (sesionParts != null && sesionParts.length == 2) {
                        if (sesionParts[0].equals("null") || sesionParts[1].equals("null")) {
                            ctx.sessionAttribute("usuario", null);
                            ctx.sessionAttribute("tipoUsuario", TipoUsuario.LECTOR);
                            return;
                        } else {
                            String usuario = sesionParts[0];
                            String password = sesionParts[1];
                            encryptor = new StrongTextEncryptor();
                            encryptor.setPassword("PWeb");
                            try {
                                usuario = encryptor.decrypt(usuario);
                                password = encryptor.decrypt(password);
                                if (VerificarUsuario(usuario, password) != null && VerificarUsuario(usuario, password)) {
                                    usuario user = usuarioServices.getInstance().getUsuarioByUsuario(usuario);
                                    ctx.sessionAttribute("usuario", user);
                                    if (user.isAdmin()) {
                                        ctx.sessionAttribute("tipoUsuario", TipoUsuario.ADMIN);
                                    } else {
                                        ctx.sessionAttribute("tipoUsuario", TipoUsuario.AUTOR);
                                    }
                                } else {
                                    ctx.sessionAttribute("usuario", null);
                                    ctx.sessionAttribute("tipoUsuario", TipoUsuario.LECTOR);
                                }
                            } catch (Exception e) {
                                ctx.sessionAttribute("usuario", null);
                                ctx.sessionAttribute("tipoUsuario", TipoUsuario.LECTOR);
                            }
                        }
                    }
                }
            });

            path("/login", () -> {
                get("/", ctx -> {
                    ctx.redirect("/login/iniciarSesion");
                });

                get("/iniciarSesion", ctx -> {
                    Map<String, Object> models = new HashMap<>();
                    models.put("titulo", "Iniciar Sesion");
                    models.put("action", "/login/iniciarSesion");
                    ctx.render("/templates/login.html", models);
                });

                get("/registrarse", ctx -> {
                    Map<String, Object> models = new HashMap<>();
                    models.put("titulo", "Registrarse");
                    models.put("action", "/login/registrarse");
                    ctx.render("/templates/login.html", models);
                });

                get("/cerrarSesion", ctx -> {
                    ctx.sessionAttribute("usuario", null);
                    ctx.cookie(COOKIE_NAME, null, 1);
                    ctx.sessionAttribute("tipoUsuario", TipoUsuario.LECTOR);
                    ctx.sessionAttribute("SBlog", null);
                    ctx.redirect("/");
                });

                post("/iniciarSesion", ctx -> {
                    String usuario = ctx.formParam("usuario");
                    String password = ctx.formParam("password");
                    Boolean recordar = ctx.formParam("recordar") != null;

                    if (usuario.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                        Map<String, Object> models = new HashMap<>();
                        models.put("titulo", "Iniciar Sesion");
                        models.put("action", "/login/iniciarSesion");
                        models.put("mensaje", "no existe el usuario, intente de nuevo o regístrate ");
                        models.put("error", true);
                        ctx.render("/templates/login.html", models);
                        return;
                    }

                    usuario user = usuarioServices.getInstance().getUsuarioByUsuario(usuario);
                    if (VerificarUsuario(usuario, password) != null && VerificarUsuario(usuario, password)) {
                        String encryptedUser = encryptor.encrypt(user.getUsername());
                        String encryptedPassword = encryptor.encrypt(password);

                        if (recordar) {
                            setCookies(ctx, encryptedUser, encryptedPassword);
                        } else {
                            ctx.sessionAttribute("SBlog", encryptedUser + "-" + encryptedPassword);
                        }

                        ctx.sessionAttribute("usuario", user);
                        ctx.redirect("/");
                    } else {
                        Map<String, Object> models = new HashMap<>();
                        models.put("titulo", "Iniciar Sesion");
                        models.put("action", "/login/iniciarSesion");
                        models.put("mensaje", "no existe el usuario, intente de nuevo o regístrate ");
                        models.put("error", true);
                        ctx.render("/templates/login.html", models);
                    }
                });

                post("/registrarse", ctx -> {
                    String nombre = ctx.formParam("nombre");
                    String usuario = ctx.formParam("usuario");
                    String password = ctx.formParam("password");

                    // Verificar si hay campos vacíos
                    if (nombre.isBlank() || usuario.isBlank() || password.isBlank()) {
                        Map<String, Object> models = new HashMap<>();
                        models.put("titulo", "Registrarse");
                        models.put("action", "/login/registrarse");
                        models.put("mensaje", "No puede dejar campos vacíos");
                        models.put("error", true);
                        ctx.render("/templates/login.html", models);
                        return;
                    }

                    // Verificar si el usuario ya existe
                    if (usuarioServices.getInstance().getUsuarioByUsuario(usuario) != null) {
                        Map<String, Object> models = new HashMap<>();
                        models.put("titulo", "Registrarse");
                        models.put("action", "/login/registrarse");
                        models.put("mensaje", "Este usuario ya existe");
                        models.put("error", true);
                        ctx.render("/templates/login.html", models);
                        return;
                    }

                    // Crear un nuevo usuario
                    usuario user = new usuario(usuario, nombre, password, false, true);
                    encryptor = new StrongTextEncryptor();
                    encryptor.setPassword("PWeb");
                    String tokenUser = encryptor.encrypt(user.getUsername());
                    String tokenPassword = encryptor.encrypt(user.getPassword());

                    // Agregar el nuevo usuario
                    usuarioServices.getInstance().addUsuario(user);

                    if (ctx.formParam("recordar") != null) {
                        // Configurar la cookie si se seleccionó "recordar"
                        ctx.cookie("Blog", tokenUser + "-" + tokenPassword, 60 * 60 * 24 * 7);
                    } else {
                        // Configurar la sesión si no se seleccionó "recordar"
                        ctx.sessionAttribute("SBlog", tokenUser + "-" + tokenPassword);
                        ctx.sessionAttribute("usuario", user);
                    }

                    ctx.redirect("/");
                });

            });
        });
    }

    private void handleCookie(Context ctx) {
        String cookie = ctx.cookie(COOKIE_NAME);

        if (cookie != null && !cookie.isEmpty()) {
            String[] cookieParts = cookie.split("-");
            if (cookieParts != null && cookieParts.length == 2) {
                String usuario = cookieParts[0];
                String password = cookieParts[1];

                if (!usuario.equals("null") && !password.equals("null")) {
                    encryptor = new StrongTextEncryptor();
                    encryptor.setPassword("PWeb");

                    try {
                        usuario = encryptor.decrypt(usuario);
                        password = encryptor.decrypt(password);

                        if (VerificarUsuario(usuario, password) != null && VerificarUsuario(usuario, password)) {
                            ctx.sessionAttribute("SBlog", cookie);
                        } else {
                            ctx.removeCookie(COOKIE_NAME); // Invalidate the cookie
                        }
                    } catch (Exception e) {
                        ctx.removeCookie(COOKIE_NAME); // Invalidate the cookie
                    }
                }
            }
        }
    }

    private void setCookies(Context ctx, String encryptedUser, String encryptedPassword) {
        ctx.cookie(COOKIE_NAME, encryptedUser + "-" + encryptedPassword, 60 * 60 * 24 * 7);
        ctx.sessionAttribute("SBlog", encryptedUser + "-" + encryptedPassword);
    }
}
