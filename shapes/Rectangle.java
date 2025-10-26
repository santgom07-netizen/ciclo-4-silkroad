package shapes;
import java.awt.*;

/**
 * Un rectángulo que puede manipularse y dibujarse a sí mismo en un lienzo.
 * Versión extendida con métodos adicionales para obtener la posición.
 * 
 * @author  
 * Michael Kolling y David J. Barnes (Modificado)
 * @version 1.1  (Extendido para el proyecto SilkRoad)
 */
public class Rectangle{

    public static int EDGES = 4;
    
    protected int height;
    protected int width;
    protected int xPosition;
    protected int yPosition;
    protected String color;
    protected boolean isVisible;

    /**
     * Crea un nuevo rectángulo en posición por defecto con color por defecto.
     */
    public Rectangle(){
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
    }

    /**
     * Obtiene la posición X actual del rectángulo.
     * @return posición X actual
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Obtiene la posición Y actual del rectángulo.
     * @return posición Y actual
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Establece la posición directamente.
     * @param x Nueva posición X
     * @param y Nueva posición Y
     */
    public void setPosition(int x, int y) {
        erase();
        xPosition = x;
        yPosition = y;
        draw();
    }

    /**
     * Hace visible este rectángulo. Si ya es visible, no hace nada.
     */
    public void makeVisible(){
        isVisible = true;
        draw();
    }
    
    /**
     * Hace invisible este rectángulo. Si ya es invisible, no hace nada.
     */
    public void makeInvisible(){
        erase();
        isVisible = false;
    }
    
    /**
     * Mueve el rectángulo unos pocos píxeles a la derecha.
     */
    public void moveRight(){
        moveHorizontal(20);
    }

    /**
     * Mueve el rectángulo unos pocos píxeles a la izquierda.
     */
    public void moveLeft(){
        moveHorizontal(-20);
    }

    /**
     * Mueve el rectángulo unos pocos píxeles hacia arriba.
     */
    public void moveUp(){
        moveVertical(-20);
    }

    /**
     * Mueve el rectángulo unos pocos píxeles hacia abajo.
     */
    public void moveDown(){
        moveVertical(20);
    }

    /**
     * Mueve el rectángulo horizontalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void moveHorizontal(int distance){
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve el rectángulo verticalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void moveVertical(int distance){
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve lentamente el rectángulo horizontalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void slowMoveHorizontal(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            xPosition += delta;
            draw();
        }
    }

    /**
     * Mueve lentamente el rectángulo verticalmente.
     * @param distance la distancia deseada en píxeles
     */
    public void slowMoveVertical(int distance){
        int delta;

        if(distance < 0) {
            delta = -1;
            distance = -distance;
        } else {
            delta = 1;
        }

        for(int i = 0; i < distance; i++){
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el tamaño al nuevo valor.
     * @param newHeight la nueva altura en píxeles. newHeight debe ser >=0.
     * @param newWidth el nuevo ancho en píxeles. newWidth debe ser >=0.
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
    
    /**
     * Cambia el color.
     * @param newColor el nuevo color. Los colores válidos son "red", "yellow", "blue", "green",
     * "magenta" y "black".
     */
    public void changeColor(String newColor){
        color = newColor;
        draw();
    }

    /*
     * Dibuja el rectángulo con las especificaciones actuales en pantalla.
     */
    private void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, 
                                       width, height));
            canvas.wait(10);
        }
    }

    /*
     * Borra el rectángulo de la pantalla.
     */
    private void erase(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
