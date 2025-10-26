package silkroad; 

/**
 * Tienda generosa. Da el doble de dinero a los robots que la visitan.
 * Tipo propuesto adicional para demostrar extensibilidad.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class GenerousStore extends Store {
    
    /**
     * Crea una tienda generosa.
     * @param location Posicion de la tienda en la ruta
     * @param tenges Cantidad inicial de dinero
     * @param color Color de la tienda
     * @param x Coordenada X visual
     * @param y Coordenada Y visual
     */
    public GenerousStore(int location, int tenges, String color, int x, int y) {
        super(location, tenges, color, x, y);
    }
    
    @Override
    public boolean canRobotTake(Robot robot) {
        return true;
    }
    
    @Override
    public int takeTenges(int amount, Robot robot) {
        if (!canRobotTake(robot)) {
            return 0;
        }
        
        int taken = Math.min(amount, tenges);
        tenges -= taken;
        
        if (tenges == 0 && taken > 0) {
            timesEmptied++;
            isEmpty = true;
            visualRepresentation.changeColor("gray");
        }
        
        return taken * 2;
    }
    
    @Override
    public String getType() {
        return "generous";
    }
}
