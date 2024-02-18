package servicios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.models.comentario;

import java.util.List;

public class comentarioServices extends GestionDb<comentario>{

    public static comentarioServices instancia;

    private comentarioServices() {
        super(comentario.class);
    }

    public static comentarioServices getInstance() {
        if (instancia == null) {
            instancia = new comentarioServices();
        }
        return instancia;
    }


    public void addComentario(comentario comentario) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(comentario);
        em.getTransaction().commit();
    }

    public void modificarComentario(comentario comentario) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(comentario);
        em.getTransaction().commit();
    }

    public void eliminarComentario(comentario comentario) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(comentario);
        em.getTransaction().commit();
    }

    public comentario getComentarioById(int id) {
        EntityManager em = getEntityManager();
        return em.find(comentario.class, id);
    }

    public List<comentario> getComentarios() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select c from comentario c");
        return query.getResultList();
    }

    public List<comentario> getComentariosByArticulo(int idArticulo) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select c from comentario c where c.articulo.id = :idArticulo");
        query.setParameter("idArticulo", idArticulo);
        return query.getResultList();
    }

    public List<comentario> getComentariosByUsuario(String usuario) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select c from comentario c where c.autor = :usuario");
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }
}
