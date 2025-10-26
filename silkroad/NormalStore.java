package silkroad; 

/**
 * Tienda normal. Cualquier robot puede tomar su dinero sin restricciones.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class NormalStore extends Store {
    
    /**
     * Crea una tienda normal.
     * @param location Posicion de la tienda en la ruta
     * @param tenges Cantidad inicial de dinero
     * @param color Color de la tienda para visualizacion
     * @param x Coordenada X para posicionamiento visual
     * @param y Coordenada Y para posicionamiento visual
     */
    public NormalStore(int location, int tenges, String color, int x, int y) {
        super(location, tenges, color, x, y);
    }
    
    /**
     * Constructor de compatibilidad con ciclos anteriores.
     * @param location Posicion de la tienda
     * @param tenges Cantidad inicial de dinero
     */
    public NormalStore(int location, int tenges) {
        this(location, tenges, "green", 50 + location * 40, 100);
    }
    
    @Override
    public boolean canRobotTake(Robot robot) {
        return true;
    }
    
    @Override
    public String getType() {
        return "normal";
    }
}
