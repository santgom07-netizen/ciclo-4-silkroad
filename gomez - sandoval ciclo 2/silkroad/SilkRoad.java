import java.util.*;

/**
 * Simulador de la Ruta de la Seda - Versión Ciclo 2.
 * Incluye nuevas funcionalidades de creación desde entrada de maratón,
 * movimientos inteligentes de robots y consultas de estadísticas.
 * 
 * @author [Santiago Andres Gomez Rojas y Miguel Angel Sandoval]
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
    
    // Componentes separados
    private SpiralGrid spiralGrid;
    private ProgressBar progressBar;
    private ColorManager colorManager;
    private VisualEffects visualEffects;
    private SilkRoadManager manager;
    
    /**
     * Constructor básico: Crea una Ruta de la Seda con una longitud dada.
     * @param length La longitud de la ruta
     */
    public SilkRoad(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva");
        }
        
        initializeSilkRoad(length);
    }
    
    /**
     * Constructor extendido: Crea una Ruta de la Seda desde la entrada del problema de la maratón.
     * NUEVO - Requisito 10 del Ciclo 2
     * 
     * @param marathonInput String con el formato de entrada del problema de la maratón
     */
    public SilkRoad(String marathonInput) {
        parseMarathonInput(marathonInput);
    }
    
    /**
     * Constructor extendido: Crea una Ruta de la Seda con configuración completa.
     * NUEVO - Requisito 10 del Ciclo 2
     * 
     * @param length Longitud de la ruta
     * @param storeData Array bidimensional con [ubicación, tenges] para cada tienda
     * @param robotPositions Array con las posiciones iniciales de los robots
     */
    public SilkRoad(int length, int[][] storeData, int[] robotPositions) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva");
        }
        
        initializeSilkRoad(length);
        
        // Agregar tiendas desde los datos
        if (storeData != null) {
            for (int[] store : storeData) {
                if (store.length >= 2) {
                    placeStore(store[0], store[1]);
                }
            }
        }
        
        // Agregar robots desde las posiciones
        if (robotPositions != null) {
            for (int position : robotPositions) {
                placeRobot(position);
            }
        }
    }
    
    /**
     * Inicializa todos los componentes del simulador.
     */
    private void initializeSilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.initialStores = new ArrayList<>();
        this.initialRobots = new ArrayList<>();
        this.visible = false;
        this.profit = 0;
        this.ok = true;
        this.lastErrorMessage = "";
        
        // Inicializar componentes
        this.spiralGrid = new SpiralGrid(length);
        this.progressBar = new ProgressBar();
        this.colorManager = new ColorManager();
        this.visualEffects = new VisualEffects();
        this.manager = new SilkRoadManager(length);
    }
    
    /**
     * Parsea la entrada del formato de la maratón.
     * NUEVO - Requisito 10 del Ciclo 2
     */
    private void parseMarathonInput(String input) {
        try {
            String[] lines = input.trim().split("\n");
            if (lines.length < 3) {
                throw new IllegalArgumentException("Formato de entrada inválido");
            }
            
            // Primera línea: longitud, número de tiendas, número de robots
            String[] firstLine = lines[0].trim().split("\\s+");
            int roadLength = Integer.parseInt(firstLine[0]);
            int numStores = Integer.parseInt(firstLine[1]);
            int numRobots = Integer.parseInt(firstLine[2]);
            
            initializeSilkRoad(roadLength);
            
            // Líneas de tiendas: ubicación y tenges
            for (int i = 1; i <= numStores && i < lines.length; i++) {
                String[] storeData = lines[i].trim().split("\\s+");
                int location = Integer.parseInt(storeData[0]);
                int tenges = Integer.parseInt(storeData[1]);
                placeStore(location, tenges);
            }
            
            // Líneas de robots: ubicación inicial
            int robotStartLine = numStores + 1;
            for (int i = robotStartLine; i < robotStartLine + numRobots && i < lines.length; i++) {
                int location = Integer.parseInt(lines[i].trim());
                placeRobot(location);
            }
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear entrada de maratón: " + e.getMessage());
        }
    }
    
    /**
     * Coloca una tienda en una ubicación dada.
     * @param location La ubicación de la tienda
     * @param tenges Dinero inicial en la tienda
     */
    public void placeStore(int location, int tenges) {
        if (!validateLocation(location)) {
            return;
        }
        
        if (tenges < 0) {
            setError("El dinero inicial debe ser no negativo");
            return;
        }
        
        if (getStoreAt(location) != null) {
            setError("Ya existe una tienda en la ubicación: " + location);
            return;
        }
        
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = colorManager.getNextStoreColor();
        Store newStore = new Store(location, tenges, color, coords[0], coords[1]);
        
        if (!visible) {
            newStore.makeInvisible();
        }
        
        stores.add(newStore);
        manager.registerStore(newStore);
        
        // Guardar en el estado inicial
        initialStores.add(new Store(location, tenges));
        
        updateMaxProfit();
        updateProgressBar();
        setSuccess();
    }
    
    /**
     * Elimina una tienda de una ubicación dada.
     * @param location La ubicación de la tienda
     */
    public void removeStore(int location) {
        Store toRemove = getStoreAt(location);
        
        if (toRemove != null) {
            toRemove.makeInvisible();
            stores.remove(toRemove);
            manager.unregisterStore(toRemove);
            
            // Remover del estado inicial también
            initialStores.removeIf(s -> s.getLocation() == location);
            
            updateMaxProfit();
            updateProgressBar();
            setSuccess();
        } else {
            setError("No se encontró una tienda en la ubicación: " + location);
        }
    }
    
    /**
     * Coloca un robot en una ubicación dada.
     * @param location Ubicación inicial del robot
     */
    public void placeRobot(int location) {
        if (!validateLocation(location)) {
            return;
        }
        
        if (getRobotAt(location) != null) {
            setError("Ya existe un robot en la ubicación: " + location);
            return;
        }
        
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = colorManager.getNextRobotColor();
        Robot newRobot = new Robot(location, color, coords[0], coords[1]);
        
        if (!visible) {
            newRobot.makeInvisible();
        }
        
        robots.add(newRobot);
        manager.registerRobot(newRobot);
        
        // Guardar en el estado inicial
        initialRobots.add(new Robot(location));
        
        updateBlinkingRobot();
        setSuccess();
    }
    
    /**
     * Elimina un robot de una ubicación dada.
     * @param location La ubicación del robot
     */
    public void removeRobot(int location) {
        Robot toRemove = getRobotAt(location);
        
        if (toRemove != null) {
            toRemove.makeInvisible();
            robots.remove(toRemove);
            manager.unregisterRobot(toRemove);
            
            // Remover del estado inicial también
            initialRobots.removeIf(r -> r.getInitialLocation() == location);
            
            updateBlinkingRobot();
            setSuccess();
        } else {
            setError("No se encontró un robot en la ubicación: " + location);
        }
    }
    
    /**
     * Mueve un robot cierta cantidad de pasos.
     * Versión básica del Ciclo 1.
     * 
     * @param location Ubicación actual del robot
     * @param meters Pasos a mover
     */
    public void moveRobot(int location, int meters) {
        Robot robot = getRobotAt(location);
        
        if (robot == null) {
            setError("No se encontró un robot en la ubicación: " + location);
            return;
        }
        
        performRobotMove(robot, meters);
    }
    
    /**
     * Mueve un robot buscando maximizar la ganancia.
     * NUEVO - Requisito 11 del Ciclo 2
     * 
     * @param location Ubicación actual del robot
     */
    public void moveRobot(int location) {
        Robot robot = getRobotAt(location);
        
        if (robot == null) {
            setError("No se encontró un robot en la ubicación: " + location);
            return;
        }
        
        // Calcular movimiento óptimo
        int optimalMove = manager.calculateOptimalMove(robot);
        
        if (optimalMove != 0) {
            performRobotMove(robot, optimalMove);
        } else {
            setError("No hay movimiento beneficioso disponible para el robot");
        }
    }
    
    /**
     * Mueve todos los robots de forma óptima.
     * NUEVO - Requisito 11 del Ciclo 2
     */
    public void moveAllRobots() {
        List<Robot> robotsCopy = new ArrayList<>(robots);
        for (Robot robot : robotsCopy) {
            int optimalMove = manager.calculateOptimalMove(robot);
            if (optimalMove != 0) {
                performRobotMove(robot, optimalMove);
            }
        }
    }
    
    /**
     * Ejecuta el movimiento de un robot.
     */
    private void performRobotMove(Robot robot, int meters) {
        int newLocation = robot.getLocation() + meters;
        
        if (newLocation < 0 || newLocation >= length) {
            setError("El robot no puede moverse a la ubicación: " + newLocation);
            return;
        }
        
        if (getRobotAt(newLocation) != null) {
            setError("Ya hay un robot en la ubicación destino: " + newLocation);
            return;
        }
        
        // Mover el robot
        int[] newCoords = spiralGrid.getSpiralCoordinates(newLocation);
        robot.moveTo(newLocation, newCoords[0], newCoords[1]);
        
        // Verificar y registrar ganancias
        int gainedProfit = checkProfitAtLocation(newLocation);
        if (gainedProfit > 0) {
            manager.recordGain(robot, gainedProfit);
            profit += gainedProfit;
        }
        
        updateProgressBar();
        updateBlinkingRobot();
        setSuccess();
    }
    
    /**
     * Consulta el número de veces que cada tienda ha sido desocupada.
     * NUEVO - Requisito 12 del Ciclo 2
     * 
     * @return Mapa con ubicación de tienda y número de veces vaciada
     */
    public Map<Integer, Integer> consultStoreEmptyCounts() {
        return manager.getAllStoreEmptyCounts();
    }
    
    /**
     * Consulta el número de veces que una tienda específica ha sido desocupada.
     * NUEVO - Requisito 12 del Ciclo 2
     * 
     * @param storeLocation Ubicación de la tienda
     * @return Número de veces que ha sido vaciada
     */
    public int consultStoreEmptyCount(int storeLocation) {
        return manager.getStoreEmptyCount(storeLocation);
    }
    
    /**
     * Consulta las ganancias que ha logrado cada robot en cada movimiento.
     * NUEVO - Requisito 13 del Ciclo 2
     * 
     * @return Mapa con ubicación del robot y lista de ganancias por movimiento
     */
    public Map<Integer, List<Integer>> consultRobotGains() {
        return manager.getAllRobotGains();
    }
    
    /**
     * Consulta las ganancias de un robot específico.
     * NUEVO - Requisito 13 del Ciclo 2
     * 
     * @param robotLocation Ubicación actual del robot
     * @return Lista de ganancias por cada movimiento
     */
    public List<Integer> consultRobotGains(int robotLocation) {
        return manager.getRobotGains(robotLocation);
    }
    
    /**
     * Reabastece todas las tiendas a su dinero inicial.
     */
    public void resupplyStores() {
        for (Store s : stores) {
            int previousTenges = s.getTenges();
            s.resupply();
            
            // Actualizar color si la tienda estaba vacía
            if (previousTenges == 0 && s.getTenges() > 0) {
                s.changeColor(colorManager.getNextStoreColor());
            }
        }
        updateProgressBar();
        setSuccess();
    }
    
    /**
     * Devuelve todos los robots a sus posiciones iniciales.
     */
    public void returnRobots() {
        for (Robot r : robots) {
            int[] coords = spiralGrid.getSpiralCoordinates(r.getInitialLocation());
            r.returnToInitialPosition(coords[0], coords[1]);
        }
        setSuccess();
    }
    
    /**
     * Reinicia la Ruta de la Seda como fue adicionada inicialmente.
     */
    public void reboot() {
        // Limpiar visuales
        visualEffects.stopBlinking();
        
        // Limpiar el manager anterior
        manager.resetStatistics();
        
        // Limpiar tiendas existentes
        for (Store s : stores) {
            s.makeInvisible();
        }
        stores.clear();
        
        // Limpiar robots existentes
        for (Robot r : robots) {
            r.makeInvisible();
        }
        robots.clear();
        
        // Reiniciar el manager para limpiar referencias
        manager = new SilkRoadManager(length);
        
        // Reiniciar color manager
        colorManager.reset();
        
        // Restaurar tiendas desde el estado inicial
        for (Store original : initialStores) {
            int[] coords = spiralGrid.getSpiralCoordinates(original.getLocation());
            Store restored = new Store(
                original.getLocation(),
                original.getInitialTenges(),
                colorManager.getNextStoreColor(),
                coords[0], coords[1]
            );
            
            if (!visible) {
                restored.makeInvisible();
            }
            stores.add(restored);
            manager.registerStore(restored);
        }
        
        // Restaurar robots desde el estado inicial
        for (Robot original : initialRobots) {
            int[] coords = spiralGrid.getSpiralCoordinates(original.getInitialLocation());
            Robot restored = new Robot(
                original.getInitialLocation(),
                colorManager.getNextRobotColor(),
                coords[0], coords[1]
            );
            
            if (!visible) {
                restored.makeInvisible();
            }
            robots.add(restored);
            manager.registerRobot(restored);
        }
        
        // Reiniciar ganancia total
        profit = 0;
        updateProgressBar();
        setSuccess();
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
        
        updateBlinkingRobot();
    }
    
    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible() {
        visible = false;
        visualEffects.stopBlinking();
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
        visualEffects.finish();
        stores.clear();
        robots.clear();
        initialStores.clear();
        initialRobots.clear();
        profit = 0;
        
        if (spiralGrid != null) {
            spiralGrid.finish();
        }
        if (progressBar != null) {
            progressBar.finish();
        }
        
        setSuccess();
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
    
    // ========== MÉTODOS AUXILIARES PRIVADOS ==========
    
    /**
     * Valida si una ubicación es válida.
     */
    private boolean validateLocation(int location) {
        if (location < 0 || location >= length) {
            setError("Ubicación fuera de límites: " + location);
            return false;
        }
        return true;
    }
    
    /**
     * Obtiene la tienda en una ubicación específica.
     */
    private Store getStoreAt(int location) {
        for (Store s : stores) {
            if (s.getLocation() == location) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * Obtiene el robot en una ubicación específica.
     */
    private Robot getRobotAt(int location) {
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                return r;
            }
        }
        return null;
    }
    
    /**
     * Verifica y procesa ganancias en una ubicación.
     * @return La cantidad de ganancia obtenida
     */
    private int checkProfitAtLocation(int location) {
        Store store = getStoreAt(location);
        
        if (store != null && store.getTenges() > 0) {
            int gainedAmount = store.getTenges();
            store.decreaseTenges(gainedAmount);
            
            // Cambiar color de tienda vacía
            if (store.getTenges() == 0) {
                store.changeColor(colorManager.getEmptyStoreColor());
                manager.recordStoreEmptied(store);
            }
            
            return gainedAmount;
        }
        return 0;
    }
    
    /**
     * Actualiza el robot que debe parpadear.
     */
    private void updateBlinkingRobot() {
        if (!visible) return;
        
        Robot maxRobot = manager.getRobotWithMaxGain();
        if (maxRobot != null) {
            visualEffects.startBlinking(maxRobot);
        } else {
            visualEffects.stopBlinking();
        }
    }
    
    /**
     * Calcula la ganancia máxima posible.
     */
    private void updateMaxProfit() {
        int totalAvailable = 0;
        for (Store s : stores) {
            totalAvailable += s.getTenges();
        }
        int maxTotal = profit + totalAvailable;
        progressBar.setMaximum(maxTotal > 0 ? maxTotal : 1);
    }
    
    /**
     * Actualiza la barra de progreso.
     */
    private void updateProgressBar() {
        updateMaxProfit();
        progressBar.updateProgress(profit);
    }
    
    /**
     * Establece el estado de éxito.
     */
    private void setSuccess() {
        this.ok = true;
        this.lastErrorMessage = "";
    }
    
    /**
     * Establece un error.
     */
    private void setError(String message) {
        this.ok = false;
        this.lastErrorMessage = message;
        showErrorIfVisible();
    }
    
    /**
     * Muestra un mensaje de error si el simulador es visible.
     */
    private void showErrorIfVisible() {
        if (visible && !lastErrorMessage.isEmpty()) {
            System.err.println("Error: " + lastErrorMessage);
        }
    }
}

