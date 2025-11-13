package silkroad; 
import java.util.*;

/**
 * Gestor de colores únicos para tiendas y robots.
 * SECUENCIA LINEAL - Sin repeticiones hasta completar ciclo.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class ColorManager {
    // Colores que Canvas soporta - SECUENCIA PARA TIENDAS
    private static final String[] STORE_COLORS = {
        "red", "green", "blue", "yellow", "magenta", "cyan", "orange",
        "pink", "purple", "brown", "gray", "lightGray", "darkGray"
    };
    
    // Colores que Canvas soporta - SECUENCIA PARA ROBOTS (ORDEN DIFERENTE)
    private static final String[] ROBOT_COLORS = {
        "blue", "purple", "cyan", "darkGray", "red", "yellow", "pink",
        "magenta", "lightGray", "green", "brown", "orange", "gray"
    };
    
    private int storeIndex = 0;
    private int robotIndex = 0;
    
    /**
     * Constructor público del gestor de colores.
     */
    public ColorManager() {
        this.storeIndex = 0;
        this.robotIndex = 0;
    }
    
    /**
     * Obtiene un color único para una tienda.
     * Cicla por secuencia lineal: 0,1,2,3,...,12,0,1,2...
     * @return Color único que Canvas soporta
     */
    public String getUniqueStoreColor() {
        String color = STORE_COLORS[storeIndex % STORE_COLORS.length];
        storeIndex++;
        return color;
    }
    
    /**
     * Obtiene un color único para un robot.
     * Cicla por secuencia DIFERENTE: para no sincronizar con tiendas.
     * @return Color único que Canvas soporta
     */
    public String getUniqueRobotColor() {
        String color = ROBOT_COLORS[robotIndex % ROBOT_COLORS.length];
        robotIndex++;
        return color;
    }
    
    /**
     * Reinicia todos los colores disponibles.
     */
    public void reset() {
        this.storeIndex = 0;
        this.robotIndex = 0;
    }
    
    /**
     * Obtiene el número de colores de tienda disponibles.
     * @return Número de colores disponibles para tiendas
     */
    public int getAvailableStoreColors() {
        return STORE_COLORS.length;
    }
    
    /**
     * Obtiene el número de colores de robot disponibles.
     * @return Número de colores disponibles para robots
     */
    public int getAvailableRobotColors() {
        return ROBOT_COLORS.length;
    }
    
    /**
     * Obtiene el número total de colores únicos para tiendas.
     * @return Total de colores disponibles para tiendas
     */
    public int getTotalStoreColors() {
        return STORE_COLORS.length;
    }
    
    /**
     * Obtiene el número total de colores únicos para robots.
     * @return Total de colores disponibles para robots
     */
    public int getTotalRobotColors() {
        return ROBOT_COLORS.length;
    }
}