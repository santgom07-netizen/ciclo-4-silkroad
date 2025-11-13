package silkroad;   

import shapes.Rectangle; 

/**
 * Clase para manejar la cuadricula en espiral del simulador.
 * Posiciona correctamente los elementos dentro de la espiral.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SpiralGrid {
    private Rectangle[][] grid;
    private int gridSize;
    private boolean visible;
    private int[][] spiralCoords; 
    private int length;
    private static final int CELL_SIZE = 30;
    private static final int START_X = 50;
    private static final int START_Y = 100;

    /**
     * Constructor que crea la cuadricula en espiral.
     * @param roadLength Longitud de la ruta
     */
    public SpiralGrid(int roadLength) {
        this.length = roadLength;
        this.gridSize = (int) Math.ceil(Math.sqrt(roadLength)) + 4;
        this.grid = new Rectangle[gridSize][gridSize];
        this.visible = false;
        this.spiralCoords = new int[roadLength][2];
        calculateSpiralCoordinates();
        createSpiralPath();
    }

    /**
     * Calcula todas las coordenadas de la espiral de una vez.
     * Centra la espiral y posiciona correctamente cada celda.
     */
    private void calculateSpiralCoordinates() {
        int centerX = gridSize / 2;
        int centerY = gridSize / 2;
        
        spiralCoords[0][0] = START_X + centerX * CELL_SIZE + CELL_SIZE / 2;
        spiralCoords[0][1] = START_Y + centerY * CELL_SIZE + CELL_SIZE / 2;
        
        if (length == 1) return;
        
        int x = centerX;
        int y = centerY;
        
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};
        int direction = 0;
        
        int steps = 1;
        int stepsTaken = 0;
        int directionsChanged = 0;
        
        for (int i = 1; i < length; i++) {
            x += dx[direction];
            y += dy[direction];
            
            spiralCoords[i][0] = START_X + x * CELL_SIZE + CELL_SIZE / 2;
            spiralCoords[i][1] = START_Y + y * CELL_SIZE + CELL_SIZE / 2;
            
            stepsTaken++;
            
            if (stepsTaken == steps) {
                stepsTaken = 0;
                direction = (direction + 1) % 4;
                directionsChanged++;
                
                if (directionsChanged % 2 == 0) {
                    steps++;
                }
            }
        }
    }

    /**
     * Crea la representacion visual de la espiral (INVISIBLE por defecto).
     */
    private void createSpiralPath() {
        for (int i = 0; i < length; i++) {
            Rectangle cell = new Rectangle();
            cell.changeColor("lightgray");
            cell.changeSize(CELL_SIZE - 5, CELL_SIZE - 5);
            
            // Posiciona el rectángulo en el centro de la celda
            int x = spiralCoords[i][0] - (CELL_SIZE - 5) / 2;
            int y = spiralCoords[i][1] - (CELL_SIZE - 5) / 2;
            
            cell.moveHorizontal(x);
            cell.moveVertical(y);
            
            cell.makeInvisible();
            
            int row = i / gridSize;
            int col = i % gridSize;
            if (row < gridSize && col < gridSize) {
                grid[row][col] = cell;
            }
        }
    }

    /**
     * Obtiene las coordenadas visuales para una posicion en la espiral.
     * @param position Posicion en la espiral
     * @return Array con [x, y] coordenadas CENTRADAS en la celda
     */
    public int[] getSpiralCoordinates(int position) {
        if (position < 0 || position >= length) {
            return new int[]{200, 200};
        }
        return new int[]{spiralCoords[position][0], spiralCoords[position][1]};
    }

    /**
     * Obtiene el tamaño de celda usado en la espiral.
     * @return Tamaño de cada celda en pixels
     */
    public int getCellSize() {
        return CELL_SIZE;
    }

    /**
     * Hace visible la espiral.
     */
    public void makeVisible() {
        visible = true;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] != null) {
                    grid[i][j].makeVisible();
                }
            }
        }
    }

    /**
     * Hace invisible la espiral.
     */
    public void makeInvisible() {
        visible = false;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] != null) {
                    grid[i][j].makeInvisible();
                }
            }
        }
    }

    /**
     * Finaliza la espiral.
     */
    public void finish() {
        makeInvisible();
    }
}
