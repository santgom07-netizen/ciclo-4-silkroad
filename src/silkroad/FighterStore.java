package silkroad; 

/**
 * Tienda luchadora. Solo robots con mas dinero que ella pueden tomar su dinero.
 * Defiende su dinero contra robots pobres.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class FighterStore extends Store {
    
    /**
     * Crea una tienda luchadora.
     * @param location Posicion de la tienda en la ruta
     * @param tenges Cantidad inicial de dinero
     * @param color Color de la tienda
     * @param x Coordenada X visual
     * @param y Coordenada Y visual
     */
    public FighterStore(int location, int tenges, String color, int x, int y) {
        super(location, tenges, color, x, y);
    }
    
    @Override
    public boolean canRobotTake(Robot robot) {
        return robot.getTotalGain() > this.tenges;
    }
    
    @Override
    public String getType() {
        return "fighter";
    }
}
