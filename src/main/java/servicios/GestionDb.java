package servicios;
import  org.example.Main;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
public class GestionDb<T> {
    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;

    public GestionDb(Class<T> claseEntidad) {
        if (emf == null) {
            if (Main.getModoConexion().equalsIgnoreCase("Heroku")) {
                emf = getConfiguracionBaseDatosHeroku();
            }else {
                emf = Persistence.createEntityManagerFactory("MiUnidadPersistencia");
            }
        }
        this.claseEntidad = claseEntidad;
    }

    public EntityManager getEntityManager(){ return emf.createEntityManager(); }

    private EntityManagerFactory getConfiguracionBaseDatosHeroku(){
String databaseUrl = System.getenv("DATABASE_URL");
        StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
        String dbVendor = st.nextToken();
        String userName = st.nextToken();
        String password = st.nextToken();
        String host = st.nextToken();
        String port = st.nextToken();
        String databaseName = st.nextToken();
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s?sslmode=require", host, port, databaseName);
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", jdbcUrl);
        properties.put("javax.persistence.jdbc.user", userName);
        properties.put("javax.persistence.jdbc.password", password);
        properties.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return Persistence.createEntityManagerFactory("Heroku", properties);

    }

    private Object getValorCampo(T entidad){
        if(entidad==null){
            return null;
        }
        for(Field f:entidad.getClass().getDeclaredFields()){
            if(f.isAnnotationPresent(Id.class)){
                try {
              f.setAccessible(true);
              Object valorCampo = f.get(entidad);

              System.out.println("Nombre del campo: "+f.getName());
                System.out.println("Tipo del campo: "+f.getType().getName());
                System.out.println("Valor del campo: "+valorCampo);
                return valorCampo;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public T crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();
        }finally {
            em.close();
        }return entidad;
    }
    public T editar(T entidad) throws PersistenceException{
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try{

            em.merge(entidad);
            em.getTransaction().commit();
        }finally {
            em.close();
        }return entidad;
    }
    public boolean eliminar(Object entidadId)throws PersistenceException{
        boolean ok= false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try{
            T entidad = em.find(claseEntidad,entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }
    public T find(Object id){
        EntityManager em = getEntityManager();
        try {
            return em.find(claseEntidad, id);
        } finally {
            em.close();
        }
    }
    public List<T> findAll() throws PersistenceException{
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> cirteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            cirteriaQuery.select(cirteriaQuery.from(claseEntidad));
            return em.createQuery(cirteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }

}