// ========== CLASES AUXILIARES INTERNAS ==========

/**
 * Clase para manejar la cuadrícula en espiral del simulador.
 */
class SpiralGrid {
    private int size;
    private Rectangle[] cells;
    private boolean visible;
    private final int cellSize = 50;
    private final int gridSpacing = 55;
    private final int startX = 50;
    private final int startY = 50;
    
    public SpiralGrid(int size) {
        this.size = Math.min(size, 100); // Limitar para visualización
        this.cells = new Rectangle[this.size];
        this.visible = false;
    }
    
    public int[] getSpiralCoordinates(int location) {
        // Disposición en grilla 10x10 para mejor visualización
        int row = location / 10;
        int col = location % 10;
        int x = startX + col * gridSpacing;
        int y = startY + row * gridSpacing;
        return new int[]{x, y};
    }
    
    public void makeVisible() {
        if (visible) return;
        
        visible = true;
        // Crear celdas visuales de la grilla
        for (int i = 0; i < size; i++) {
            if (cells[i] == null) {
                cells[i] = new Rectangle();
            }
            int[] coords = getSpiralCoordinates(i);
            cells[i].changeSize(cellSize, cellSize);
            cells[i].moveHorizontal(coords[0] - 70);
            cells[i].moveVertical(coords[1] - 15);
            cells[i].changeColor("lightgray");
            cells[i].makeVisible();
        }
    }
    
