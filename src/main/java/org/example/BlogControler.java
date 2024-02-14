package org.example;
import org.example.models.*;

import java.util.*;

public class BlogControler {

    private static BlogControler instance;

    private ArrayList<usuario> usuarios;
    private ArrayList<Articulo> articulos;

    private BlogControler() {
        usuarios = new ArrayList<>();
        articulos = new ArrayList<>();
    }

    public void actualizarArticulo(Articulo articuloActualizado) {
        for (int i = 0; i < articulos.size(); i++) {
            Articulo articulo = articulos.get(i);
            articuloActualizado.setListaComentarios(articulo.getListaComentarios());
            if (articulo.getId() == articuloActualizado.getId()) {
                articulos.set(i, articuloActualizado);
                break;
            }

        }
    }

    public void eliminarArticuloById(Long id) {
        Iterator<Articulo> iterator = articulos.iterator();
        while (iterator.hasNext()) {
            Articulo articulo = iterator.next();
            if (articulo.getId() == id) {
                iterator.remove();
            }
        }
    }

    public long crearIdArticulo() {
        long temp = 0;
        if (articulos.isEmpty()) {
            return temp;
        } else {
            for (Articulo i : articulos) {
                temp++;
            }
        }
        return temp;
    }

    public static BlogControler getInstance() {
        if (instance == null) {
            instance = new BlogControler();
        }
        return instance;
    }

    public void crearComentario(Long artId, comentario coment) {
        for (Articulo i : articulos) {
            if (artId == i.getId()) {
                i.agregarComentario(coment);
            }
        }
    }

    public ArrayList<Articulo> sortArticuloByFecha(ArrayList<Articulo> articulos) {
        Collections.sort(articulos, new Comparator<Articulo>() {
            public int compare(Articulo articulo1, Articulo articulo2) {
                Date fecha1 = articulo1.getFecha();
                Date fecha2 = articulo2.getFecha();
                return fecha2.compareTo(fecha1);
            }
        });
        return articulos;
    }

    public usuario autenticarUsuario(String username, String password) {
        for (usuario user : usuarios) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public void crearAdmin(){
        usuario admin = new usuario("admin", "admin", "admin",true, true);
        usuarios.add(admin);
    }

    public void crearPost(){
        usuario user = buscarUsuarioByUserName("admin");
        Articulo newArticulo = new Articulo(0,"Prueba","Hey chicos, sabian que en terminos de reproduccion entre hombres humanos y Pokemon hembras, Vaporeon es el Pokémon mas compatible para los Humanos?\n" +
                "\n" +
                "No solo porque estan en el Grupo Huevo Campo, que esta principalmente conformado por Mamiferos, Vaporeon tiene en promedio una medida de 91.44 Cm. de altura y un peso de 28,98 Kg., esto significa que son suficientemente grandes para soportar penes humanos, y con sus impresionantes Estadisticas Base de PS y acceso a Armadura Acida, puedes ser duro con ella. Debido a su biologia mayoritariamente compuesta de agua, no hay dudas de que una Vaporeon excitada sería increiblemente humeda, tan humeda que podrias facilmente tener sexo con una por horas sin lastimarte o sentir dolor.\n" +
                "\n" +
                "Ellas tambien pueden aprender los movimientos \"Atraccion\", \"Ojitos Tiernos\", \"Seduccion\", \"Encanto\" y \"Latigo\", además de no tener pelaje para esconder pezones, así que seria increiblemente facil conseguirte una con humor. Con sus habilidades \"Absorbe Agua\" e \"Hidratacion\", pueden recuperarse facilmente de la fatiga con suficiente agua.\n" +
                "\n" +
                "Ningun otro Pokemon llega a estar cerca de este nivel de compatibilidad. Ademas, como curiosidad, si te empenas suficiente al acabar, puedes llegar a hacer a tu Vaporeon Blanca.\n" +
                "\n" +
                "Vaporeon está literalmente hecha para el pene humano. Asombrosas Estadisticas de Defensa+Alta cantidad de PS+Armadura Acida significa que puede recibir verga todo el dia, de todas las formas y tamanos, y aun así venir por mas.",user);
        Etiqueta nuevaEtiquta = new Etiqueta(1,"Prueba");
        newArticulo.addEtiqueta(nuevaEtiquta);
        comentario newComentario = new comentario(0,"Prueba",user,newArticulo);
        newArticulo.addComentario(newComentario);
        articulos.add(newArticulo);
    }
    public Articulo buscarArticuloById(Long id) {
        for (Articulo articulo : articulos) {
            if (articulo.getId() == id) {
                return articulo;
            }
        }
        return null;
    }
    public ArrayList<Articulo> busarArticulosByuername(String username){
        ArrayList<Articulo> articulosByUser = new ArrayList<>();
        for (Articulo articulo: articulos) {
            if(articulo.getAutor().getUsername().equals(username)){
                articulosByUser.add(articulo);
            }
        }
        return articulosByUser;
    }
    public usuario buscarUsuarioByUserName(String username){
        for (usuario user: usuarios) {
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public void insertarUsuario(usuario user){
        usuarios.add(user);
    }
    public  void eliminarUsuario(usuario user){
        usuarios.remove(user);
    }
    public void insertarArticulo(Articulo articulo){
        articulos.add(articulo);
    }
    public void eliminarArticulo(Articulo articulo){
        articulos.remove(articulo);
    }
    public ArrayList<usuario> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(ArrayList<usuario> usuarios) {
        this.usuarios = usuarios;
    }
    public ArrayList<Articulo> getArticulos() {
        return articulos;
    }
    public void setArticulos(ArrayList<Articulo> articulos) {
        this.articulos = articulos;
    }






}
