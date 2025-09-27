/**
 * Clase Store en la Ruta de la Seda.
 * Una tienda está representada como un rectángulo en el simulador visual.
 */
public class Store extends Rectangle {
    private int location;
    private int tenges;
    private int initialTenges;

    /**
     * Crea una tienda en una ubicación dada con dinero inicial.
     * @param location Posición de la tienda en la Ruta de la Seda
     * @param tenges Cantidad inicial de dinero en la tienda
     * @param color Color de la tienda
     * @param x Coordenada X para posicionamiento visual
     * @param y Coordenada Y para posicionamiento visual
     */
    public Store(int location, int tenges, String color, int x, int y) {
        super();
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        
        // Configurar apariencia visual
        changeColor(color);
        changeSize(30, 30);
        setPosition(x, y);
        makeVisible();
    }

    /**
     * Crea una tienda con posicionamiento por defecto (para compatibilidad).
     * @param location Posición de la tienda en la Ruta de la Seda
     * @param tenges Cantidad inicial de dinero en la tienda
     */
    public Store(int location, int tenges) {
        this(location, tenges, "green", 50 + location * 40, 100);
    }

    /**
     * @return Ubicación de la tienda
     */
    public int getLocation() {
        return location;
    }

    /**
     * @return Dinero actual en la tienda
     */
    public int getTenges() {
        return tenges;
    }

    /**
     * @return Cantidad inicial de dinero
     */
    public int getInitialTenges() {
        return initialTenges;
    }

    /**
     * Disminuye el dinero de la tienda.
     * @param amount Cantidad a disminuir
     */
    public void decreaseTenges(int amount) {
        if (tenges >= amount) {
            tenges -= amount;
        }
    }

    /**
     * Reabastece la tienda a su dinero inicial.
     */
    public void resupply() {
        this.tenges = initialTenges;
    }

    /**
     * Reinicia la tienda (igual que reabastecer).
     */
    public void reset() {
        resupply();
    }

    /**
     * Mueve la tienda a nuevas coordenadas.
     * @param x Nueva coordenada X
     * @param y Nueva coordenada Y
     */
    public void moveTo(int x, int y) {
        setPosition(x, y);
    }
}
