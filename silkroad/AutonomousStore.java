package silkroad; 

import java.util.Random;

/**
 * Tienda autonoma. Escoge su propia posicion, no la que le indican.
 * Se ubica cerca de la posicion solicitada pero con cierta autonomia.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class AutonomousStore extends Store {
    private static Random random = new Random();
    private int requestedLocation;
    
    /**
     * Crea una tienda autonoma que elige su propia ubicacion.
     * @param requestedLocation Ubicacion solicitada (sera modificada autonomamente)
     * @param tenges Cantidad inicial de dinero
     * @param color Color de la tienda
     * @param roadLength Longitud total de la ruta para elegir posicion valida
     */
    public AutonomousStore(int requestedLocation, int tenges, String color, int roadLength) {
        super(selectAutonomousLocation(requestedLocation, roadLength), tenges, color, 0, 0);
        this.requestedLocation = requestedLocation;
    }
    
    /**
     * Selecciona una ubicacion autonoma cerca de la solicitada.
     * @param requested Ubicacion solicitada
     * @param roadLength Longitud de la ruta
     * @return Ubicacion seleccionada autonomamente
     */
    private static int selectAutonomousLocation(int requested, int roadLength) {
        int offset = random.nextInt(11) - 5;
        int selected = requested + offset;
        return Math.max(0, Math.min(selected, roadLength - 1));
    }
    
    /**
     * Obtiene la ubicacion originalmente solicitada.
     * @return Ubicacion solicitada
     */
    public int getRequestedLocation() {
        return requestedLocation;
    }
    
    @Override
    public boolean canRobotTake(Robot robot) {
        return true;
    }
    
    @Override
    public String getType() {
        return "autonomous";
    }
}
