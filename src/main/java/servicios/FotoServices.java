package servicios;
import org.example.models.Fotos;
import org.example.models.usuario;

public class FotoServices extends GestionDb<Fotos>{

    private static FotoServices instancia;

    private FotoServices(){
        super(Fotos.class);
    }
    public static FotoServices getInstance(){
        if(instancia==null){
            instancia = new FotoServices();
        }
        return instancia;
    }
}