    public void makeInvisible() {
        visible = false;
        for (Rectangle cell : cells) {
            if (cell != null) {
                cell.makeInvisible();
            }
        }
    }
    
    public void finish() {
        makeInvisible();
        cells = null;
    }
}

/**
 * Clase para manejar la barra de progreso.
 */
class ProgressBar {
    private Rectangle background;
    private Rectangle progress;
    private int maximum;
    private int current;
    private boolean visible;
    
    public ProgressBar() {
        this.maximum = 100;
        this.current = 0;
        this.visible = false;
        
        // Crear elementos visuales
        background = new Rectangle();
        background.changeSize(500, 20);
        background.moveHorizontal(650 - 70);
        background.moveVertical(50 - 15);
        background.changeColor("gray");
        
        progress = new Rectangle();
        progress.changeSize(1, 20);
        progress.moveHorizontal(650 - 70);
        progress.moveVertical(50 - 15);
        progress.changeColor("green");
    }
    
    public void setMaximum(int max) {
        this.maximum = max;
        updateVisual();
    }
    
    public void updateProgress(int value) {
        this.current = value;
        updateVisual();
    }
    
    private void updateVisual() {
        if (visible && maximum > 0) {
            int width = (current * 500) / maximum;
            progress.changeSize(Math.max(1, width), 20);
        }
    }
    
