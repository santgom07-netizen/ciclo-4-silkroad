import java.util.ArrayList;
import java.util.List;

/**
 * Simulador de la Ruta de la Seda mejorado.
 * Incluye representación en espiral cuadrada y barra de progreso de ganancias.
 * Versión mejorada que cumple con todos los requisitos funcionales y de usabilidad.
 */
public class SilkRoad {
    private int length;
    private List<Store> stores;
    private List<Robot> robots;
    private List<Store> initialStores;
    private List<Robot> initialRobots;
    private boolean visible;
    private int profit;
    private boolean ok;
    private String lastErrorMessage;
    private ProgressBar progressBar;
    private SpiralGrid spiralGrid;
    
    // Colores para diferenciación visual
    private static final String[] STORE_COLORS = {"green", "red", "yellow", "magenta", "cyan", "orange"};
    private static final String[] ROBOT_COLORS = {"blue", "red", "green", "magenta", "orange", "pink"};

    /**
     * Crea una Ruta de la Seda con una longitud dada.
     * @param length La longitud de la ruta.
     */
    public SilkRoad(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva");
        }
        
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.initialStores = new ArrayList<>();
        this.initialRobots = new ArrayList<>();
        this.visible = false;
        this.profit = 0;
        this.ok = true;
        this.lastErrorMessage = "";
        
        // Inicializar componentes visuales
        this.spiralGrid = new SpiralGrid(length);
        this.progressBar = new ProgressBar();
        
