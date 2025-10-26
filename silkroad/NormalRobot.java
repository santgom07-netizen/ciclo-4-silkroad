package silkroad; 

/**
 * Robot normal. Toma todo el dinero disponible y puede volver a su posicion inicial.
 * Comportamiento estandar sin restricciones especiales.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class NormalRobot extends Robot {
    
    /**
     * Crea un robot normal.
     * @param location Posicion inicial del robot
     * @param color Color del robot
     * @param x Coordenada X visual
     * @param y Coordenada Y visual
     */
    public NormalRobot(int location, String color, int x, int y) {
        super(location, color, x, y);
    }
    
    /**
     * Constructor de compatibilidad con ciclos anteriores.
     * @param location Posicion inicial del robot
     */
    public NormalRobot(int location) {
        this(location, "blue", 50 + location * 40, 130);
    }
    
    @Override
    public int collectFromStore(Store store) {
        return store.takeTenges(store.getTenges(), this);
    }
    
    @Override
    public boolean canReturnToInitial() {
        return true;
    }
    
    @Override
    public String getType() {
        return "normal";
    }
}
