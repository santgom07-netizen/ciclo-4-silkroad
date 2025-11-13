package silkroad; 

import shapes.Rectangle;  
import java.awt.*;

/**
 * Clase abstracta base para todas las tiendas en la Ruta de la Seda.
 * Define comportamiento comun y permite extensibilidad mediante herencia.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public abstract class Store {
    protected Rectangle visualRepresentation;
    protected int location;
    protected int tenges;
    protected int initialTenges;
    protected int timesEmptied;
    protected boolean isEmpty;
    protected String originalColor;
    
    /**
     * Constructor protegido para subclases.
     * @param location Posicion en la ruta (0 <= location < length)
     * @param tenges Cantidad inicial de dinero (>= 0)
     * @param color Color de la tienda para visualizacion
     * @param x Coordenada X visual en pixels
     * @param y Coordenada Y visual en pixels
     */
    protected Store(int location, int tenges, String color, int x, int y) {
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        this.timesEmptied = 0;
        this.isEmpty = (tenges == 0);
        this.originalColor = color;
        
        this.visualRepresentation = new Rectangle();
        visualRepresentation.changeColor(isEmpty ? "gray" : color);
        visualRepresentation.changeSize(30, 30);
        visualRepresentation.setPosition(x, y);
        visualRepresentation.makeVisible();
    }
    
    /**
     * Obtiene la ubicacion de la tienda en la ruta.
     * @return Ubicacion (posicion en la ruta)
     */
    public int getLocation() {
        return location;
    }
    
    /**
     * Obtiene el dinero actual disponible en la tienda.
     * @return Cantidad de tenges disponibles
     */
    public int getTenges() {
        return tenges;
    }
    
    /**
     * Obtiene la cantidad inicial de dinero que tenia la tienda.
     * @return Tenges iniciales
     */
    public int getInitialTenges() {
        return initialTenges;
    }
    
    /**
     * Obtiene el numero de veces que la tienda ha sido vaciada.
     * @return Veces que la tienda fue vaciada completamente
     */
    public int getTimesEmptied() {
        return timesEmptied;
    }
    
    /**
     * Metodo abstracto que determina si un robot puede tomar dinero.
     * Cada tipo de tienda implementa su propia logica de acceso.
     * @param robot Robot que intenta tomar dinero
     * @return true si el robot puede tomar dinero, false en caso contrario
     */
    public abstract boolean canRobotTake(Robot robot);
    
    /**
     * Toma dinero de la tienda cuando un robot la visita.
     * Actualiza automaticamente el estado visual (color gris si se vacía).
     * @param amount Cantidad de dinero solicitado
     * @param robot Robot que toma el dinero
     * @return Cantidad realmente tomada (puede ser menor que lo solicitado)
     */
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
        
        return taken;
    }
    
    /**
     * Reabastece la tienda a su cantidad inicial de dinero.
     * Restaura el color original si la tienda estaba vacia.
     */
    public void resupply() {
        this.tenges = initialTenges;
        if (tenges > 0) {
            isEmpty = false;
            visualRepresentation.changeColor(originalColor);
        }
    }
    
    /**
     * Reinicia la tienda a su estado inicial.
     * Reabastece dinero y reinicia contador de veces vaciada.
     */
    public void reset() {
        resupply();
        timesEmptied = 0;
    }
    
    /**
     * Obtiene el color original asignado a la tienda.
     * @return Color original de la tienda
     */
    public String getColor() {
        return originalColor;
    }
    
    /**
     * Obtiene el tipo de tienda como String.
     * @return Tipo de tienda ("normal", "autonomous", "fighter", "generous", etc.)
     */
    public abstract String getType();
    
    /**
     * Hace visible la tienda en el canvas.
     */
    public void makeVisible() {
        visualRepresentation.makeVisible();
    }
    
    /**
     * Hace invisible la tienda en el canvas.
     */
    public void makeInvisible() {
        visualRepresentation.makeInvisible();
    }
}