    public void makeVisible() {
        visible = true;
        background.makeVisible();
        progress.makeVisible();
        updateVisual();
    }
    
    public void makeInvisible() {
        visible = false;
        background.makeInvisible();
        progress.makeInvisible();
    }
    
    public void finish() {
        makeInvisible();
    }
}

/**
 * Clase para manejar los colores del simulador.
 */
class ColorManager {
    private String[] storeColors = {"green", "blue", "cyan", "magenta", "orange"};
    private String[] robotColors = {"red", "blue", "yellow", "pink", "cyan"};
    private int storeColorIndex;
    private int robotColorIndex;
    
    public ColorManager() {
        reset();
    }
    
    public String getNextStoreColor() {
        String color = storeColors[storeColorIndex % storeColors.length];
        storeColorIndex++;
        return color;
    }
    
    public String getNextRobotColor() {
        String color = robotColors[robotColorIndex % robotColors.length];
        robotColorIndex++;
        return color;
    }
    
    public String getEmptyStoreColor() {
        return "darkgray";
    }
    
    public void reset() {
        storeColorIndex = 0;
        robotColorIndex = 0;
    }
}

/**
 * Clase para manejar efectos visuales como el parpadeo.
 */
class VisualEffects {
    private Robot blinkingRobot;
    private Thread blinkThread;
    private volatile boolean blinking;
    
    public void startBlinking(Robot robot) {
        stopBlinking();
        
        this.blinkingRobot = robot;
        this.blinking = true;
        
        blinkThread = new Thread(() -> {
            String originalColor = "blue"; // Color por defecto
            while (blinking) {
                try {
                    robot.changeColor("yellow");
                    Thread.sleep(500);
                    robot.changeColor(originalColor);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        blinkThread.start();
    }
    
    public void stopBlinking() {
        blinking = false;
        if (blinkThread != null) {
            blinkThread.interrupt();
            try {
                blinkThread.join(100);
            } catch (InterruptedException e) {
                // Ignorar
            }
            blinkThread = null;
        }
        blinkingRobot = null;
    }
    
    public void finish() {
        stopBlinking();
    }
}