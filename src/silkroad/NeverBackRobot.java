package silkroad; 

/**
 * Robot que nunca vuelve a su posicion inicial.
 * Una vez que se mueve, permanece en su nueva ubicacion.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class NeverBackRobot extends Robot {
    
    /**
     * Crea un robot que nunca vuelve.
     * @param location Posicion inicial del robot
     * @param color Color del robot
     * @param x Coordenada X visual
     * @param y Coordenada Y visual
     */
    public NeverBackRobot(int location, String color, int x, int y) {
        super(location, color, x, y);
    }
    
    @Override
    public int collectFromStore(Store store) {
        return store.takeTenges(store.getTenges(), this);
    }
    
    @Override
    public boolean canReturnToInitial() {
        return false;
    }
    
    @Override
    public String getType() {
        return "neverback";
    }
}
