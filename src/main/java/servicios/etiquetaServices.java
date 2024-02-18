package servicios;

import jakarta.persistence.EntityManager;
import org.example.models.Etiqueta;

public class etiquetaServices extends  GestionDb<Etiqueta>{
    public static etiquetaServices instancia;

    private etiquetaServices() {
        super(Etiqueta.class);
    }

    public static etiquetaServices getInstance() {
        if (instancia == null) {
            instancia = new etiquetaServices();
        }
        return instancia;
    }

    public void addEtiqueta(Etiqueta etiqueta) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(etiqueta);
        em.getTransaction().commit();
    }

    public void modificarEtiqueta(Etiqueta etiqueta) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(etiqueta);
        em.getTransaction().commit();
    }

    public void eliminarEtiqueta(Etiqueta etiqueta) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(etiqueta);
        em.getTransaction().commit();
    }

    public Etiqueta getEtiquetaById(int id) {
        EntityManager em = getEntityManager();
        return em.find(Etiqueta.class, id);
    }

}
