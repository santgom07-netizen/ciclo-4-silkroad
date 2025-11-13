package shapes;
import java.awt.geom.*;

/**
 * Un círculo que puede manipularse y dibujarse a sí mismo en un lienzo.
 * Versión extendida con métodos adicionales para obtener la posición.
 * 
 * @author  
 * Michael Kolling y David J. Barnes (Modificado)
 * @version 1.1  (Extendido para el proyecto SilkRoad)
 */
public class Circle {
    public static final double PI = 3.1416;
    
    protected int diameter;
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;
    
    /**
     * Crea un nuevo círculo en una posición por defecto y con color por defecto.
     */
    public Circle() {
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }

    /**
     * Obtiene la posición X actual del círculo.
     * @return posición X actual
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Obtiene la posición Y actual del círculo.
     * @return posición Y actual
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Establece directamente la posición (método protegido para subclases).
     * @param x nueva posición X
     * @param y nueva posición Y
     */
    protected void setPosition(int x, int y) {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }
       
    /**
     * Hace visible el círculo.  
     * Si ya era visible, no hace nada.
     */
    public void makeVisible() {
        isVisible = true;
        draw();
    }
    
    /**
     * Hace invisible el círculo.  
     * Si ya era invisible, no hace nada.
     */
    public void makeInvisible() {
        erase();
        isVisible = false;
    }
    
    /*
     * Dibuja el círculo con las especificaciones actuales en pantalla.
     */
    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition, yPosition, 
                diameter, diameter));
            canvas.wait(10);
        }
    }
    
    /*
     * Borra el círculo de la pantalla.
     */
    private void erase() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
    
    /**
     * Mueve el círculo unos pocos píxeles a la derecha.
     */
    public void moveRight() {
        moveHorizontal(20);
    }
    
    /**
     * Mueve el círculo unos pocos píxeles a la izquierda.
     */
    public void moveLeft() {
        moveHorizontal(-20);
    }
    
    /**
     * Mueve el círculo unos pocos píxeles hacia arriba.
     */
    public void moveUp() {
        moveVertical(-20);
    }
    
    /**
     * Mueve el círculo unos pocos píxeles hacia abajo.
     */
    public void moveDown() {
        moveVertical(20);
    }
    
    /**
     * Mueve el círculo horizontalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }
    
    /**
     * Mueve el círculo verticalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }
    
    public void slowMoveHorizontal(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        int steps = Math.abs(distance);
        for(int i = 0; i < steps; i++) {
            xPosition += delta;
            draw();
        }
    }

    /**	
     * Mueve lentamente el círculo verticalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void slowMoveVertical(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        int steps = Math.abs(distance);
        for(int i = 0; i < steps; i++) {
            yPosition += delta;
            draw();
        }
    }
    
    /**
     * Cambia el tamaño del círculo.
     * @param newDiameter el nuevo tamaño (en píxeles). 
     *        El tamaño debe ser >= 0.
     */
    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }
    
    /**
     * Cambia el color del círculo.  
     * @param newColor el nuevo color. Los colores válidos son 
     *        "red", "yellow", "blue", "green", "magenta" y "black".
     */
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }
}
