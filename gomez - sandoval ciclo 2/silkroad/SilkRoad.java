import java.util.*;

/**
 * Simulador de la Ruta de la Seda 
 * Clase principal que coordina los otros componentes.
 * 
 * @autor Santiago Andres Gomez Rojas y Miguel Angel Sandoval
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
    
    // Componentes separados en otras clases
    private ProgressBar progressBar;
    private SpiralGrid spiralGrid;
    private StatisticsManager statistics;
    
    // Colores para diferenciación visual
    private static final String[] STORE_COLORS = {"green", "red", "yellow", "magenta", "cyan", "orange"};
    private static final String[] ROBOT_COLORS = {"blue", "red", "green", "magenta", "orange", "pink"};

    /**
     * Constructor básico: Crea una Ruta de la Seda con una longitud dada.
     */
    public SilkRoad(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva");
        }
        initializeSilkRoad(length);
    }
    
    /**
     * Constructor extendido: Crea una Ruta de la Seda desde la entrada del problema de la maratón.
     */
    public SilkRoad(String marathonInput) {
        parseMarathonInput(marathonInput);
    }
    
    /**
     * Constructor extendido: Crea una Ruta de la Seda con configuración completa.
     */
    public SilkRoad(int length, int[][] storeData, int[] robotPositions) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva");
        }
        
        initializeSilkRoad(length);
        
        if (storeData != null) {
            for (int[] store : storeData) {
                if (store.length >= 2) {
                    placeStore(store[0], store[1]);
                }
            }
        }
        
        if (robotPositions != null) {
            for (int position : robotPositions) {
                placeRobot(position);
            }
        }
        
        saveInitialState();
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
        
        // Inicializar componentes separados
        this.spiralGrid = new SpiralGrid(length);
        this.progressBar = new ProgressBar();
        this.statistics = new StatisticsManager();
        
        if (visible) {
            spiralGrid.makeVisible();
            progressBar.makeVisible();
        }
    }
    
    /**
     * Parsea la entrada del formato de la maratón.
     */
    private void parseMarathonInput(String input) {
        try {
            String[] lines = input.trim().split("\n");
            if (lines.length < 3) {
                throw new IllegalArgumentException("Formato de entrada inválido");
            }
            
            String[] firstLine = lines[0].trim().split("\\s+");
            int roadLength = Integer.parseInt(firstLine[0]);
            int numStores = Integer.parseInt(firstLine[1]);
            int numRobots = Integer.parseInt(firstLine[2]);
            
            initializeSilkRoad(roadLength);
            
            // Líneas de tiendas
            for (int i = 1; i <= numStores && i < lines.length; i++) {
                String[] storeData = lines[i].trim().split("\\s+");
                int location = Integer.parseInt(storeData[0]);
                int tenges = Integer.parseInt(storeData[1]);
                placeStore(location, tenges);
            }
            
            // Líneas de robots
            int robotStartLine = numStores + 1;
            for (int i = robotStartLine; i < robotStartLine + numRobots && i < lines.length; i++) {
                int location = Integer.parseInt(lines[i].trim());
                placeRobot(location);
            }
            
            saveInitialState();
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear entrada de maratón: " + e.getMessage());
        }
    }

    /**
     * Coloca una tienda en una ubicación dada.
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
        
        // Verificar duplicados
        for (Store s : stores) {
            if (s.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe una tienda en la ubicación: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        // Crear tienda usando coordenadas del grid
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = STORE_COLORS[stores.size() % STORE_COLORS.length];
        Store newStore = new Store(location, tenges, color, coords[0], coords[1]);
        
        if (!visible) {
            newStore.makeInvisible();
        }
        
        stores.add(newStore);
        statistics.initializeStoreEmptyCount(location);
        updateProgressBar();
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Elimina una tienda de una ubicación dada.
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
            statistics.removeStore(location);
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
     * Coloca un robot en una ubicación dada.
     */
    public void placeRobot(int location) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicación fuera de límites: " + location;
            showErrorIfVisible();
            return;
        }
        
        // Verificar duplicados
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe un robot en la ubicación: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        // Crear robot usando coordenadas del grid
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = ROBOT_COLORS[robots.size() % ROBOT_COLORS.length];
        Robot newRobot = new Robot(location, color, coords[0], coords[1]);
        
        if (!visible) {
            newRobot.makeInvisible();
        }
        
        robots.add(newRobot);
        statistics.initializeRobotGains(location);
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Elimina un robot de una ubicación dada.
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
            statistics.removeRobot(location);
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
     */
    public void moveRobot(int location, int meters) {
        Robot robotToMove = findRobot(location);
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
        
        // Verificar colisiones
        for (Robot r : robots) {
            if (r != robotToMove && r.getLocation() == newLocation) {
                ok = false;
                lastErrorMessage = "Ya hay un robot en la ubicación destino: " + newLocation;
                showErrorIfVisible();
                return;
            }
        }
        
        // Realizar movimiento
        int[] newCoords = spiralGrid.getSpiralCoordinates(newLocation);
        robotToMove.moveTo(newLocation, newCoords[0], newCoords[1]);
        
        // Procesar ganancias
        int gainedProfit = checkProfitAtLocation(newLocation);
        statistics.recordRobotGain(location, newLocation, gainedProfit);
        
        updateProgressBar();
        statistics.updateBlinkingRobot(robots, visible);
        
        ok = true;
        lastErrorMessage = "";
    }
    
    /**
     * Mueve un robot buscando maximizar la ganancia (REQUISITO 11).
     */
    public void moveRobot(int location) {
        Robot robotToMove = findRobot(location);
        if (robotToMove == null) {
            ok = false;
            lastErrorMessage = "No se encontró un robot en la ubicación: " + location;
            showErrorIfVisible();
            return;
        }
        
        int bestMove = calculateOptimalMove(location, robotToMove);
        
        if (bestMove != 0) {
            moveRobot(location, bestMove);
        } else {
            ok = false;
            lastErrorMessage = "No hay movimiento beneficioso disponible para el robot";
            showErrorIfVisible();
        }
    }
    
    /**
     * Mueve todos los robots de forma óptima (REQUISITO 11).
     */
    public void moveAllRobots() {
        List<Robot> robotsCopy = new ArrayList<>(robots);
        for (Robot robot : robotsCopy) {
            moveRobot(robot.getLocation());
        }
    }

    /**
     * Calcula el movimiento óptimo para un robot.
     */
    private int calculateOptimalMove(int location, Robot robotToMove) {
        int bestMove = 0;
        double maxValue = 0;
        
        for (Store store : stores) {
            if (store.getTenges() > 0) {
                int storeLocation = store.getLocation();
                int distance = Math.abs(storeLocation - location);
                
                // Verificar que no haya otro robot en esa posición
                boolean occupied = false;
                for (Robot other : robots) {
                    if (other != robotToMove && other.getLocation() == storeLocation) {
                        occupied = true;
                        break;
                    }
                }
                
                if (!occupied && distance > 0) {
                    double value = (double) store.getTenges() / distance;
                    if (value > maxValue) {
                        maxValue = value;
                        bestMove = storeLocation - location;
                    }
                }
            }
        }
        
        return bestMove;
    }

    /**
     * Verifica ganancias en una ubicación y actualiza estadísticas.
     */
    private int checkProfitAtLocation(int location) {
        for (Store s : stores) {
            if (s.getLocation() == location && s.getTenges() > 0) {
                int dineroDisponible = s.getTenges();
                profit += dineroDisponible;
                s.decreaseTenges(dineroDisponible);
                
                // Marcar tienda como vaciada
                if (s.getTenges() == 0) {
                    s.changeColor("darkgray");
                    statistics.incrementStoreEmptyCount(location);
                }
                
                return dineroDisponible;
            }
        }
        return 0;
    }

    // MÉTODOS DE CONSULTA DEL CICLO 2 (REQUISITOS 12 y 13)
    
    /**
     * Consulta el número de veces que cada tienda ha sido desocupada.
     */
    public Map<Integer, Integer> consultStoreEmptyCounts() {
        return statistics.consultStoreEmptyCounts();
    }
    
    /**
     * Consulta el número de veces que una tienda específica ha sido desocupada.
     */
    public int consultStoreEmptyCount(int storeLocation) {
        return statistics.consultStoreEmptyCount(storeLocation);
    }
    
    /**
     * Consulta las ganancias que ha logrado cada robot en cada movimiento.
     */
    public Map<Integer, List<Integer>> consultRobotGains() {
        return statistics.consultRobotGains();
    }
    
    /**
     * Consulta las ganancias de un robot específico.
     */
    public List<Integer> consultRobotGains(int robotLocation) {
        return statistics.consultRobotGains(robotLocation);
    }

    /**
     * Reabastece todas las tiendas a su dinero inicial.
     */
    public void resupplyStores() {
        for (Store s : stores) {
            boolean wasEmpty = (s.getTenges() == 0);
            s.resupply();
            if (wasEmpty) {
                int storeIndex = stores.indexOf(s);
                s.changeColor(STORE_COLORS[storeIndex % STORE_COLORS.length]);
            }
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
            int currentLocation = r.getLocation();
            int initialLocation = r.getInitialLocation();
            int[] coords = spiralGrid.getSpiralCoordinates(initialLocation);
            r.returnToInitialPosition(coords[0], coords[1]);
            statistics.updateRobotLocation(currentLocation, initialLocation);
        }
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Reinicia la Ruta de la Seda como fue adicionada inicialmente.
     */
    public void reboot() {
        // Limpiar elementos actuales
        for (Store s : stores) {
            s.makeInvisible();
        }
        for (Robot r : robots) {
            r.makeInvisible();
        }
        
        stores.clear();
        robots.clear();
        statistics.clear();
        
        // Restaurar estado inicial
        for (Store original : initialStores) {
            int[] coords = spiralGrid.getSpiralCoordinates(original.getLocation());
            Store restored = new Store(original.getLocation(), 
                                     original.getInitialTenges(), 
                                     STORE_COLORS[stores.size() % STORE_COLORS.length],
                                     coords[0], coords[1]);
            if (!visible) restored.makeInvisible();
            stores.add(restored);
            statistics.initializeStoreEmptyCount(original.getLocation());
        }
        
        for (Robot original : initialRobots) {
            int[] coords = spiralGrid.getSpiralCoordinates(original.getInitialLocation());
            Robot restored = new Robot(original.getInitialLocation(), 
                                     ROBOT_COLORS[robots.size() % ROBOT_COLORS.length],
                                     coords[0], coords[1]);
            if (!visible) restored.makeInvisible();
            robots.add(restored);
            statistics.initializeRobotGains(original.getInitialLocation());
        }
        
        profit = 0;
        updateProgressBar();
        ok = true;
        lastErrorMessage = "";
    }

    // MÉTODOS DE CONSULTA BÁSICOS
    
    public int profit() { return profit; }
    public int stores() { return stores.size(); }
    public int robots() { return robots.size(); }
    public int length() { return length; }
    public boolean ok() { return ok; }
    public String getLastError() { return lastErrorMessage; }

    /**
     * Hace visible el simulador.
     */
    public void makeVisible() {
        visible = true;
        spiralGrid.makeVisible();
        progressBar.makeVisible();
        
        for (Store s : stores) s.makeVisible();
        for (Robot r : robots) r.makeVisible();
        
        statistics.updateBlinkingRobot(robots, visible);
    }

    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible() {
        visible = false;
        spiralGrid.makeInvisible();
        progressBar.makeInvisible();
        
        for (Store s : stores) s.makeInvisible();
        for (Robot r : robots) r.makeInvisible();
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
        statistics.clear();
        profit = 0;
        visible = false;
        ok = true;
        lastErrorMessage = "";
        
        if (spiralGrid != null) spiralGrid.finish();
        if (progressBar != null) progressBar.finish();
    }

    // MÉTODOS AUXILIARES
    
    private Robot findRobot(int location) {
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                return r;
            }
        }
        return null;
    }
    
    private void updateProgressBar() {
        int totalAvailable = 0;
        for (Store s : stores) {
            totalAvailable += s.getTenges();
        }
        int maxTotal = profit + totalAvailable;
        progressBar.setMaximum(maxTotal > 0 ? maxTotal : 1);
        progressBar.updateProgress(profit);
    }

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

    private void showErrorIfVisible() {
        if (visible && !lastErrorMessage.isEmpty()) {
            System.out.println("Error: " + lastErrorMessage);
        }
    }
}