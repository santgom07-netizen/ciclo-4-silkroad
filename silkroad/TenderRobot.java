package silkroad; 

/**
 * Robot tierno. Solo toma la mitad del dinero disponible en las tiendas.
 * Comportamiento considerado y moderado en la recoleccion.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class TenderRobot extends Robot {
    
    /**
     * Crea un robot tierno.
     * @param location Posicion inicial del robot
     * @param color Color del robot
     * @param x Coordenada X visual
     * @param y Coordenada Y visual
     */
    public TenderRobot(int location, String color, int x, int y) {
        super(location, color, x, y);
    }
    
    @Override
    public int collectFromStore(Store store) {
        int available = store.getTenges();
        int halfAmount = available / 2;
        return store.takeTenges(halfAmount, this);
    }
    
    @Override
    public boolean canReturnToInitial() {
        return true;
    }
    
    @Override
    public String getType() {
        return "tender";
    }
}
