package silkroad;  

import shapes.Rectangle;  

/**
 * Clase para manejar la barra de progreso de ganancias.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class ProgressBar {
    private Rectangle background;
    private Rectangle fill;
    private int maximum;
    private int current;
    private boolean visible;
    
    /**
     * Constructor que crea la barra de progreso.
     */
    public ProgressBar() {
        this.maximum = 100;
        this.current = 0;
        this.visible = false;
        
        background = new Rectangle();
        background.changeColor("gray");
        background.changeSize(400, 20);
        background.moveHorizontal(50);
        background.moveVertical(20);
    
        fill = new Rectangle();
        fill.changeColor("green");
        fill.changeSize(1, 20);
        fill.moveHorizontal(50);
        fill.moveVertical(20);
        
        background.makeInvisible();
        fill.makeInvisible();
    }
    
    /**
     * Establece el valor maximo de la barra.
     * @param max Valor maximo
     */
    public void setMaximum(int max) {
        this.maximum = Math.max(1, max);
        updateVisual();
    }
    
    /**
     * Actualiza el progreso actual de la barra.
     * @param value Valor actual
     */
    public void updateProgress(int value) {
        this.current = Math.max(0, Math.min(value, maximum));
        updateVisual();
    }
    
    /**
     * Actualiza la visualizacion de la barra.
     */
    private void updateVisual() {
        if (visible && maximum > 0) {
            int fillWidth = Math.max(1, (current * 400) / maximum);
            fill.changeSize(fillWidth, 20);
        }
    }
    
    /**
     * Hace visible la barra de progreso.
     */
    public void makeVisible() {
        visible = true;
        background.makeVisible();
        fill.makeVisible();
        updateVisual();
    }
    
    /**
     * Hace invisible la barra de progreso.
     */
    public void makeInvisible() {
        visible = false;
        background.makeInvisible();
        fill.makeInvisible();
    }
    
    /**
     * Finaliza la barra de progreso.
     */
    public void finish() {
        makeInvisible();
    }
}
