import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas es una clase que permite realizar dibujos gráficos simples
 * en un lienzo. 
 * Versión extendida con colores adicionales para el proyecto SilkRoad.
 *
 * @author  
 * Bruce Quig  
 * Michael Kolling (mik)  
 * Extendido para el proyecto SilkRoad
 *
 * @version 1.7 (extendido)
 */
public class Canvas {
    private static Canvas canvasSingleton;

    /**
     * Método de fábrica para obtener el objeto único (singleton) del lienzo.
     */
    public static Canvas getCanvas() {
        if(canvasSingleton == null) {
            canvasSingleton = new Canvas("SilkRoad Simulator", 800, 600, 
                                         Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    // ----- Parte de instancia -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object,ShapeDescription> shapes;
    
    /**
     * Crea un Canvas.
     * @param title  título que aparece en la ventana del Canvas
     * @param width  el ancho deseado del lienzo
     * @param height  la altura deseada del lienzo
     * @param bgColour  el color de fondo deseado para el lienzo
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object,ShapeDescription>();
    }

    /**
     * Establece la visibilidad del lienzo y lo trae al frente de la pantalla
     * cuando se hace visible.
     */
    public void setVisible(boolean visible) {
        if(graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D)canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Dibuja una forma dada en el lienzo.
     */
    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }
 
    /**
     * Borra del lienzo la forma asociada al objeto dado.
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Establece el color de primer plano del Canvas.  
     * Versión extendida que maneja más colores, incluido el blanco.
     */
    public void setForegroundColor(String colorString) {
        if (colorString == null) {
            graphic.setColor(Color.BLACK);
            return;
        }
        
        switch(colorString.toLowerCase().trim()) {
            case "red":
                graphic.setColor(Color.RED);
                break;
            case "black":
                graphic.setColor(Color.BLACK);
                break;
            case "blue":
                graphic.setColor(Color.BLUE);
                break;
            case "yellow":
                graphic.setColor(Color.YELLOW);
                break;
            case "green":
                graphic.setColor(Color.GREEN);
                break;
            case "magenta":
                graphic.setColor(Color.MAGENTA);
                break;
            case "cyan":
                graphic.setColor(Color.CYAN);
                break;
            case "orange":
                graphic.setColor(Color.ORANGE);
                break;
            case "pink":
                graphic.setColor(Color.PINK);
                break;
            case "gray":
                graphic.setColor(Color.GRAY);
                break;
            case "darkgray":
                graphic.setColor(Color.DARK_GRAY);
                break;
            case "lightgray":
                graphic.setColor(Color.LIGHT_GRAY);
                break;
            default:
                graphic.setColor(Color.BLACK);
                break;
        }
    }

    /**
     * Espera un número específico de milisegundos antes de continuar.
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            // Se ignora la excepción por el momento
        }
    }

    /**
     * Redibuja todas las formas que actualmente están en el lienzo.
     */
    private void redraw() {
        erase();
        for(Iterator i=objects.iterator(); i.hasNext(); ) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }
       
    /**
     * Borra todo el contenido del lienzo. (No repinta).
     */
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    /************************************************************************
     * Clase interna CanvasPane - el componente gráfico que contiene
     * realmente el lienzo dentro del marco (frame).
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }
    
    /************************************************************************
     * Clase interna ShapeDescription - almacena la información de la forma
     * y el color asociado.
     */
    private class ShapeDescription {
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}
