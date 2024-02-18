package servicios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import org.example.models.Articulo;
import org.example.models.Etiqueta;
import org.example.models.comentario;
import org.example.models.usuario;
import org.h2.engine.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ArticuloServices extends GestionDb<Articulo> {
    private static ArticuloServices instancia;

    private ArticuloServices() {
        super(Articulo.class);
    }

    public static ArticuloServices getInstance() {
        if (instancia == null) {
            instancia = new ArticuloServices();
        }
        return instancia;
    }

    public void addArticulo(Articulo articulo, usuario autor) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(autor);
        articulo.setAutor(autor);
        em.persist(articulo);
        em.getTransaction().commit();

    }

    public void modificarArticulo(Articulo articulo) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Articulo articuloExistente = em.find(Articulo.class, articulo.getId());
        if (articuloExistente != null) {
            articuloExistente.setTitulo(articulo.getTitulo());
            articuloExistente.setCuerpo(articulo.getCuerpo());
            // establecer las demás propiedades que quieras actualizar...
            em.merge(articuloExistente);
        }
        em.getTransaction().commit();
    }


    public void eliminarArticulo(Articulo articulo) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(articulo);
        em.getTransaction().commit();
    }

    public Articulo getArticuloById(int id) {
        EntityManager em = getEntityManager();
        Articulo articulo = em.find(Articulo.class, id);
        if (articulo == null) {
            throw new IllegalArgumentException("No se encontró ningún Articulo con el id: " + id);
        }
        return articulo;
    }


    public List<Articulo> getArticulos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a");
        List<Articulo> articulos = query.getResultList();
        if (articulos.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron Articulos en la base de datos");
        }
        return articulos;
    }


    public void agregarEtiqueta(int idArticulo, String etiqueta) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Articulo articulo = em.find(Articulo.class, idArticulo);
            if (articulo != null) {
                articulo.addEtiqueta(new Etiqueta(etiqueta)); // Asumiendo que hay un constructor en Etiqueta que toma una cadena
                em.merge(articulo); // Usar merge para asociar la etiqueta persistente al artículo
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // O manejar la excepción de acuerdo a tus necesidades
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }


    public void eliminarEtiqueta(int idArticulo, String etiqueta) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Articulo articulo = em.find(Articulo.class, idArticulo);
            articulo.getListaEtiquetas().remove(etiqueta);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Articulo> getArticulosByEtiqueta(String etiqueta) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Articulo a WHERE :etiqueta MEMBER OF a.listaEtiquetas", Articulo.class);
            query.setParameter("etiqueta", etiqueta);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void getArticulosByTitulo(String titulo) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a where a.titulo like :titulo");
        query.setParameter("titulo", titulo + "%");
        query.getResultList();
    }

    public List<Articulo> getArticulosByAutor(String autorNombre) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Articulo a WHERE a.autor.nombre LIKE :autorNombre", Articulo.class);
            query.setParameter("autorNombre", autorNombre + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public void getArticulosByFecha(String fecha) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Articulo a WHERE a.fecha BETWEEN :fechaStart AND :fechaEnd", Articulo.class);
            // Assuming fecha is a String, you need to convert it to a Date object before setting parameters.
            // Date fechaStart and fechaEnd should be the start and end dates of the specified day.
            query.setParameter("fechaStart", fecha);
            query.setParameter("fechaEnd", fecha);
            query.getResultList();
        } finally {
            em.close();
        }
    }

    public void agregarComentario(int idArticulo, String comentarioTexto) {
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Articulo articulo = em.find(Articulo.class, idArticulo);
            if (articulo != null) {
                comentario comentario = new comentario(); // Asegúrate de crear una instancia de Comentario si no lo has hecho
                comentario.getComentario(comentarioTexto);
                articulo.agregarComentario(comentario);
                em.merge(articulo);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace(); // O manejar la excepción según tus necesidades
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void eliminarComentario(int idArticulo, String comentario) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Articulo articulo = em.find(Articulo.class, idArticulo);
        articulo.getListaComentarios().remove(comentario);
        em.getTransaction().commit();
    }
}


