/**
 * Clase para manejar la cuadrícula en espiral del simulador.
 * 
 * @autor Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SpiralGrid {
    private Rectangle[][] grid;
    private int gridSize;
    private boolean visible;
    private int[][] spiralCoords; // Coordenadas precalculadas
    private int length;

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
     */
    private void calculateSpiralCoordinates() {
        int centerX = gridSize / 2;
        int centerY = gridSize / 2;
        
        spiralCoords[0][0] = centerX * 30 + 50;
        spiralCoords[0][1] = centerY * 30 + 100;
        
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
            
            spiralCoords[i][0] = x * 30 + 50;
            spiralCoords[i][1] = y * 30 + 100;
            
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
     * Crea la representación visual de la espiral.
     */
    private void createSpiralPath() {
        for (int i = 0; i < length; i++) {
            Rectangle cell = new Rectangle();
            cell.changeColor("lightgray");
            cell.changeSize(25, 25);
            cell.moveHorizontal(spiralCoords[i][0]);
            cell.moveVertical(spiralCoords[i][1]);
            
            if (!visible) {
                cell.makeInvisible();
            }
            
            int row = i / gridSize;
            int col = i % gridSize;
            if (row < gridSize && col < gridSize) {
                grid[row][col] = cell;
            }
        }
    }

    /**
     * Obtiene las coordenadas visuales para una posición en la espiral.
     */
    public int[] getSpiralCoordinates(int position) {
        if (position < 0 || position >= length) {
            return new int[]{200, 200};
        }
        return new int[]{spiralCoords[position][0], spiralCoords[position][1]};
    }

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

    public void finish() {
        makeInvisible();
    }
}