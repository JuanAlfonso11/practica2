package servicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.jasypt.util.text.StrongTextEncryptor;
import org.example.models.usuario;
import org.example.models.Articulo;
import org.example.models.Fotos;
import java.util.ArrayList;


public class usuarioServices extends GestionDb<usuario> {
    private static usuarioServices instancia;

    private usuarioServices(){
        super(usuario.class);
    }

    public static usuarioServices getInstance(){
        if(instancia==null){
            instancia = new usuarioServices();
        }
        return instancia;
    }

    public void addUsuario(usuario usuario){
        EntityManager em = getEntityManager();
        String plainPassword = usuario.getPassword();

        StrongTextEncryptor encriptador = new StrongTextEncryptor();
        encriptador.setPassword("PWeb");
        String encryptedPassword = encriptador.encrypt(plainPassword);
        usuario.setPassword(encryptedPassword);
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
    }

    public usuario getUsuarioByUsuario(String usuario){
        EntityManager em = getEntityManager();
        usuario user = null;
        try {
            user = em.find(usuario.class, usuario);
        }
        catch (Exception e){
            user = null;
        }
        return user;
    }

    public ArrayList<usuario> getUsuarios(){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from usuario u");
        return (ArrayList<usuario>) query.getResultList();
    }

    public void modificarUsuario(usuario usuario) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
    }

    public void eliminarUsuario(usuario usuario) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(usuario);
        em.getTransaction().commit();
    }
    public  void agregarFotos(int idUsuario, String foto){
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        usuario usuario = em.find(usuario.class, idUsuario);
        usuario.setFoto(foto);
        em.getTransaction().commit();
    }
}
