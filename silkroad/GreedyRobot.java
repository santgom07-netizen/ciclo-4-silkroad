package silkroad;

/**
 * Robot codicioso. Intenta tomar el doble del dinero disponible en las tiendas.
 * Tipo propuesto adicional para demostrar extensibilidad.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class GreedyRobot extends Robot {
    
    /**
     * Crea un robot codicioso.
     * @param location Posicion inicial del robot
     * @param color Color del robot
     * @param x Coordenada X visual
     * @param y Coordenada Y visual
     */
    public GreedyRobot(int location, String color, int x, int y) {
        super(location, color, x, y);
    }
    
    @Override
    public int collectFromStore(Store store) {
        int available = store.getTenges();
        int doubleAmount = available * 2;
        return store.takeTenges(doubleAmount, this);
    }
    
    @Override
    public boolean canReturnToInitial() {
        return true;
    }
    
    @Override
    public String getType() {
        return "greedy";
    }
}
