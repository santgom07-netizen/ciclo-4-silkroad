/**
 * Clase Robot en la Ruta de la Seda.
 * Un robot está representado como un círculo en el simulador visual.
 */
public class Robot extends Circle {
    private int location;
    private int initialLocation;

    /**
     * Crea un robot en una posición dada con color y coordenadas específicas.
     * @param location Posición inicial del robot
     * @param color Color del robot
     * @param x Coordenada X para posicionamiento visual
     * @param y Coordenada Y para posicionamiento visual
     */
    public Robot(int location, String color, int x, int y) {
        super();
        this.location = location;
        this.initialLocation = location;
        
        // Configurar apariencia visual
        changeColor(color);
        changeSize(20);
        setPosition(x + 5, y + 5); // Offset para centrarlo en la celda
        makeVisible();
    }

    /**
     * Crea un robot con posicionamiento por defecto (para compatibilidad).
     * @param location Posición inicial del robot
     */
    public Robot(int location) {
        this(location, "blue", 50 + location * 40, 130);
    }

    /**
     * @return Ubicación actual del robot
     */
    public int getLocation() {
        return location;
    }

    /**
     * @return Ubicación inicial del robot
     */
    public int getInitialLocation() {
        return initialLocation;
    }

    /**
     * Mueve el robot a una nueva ubicación con coordenadas específicas.
     * @param newLocation Nueva posición lógica
     * @param x Nueva coordenada X
     * @param y Nueva coordenada Y
     */
    public void moveTo(int newLocation, int x, int y) {
        setPosition(x + 5, y + 5); // Offset para centrarlo
        this.location = newLocation;
    }

    /**
     * Mueve el robot cierta cantidad de metros (método heredado).
     * @param meters Pasos a mover (positivos o negativos)
     * @param roadLength Longitud de la Ruta de la Seda
     */
    public void move(int meters, int roadLength) {
        int newPos = location + meters;
        if (newPos >= 0 && newPos < roadLength) {
            moveHorizontal(meters * 40); // Usar nueva escala visual
            location = newPos;
        }
    }

    /**
     * Devuelve el robot a su posición inicial con coordenadas específicas.
     * @param x Coordenada X de la posición inicial
     * @param y Coordenada Y de la posición inicial
     */
    public void returnToInitialPosition(int x, int y) {
        moveTo(initialLocation, x, y);
    }

    /**
     * Reinicia el robot a su posición inicial (método heredado).
     */
    public void resetPosition() {
        int displacement = (initialLocation - location) * 40;
        moveHorizontal(displacement);
        location = initialLocation;
    }
}