        if (visible) {
            spiralGrid.makeVisible();
            progressBar.makeVisible();
        }
    }

    /**
     * Si es posible, coloca una tienda en una ubicación dada.
     * @param location La ubicación de la tienda
     * @param tenges Dinero inicial en la tienda
     */
    public void placeStore(int location, int tenges) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicación fuera de límites: " + location;
            showErrorIfVisible();
            return;
        }
        
        if (tenges < 0) {
            ok = false;
            lastErrorMessage = "El dinero inicial debe ser no negativo";
            showErrorIfVisible();
            return;
        }
        
        // Verificar si ya existe una tienda en esa ubicación
        for (Store s : stores) {
            if (s.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe una tienda en la ubicación: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        // Obtener coordenadas de la espiral
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = STORE_COLORS[stores.size() % STORE_COLORS.length];
        Store newStore = new Store(location, tenges, color, coords[0], coords[1]);
        
        if (!visible) {
            newStore.makeInvisible();
        }
        
        stores.add(newStore);
        updateMaxProfit();
        updateProgressBar();
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Elimina una tienda de una ubicación dada.
     * @param location La ubicación de la tienda
     */
    public void removeStore(int location) {
        Store toRemove = null;
        for (Store s : stores) {
            if (s.getLocation() == location) {
                toRemove = s;
                break;
            }
        }
        
        if (toRemove != null) {
            toRemove.makeInvisible();
            stores.remove(toRemove);
            updateMaxProfit();
            updateProgressBar();
            ok = true;
            lastErrorMessage = "";
        } else {
            ok = false;
            lastErrorMessage = "No se encontró una tienda en la ubicación: " + location;
            showErrorIfVisible();
        }
    }

    /**
     * Si es posible, coloca un robot en una ubicación dada.
     * @param location Ubicación inicial del robot
     */
    public void placeRobot(int location) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicación fuera de límites: " + location;
            showErrorIfVisible();
            return;
        }
        
        // Verificar si ya existe un robot en esa ubicación
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe un robot en la ubicación: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        // Obtener coordenadas de la espiral
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = ROBOT_COLORS[robots.size() % ROBOT_COLORS.length];
        Robot newRobot = new Robot(location, color, coords[0], coords[1]);
        
        if (!visible) {
            newRobot.makeInvisible();
        }
        
        robots.add(newRobot);
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Elimina un robot de una ubicación dada.
     * @param location La ubicación del robot
     */
    public void removeRobot(int location) {
        Robot toRemove = null;
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                toRemove = r;
                break;
            }
        }
        
        if (toRemove != null) {
            toRemove.makeInvisible();
            robots.remove(toRemove);
            ok = true;
            lastErrorMessage = "";
        } else {
            ok = false;
            lastErrorMessage = "No se encontró un robot en la ubicación: " + location;
            showErrorIfVisible();
        }
    }

    /**
     * Mueve un robot cierta cantidad de pasos.
     * @param location Ubicación actual del robot
     * @param meters Pasos a mover
     */
    public void moveRobot(int location, int meters) {
        Robot robotToMove = null;
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                robotToMove = r;
                break;
            }
        }
        
        if (robotToMove == null) {
            ok = false;
            lastErrorMessage = "No se encontró un robot en la ubicación: " + location;
            showErrorIfVisible();
            return;
        }
        
        int newLocation = robotToMove.getLocation() + meters;
        if (newLocation < 0 || newLocation >= length) {
            ok = false;
            lastErrorMessage = "El robot no puede moverse a la ubicación: " + newLocation;
            showErrorIfVisible();
            return;
        }
        
        // Verificar si hay otro robot en la nueva ubicación
        for (Robot r : robots) {
            if (r != robotToMove && r.getLocation() == newLocation) {
                ok = false;
                lastErrorMessage = "Ya hay un robot en la ubicación destino: " + newLocation;
                showErrorIfVisible();
                return;
            }
        }
        
        // Mover el robot
        int[] newCoords = spiralGrid.getSpiralCoordinates(newLocation);
        robotToMove.moveTo(newLocation, newCoords[0], newCoords[1]);
        
        // Verificar si hay ganancias en la nueva ubicación
        checkProfitAtLocation(newLocation);
        updateProgressBar();
        
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Reabastece todas las tiendas a su dinero inicial.
     */
    public void resupplyStores() {
        for (Store s : stores) {
            s.resupply();
        }
        updateProgressBar();
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Devuelve todos los robots a sus posiciones iniciales.
     */
    public void returnRobots() {
        for (Robot r : robots) {
            int[] coords = spiralGrid.getSpiralCoordinates(r.getInitialLocation());
            r.returnToInitialPosition(coords[0], coords[1]);
        }
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Reinicia la Ruta de la Seda como fue adicionada inicialmente.
     */
    public void reboot() {
        // Restaurar tiendas originales
        for (Store s : stores) {
            s.makeInvisible();
        }
        stores.clear();
        
        for (Store original : initialStores) {
            int[] coords = spiralGrid.getSpiralCoordinates(original.getLocation());
            Store restored = new Store(original.getLocation(), 
                                     original.getInitialTenges(), 
                                     STORE_COLORS[stores.size() % STORE_COLORS.length],
                                     coords[0], coords[1]);
            if (!visible) {
                restored.makeInvisible();
            }
            stores.add(restored);
        }
        
        // Restaurar robots originales
        for (Robot r : robots) {
            r.makeInvisible();
        }
        robots.clear();
        
        for (Robot original : initialRobots) {
            int[] coords = spiralGrid.getSpiralCoordinates(original.getInitialLocation());
            Robot restored = new Robot(original.getInitialLocation(), 
                                     ROBOT_COLORS[robots.size() % ROBOT_COLORS.length],
                                     coords[0], coords[1]);
            if (!visible) {
                restored.makeInvisible();
            }
            robots.add(restored);
        }
        
        profit = 0;
        updateProgressBar();
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * @return Ganancia actual
     */
    public int profit() {
        return profit;
    }

    /**
     * @return Número de tiendas
     */
    public int stores() {
        return stores.size();
    }

    /**
     * @return Número de robots
     */
    public int robots() {
        return robots.size();
    }

    /**
     * @return Longitud de la ruta de la seda
     */
    public int length() {
        return length;
    }

    /**
     * Hace visible el simulador.
     */
    public void makeVisible() {
        visible = true;
        spiralGrid.makeVisible();
        progressBar.makeVisible();
        
        for (Store s : stores) {
            s.makeVisible();
        }
        for (Robot r : robots) {
            r.makeVisible();
        }
    }

    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible() {
        visible = false;
        spiralGrid.makeInvisible();
        progressBar.makeInvisible();
        
        for (Store s : stores) {
            s.makeInvisible();
        }
        for (Robot r : robots) {
            r.makeInvisible();
        }
    }

    /**
     * Finaliza el simulador.
     */
    public void finish() {
        makeInvisible();
        stores.clear();
        robots.clear();
        initialStores.clear();
        initialRobots.clear();
        profit = 0;
        visible = false;
        ok = true;
        lastErrorMessage = "";
        
        if (spiralGrid != null) {
            spiralGrid.finish();
        }
        if (progressBar != null) {
            progressBar.finish();
        }
    }

    /**
     * @return Indica si la última operación fue exitosa
     */
    public boolean ok() {
        return ok;
    }

    /**
     * Obtiene el último mensaje de error.
     * @return El último mensaje de error
     */
    public String getLastError() {
        return lastErrorMessage;
    }

    /**
     * Verifica si hay ganancias en una ubicación dada.
     * @param location La ubicación a verificar
     */
    private void checkProfitAtLocation(int location) {
        for (Store s : stores) {
            if (s.getLocation() == location && s.getTenges() > 0) {
                // El robot toma TODO el dinero disponible en la tienda
                int dineroDisponible = s.getTenges();
                profit += dineroDisponible;
                s.decreaseTenges(dineroDisponible); // Vacía completamente la tienda
                break;
            }
        }
    }

    /**
     * Calcula la ganancia máxima posible actualmente.
     */
    private void updateMaxProfit() {
        // La ganancia máxima es la suma de todos los tenges disponibles en todas las tiendas
        int totalAvailable = 0;
        for (Store s : stores) {
            totalAvailable += s.getTenges();
        }
        // El máximo total es las ganancias ya obtenidas + lo que queda disponible
        int maxTotal = profit + totalAvailable;
        progressBar.setMaximum(maxTotal > 0 ? maxTotal : 1); // Evitar división por cero
    }

    /**
     * Actualiza la barra de progreso.
     */
    private void updateProgressBar() {
        updateMaxProfit();
        progressBar.updateProgress(profit);
    }

    /**
     * Guarda el estado inicial para poder reiniciar.
     */
    private void saveInitialState() {
        initialStores.clear();
        initialRobots.clear();
        
        for (Store s : stores) {
            initialStores.add(new Store(s.getLocation(), s.getInitialTenges()));
        }
        
        for (Robot r : robots) {
            initialRobots.add(new Robot(r.getInitialLocation()));
        }
    }

    /**
     * Muestra un mensaje de error solo si es visible.
     */
    private void showErrorIfVisible() {
        if (visible && !lastErrorMessage.isEmpty()) {
            System.out.println("Error: " + lastErrorMessage);
            // Aquí podrías mostrar un mensaje visual en el canvas si lo deseas
        }
    }

    /**
     * Clase interna para manejar la cuadrícula en espiral.
     */
    private class SpiralGrid {
        private Rectangle[][] grid;
        private int gridSize;
        private boolean visible;

        public SpiralGrid(int roadLength) {
            this.gridSize = (int) Math.ceil(Math.sqrt(roadLength)) + 2;
            this.grid = new Rectangle[gridSize][gridSize];
            this.visible = false;
            createSpiralPath();
        }

        /**
         * Crea la representación visual de la espiral.
         */
        private void createSpiralPath() {
            int centerX = gridSize / 2;
            int centerY = gridSize / 2;
            int x = centerX, y = centerY;
            int dx = 1, dy = 0;
            int steps = 1;
            int stepCount = 0;
            int directionChanges = 0;

            for (int i = 0; i < length && i < gridSize * gridSize; i++) {
                // Crear rectángulo para esta posición
                Rectangle cell = new Rectangle();
                cell.changeColor("lightgray");
                cell.changeSize(25, 25);
                cell.setPosition(x * 30 + 100, y * 30 + 100);
                
                if (!visible) {
                    cell.makeInvisible();
                }
                
                grid[x][y] = cell;

                // Calcular siguiente posición en espiral
                if (i < length - 1) {
                    x += dx;
                    y += dy;
                    stepCount++;

                    if (stepCount == steps) {
                        stepCount = 0;
                        int tempDx = dx;
                        dx = -dy;
                        dy = tempDx;
                        directionChanges++;
                        if (directionChanges % 2 == 0) {
                            steps++;
                        }
                    }
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

            int centerX = gridSize / 2;
            int centerY = gridSize / 2;
            int x = centerX, y = centerY;
            int dx = 1, dy = 0;
            int steps = 1;
            int stepCount = 0;
            int directionChanges = 0;

            for (int i = 0; i < position; i++) {
                x += dx;
                y += dy;
                stepCount++;

                if (stepCount == steps) {
                    stepCount = 0;
                    int tempDx = dx;
                    dx = -dy;
                    dy = tempDx;
                    directionChanges++;
                    if (directionChanges % 2 == 0) {
                        steps++;
                    }
                }
            }

            return new int[]{x * 30 + 100, y * 30 + 100};
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

    /**
     * Clase interna para la barra de progreso de ganancias.
     */
    private class ProgressBar {
        private Rectangle background;
        private Rectangle fill;
        private int maximum;
        private int current;
        private boolean visible;

        public ProgressBar() {
            this.maximum = 100;
            this.current = 0;
            this.visible = false;
            
            // Crear fondo de la barra
            background = new Rectangle();
            background.changeColor("darkgray");
            background.changeSize(20, 300);
            background.setPosition(50, 50);
            
            // Crear relleno de la barra
            fill = new Rectangle();
            fill.changeColor("green");
            fill.changeSize(0, 298); // Ligeramente más pequeño que el fondo
            fill.setPosition(51, 51);
            
            if (!visible) {
                background.makeInvisible();
                fill.makeInvisible();
            }
        }

        public void setMaximum(int max) {
            this.maximum = Math.max(1, max); // Evitar división por cero
            updateVisual();
        }

        public void updateProgress(int value) {
            this.current = Math.max(0, Math.min(value, maximum));
            updateVisual();
        }

        private void updateVisual() {
            if (maximum > 0) {
                int fillWidth = (int) ((double) current / maximum * 296); // 296 es el ancho máximo
                fill.changeSize(20, Math.max(0, fillWidth)); // Altura fija, ancho variable
            }
        }

        public void makeVisible() {
            visible = true;
            background.makeVisible();
            fill.makeVisible();
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
}