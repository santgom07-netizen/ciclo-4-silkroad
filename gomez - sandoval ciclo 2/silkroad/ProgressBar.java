/**
 * Clase para manejar la barra de progreso de ganancias.
 * 
 * @autor Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class ProgressBar {
    private Rectangle background;
    private Rectangle fill;
    private int maximum;
    private int current;
    private boolean visible;
    
    public ProgressBar() {
        this.maximum = 100;
        this.current = 0;
        this.visible = false;
        
        // Barra de progreso horizontal en la parte superior
        background = new Rectangle();
        background.changeColor("gray");
        background.changeSize(400, 20);
        background.moveHorizontal(50);
        background.moveVertical(20);
        
        // Relleno que crece horizontalmente
        fill = new Rectangle();
        fill.changeColor("green");
        fill.changeSize(1, 20);
        fill.moveHorizontal(50);
        fill.moveVertical(20);
        
        if (!visible) {
            background.makeInvisible();
            fill.makeInvisible();
        }
    }
    
    public void setMaximum(int max) {
        this.maximum = Math.max(1, max);
        updateVisual();
    }
    
    public void updateProgress(int value) {
        this.current = Math.max(0, Math.min(value, maximum));
        updateVisual();
    }
    
    private void updateVisual() {
        if (visible && maximum > 0) {
            int fillWidth = Math.max(1, (current * 400) / maximum);
            fill.changeSize(fillWidth, 20);
        }
    }
    
    public void makeVisible() {
        visible = true;
        background.makeVisible();
        fill.makeVisible();
        updateVisual();
    }
    
    public void makeInvisible() {
        visible = false;
        background.makeInvisible();
        fill.makeInvisible();
    }
    
    public void finish() {
        makeInvisible();
    }
}