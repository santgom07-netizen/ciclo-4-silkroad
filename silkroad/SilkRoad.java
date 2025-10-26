package silkroad;  

import shapes.Canvas;  
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Simulador de la Ruta de la Seda 
 * Clase principal que coordina todos los componentes del simulador.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoad {
    private int length;
    private List<Store> stores;
    private List<Robot> robots;
    private boolean visible;
    private int currentProfit;
    private boolean ok;
    private String lastErrorMessage;
    
    private ProgressBar progressBar;
    private SpiralGrid spiralGrid;
    private ColorManager colorManager;
    
    /**
     * Constructor básico.
     * @param length Longitud de la ruta (debe ser positiva)
     * @throws IllegalArgumentException si longitud <= 0
     */
    public SilkRoad(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("La longitud debe ser positiva");
        }
        initializeSilkRoad(length);
    }
    
    /**
     * Constructor que recibe entrada de maratón.
     * @param marathonInput String con datos de entrada en formato de maratón
     * @throws IllegalArgumentException si hay error al parsear
     */
    public SilkRoad(String marathonInput) {
        parseMarathonInput(marathonInput);
    }
    
    /**
     * Constructor que recibe configuración de días.
     * @param days Array bidimensional con configuración de cada día
     * @throws IllegalArgumentException si el formato es inválido
     */
    public SilkRoad(int[][] days) {
        if (days == null || days.length == 0 || days[0].length < 3) {
            throw new IllegalArgumentException("Formato de dias invalido");
        }
        
        int roadLength = days[0][0];
        int numStores = days[0][1];
        int numRobots = days[0][2];
        
        initializeSilkRoad(roadLength);
        
        for (int i = 1; i <= numStores && i < days.length; i++) {
            if (days[i].length >= 2) {
                placeStore(days[i][0], days[i][1]);
            }
        }

        int robotStartIndex = numStores + 1;
        for (int i = robotStartIndex; i < robotStartIndex + numRobots && i < days.length; i++) {
            if (days[i].length >= 1) {
                placeRobot(days[i][0]);
            }
        }
    }
    
    /**
     * Inicializa la ruta.
     * @param length Longitud de la ruta
     */
    private void initializeSilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.visible = false;
        this.currentProfit = 0;
        this.ok = true;
        this.lastErrorMessage = "";
        
        this.spiralGrid = new SpiralGrid(length);
        this.progressBar = new ProgressBar();
        this.colorManager = new ColorManager();
    }
    
    /**
     * Parsea entrada de maratón.
     * @param input String en formato de maratón (Ciclo 3)
     * @throws IllegalArgumentException si hay error al parsear
     */
    private void parseMarathonInput(String input) {
        try {
            String[] lines = input.trim().split("\n");
            String[] firstLine = lines[0].trim().split("\\s+");
            
            int roadLength = Integer.parseInt(firstLine[0]);
            int numStores = Integer.parseInt(firstLine[1]);
            int numRobots = Integer.parseInt(firstLine[2]);
            
            initializeSilkRoad(roadLength);
            
            for (int i = 1; i <= numStores && i < lines.length; i++) {
                String[] storeData = lines[i].trim().split("\\s+");
                placeStore(Integer.parseInt(storeData[0]), Integer.parseInt(storeData[1]));
            }
            
            int robotStartLine = numStores + 1;
            for (int i = robotStartLine; i < robotStartLine + numRobots && i < lines.length; i++) {
                placeRobot(Integer.parseInt(lines[i].trim()));
            }
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear entrada: " + e.getMessage());
        }
    }
    
    /**
     * Coloca una tienda (Ciclo 1 - tienda normal por defecto).
     * @param location Ubicación (0 <= location < length)
     * @param tenges Cantidad inicial (>= 0)
     * @throws IllegalArgumentException si ubicación o tenges inválidos
     */
    public void placeStore(int location, int tenges) {
        placeStore("normal", location, tenges);
    }
    
    /**
     * Coloca una tienda especificando tipo (Ciclo 4).
     * @param type Tipo: "normal", "autonomous", "fighter", "generous"
     * @param location Ubicación en la ruta
     * @param tenges Cantidad inicial de dinero
     * @throws IllegalArgumentException si ubicación o tenges inválidos
     */
    public void placeStore(String type, int location, int tenges) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicacion fuera de limites: " + location;
            showErrorIfVisible();
            return;
        }
        
        if (tenges < 0) {
            ok = false;
            lastErrorMessage = "El dinero inicial debe ser no negativo";
            showErrorIfVisible();
            return;
        }
        
        String color = colorManager.getUniqueStoreColor();
        Store newStore = null;
        
        switch(type.toLowerCase()) {
            case "normal":
                int[] coords = spiralGrid.getSpiralCoordinates(location);
                newStore = new NormalStore(location, tenges, color, coords[0], coords[1]);
                break;
            case "autonomous":
                newStore = new AutonomousStore(location, tenges, color, length);
                int actualLocation = newStore.getLocation();
                int[] autonomousCoords = spiralGrid.getSpiralCoordinates(actualLocation);
                newStore.visualRepresentation.setPosition(autonomousCoords[0], autonomousCoords[1]);
                break;
            case "fighter":
                int[] fighterCoords = spiralGrid.getSpiralCoordinates(location);
                newStore = new FighterStore(location, tenges, color, fighterCoords[0], fighterCoords[1]);
                break;
            case "generous":
                int[] generousCoords = spiralGrid.getSpiralCoordinates(location);
                newStore = new GenerousStore(location, tenges, color, generousCoords[0], generousCoords[1]);
                break;
            default:
                int[] defaultCoords = spiralGrid.getSpiralCoordinates(location);
                newStore = new NormalStore(location, tenges, color, defaultCoords[0], defaultCoords[1]);
                break;
        }
        
        for (Store s : stores) {
            if (s.getLocation() == newStore.getLocation()) {
                ok = false;
                lastErrorMessage = "Ya existe una tienda en la ubicacion: " + newStore.getLocation();
                showErrorIfVisible();
                return;
            }
        }
        
        if (!visible) {
            newStore.makeInvisible();
        }
        
        stores.add(newStore);
        updateProgressBar();
        ok = true;
    }
    
    /**
     * Elimina una tienda.
     * @param location Ubicación de la tienda a eliminar
     */
    public void removeStore(int location) {
        Store toRemove = findStore(location);
        
        if (toRemove != null) {
            toRemove.makeInvisible();
            stores.remove(toRemove);
            updateProgressBar();
            ok = true;
        } else {
            ok = false;
            lastErrorMessage = "No se encontro tienda en ubicacion: " + location;
            showErrorIfVisible();
        }
    }
    
    /**
     * Coloca un robot (Ciclo 1 - robot normal por defecto).
     * @param location Ubicación del robot
     */
    public void placeRobot(int location) {
        placeRobot("normal", location);
    }
    
    /**
     * Coloca un robot especificando tipo (Ciclo 4).
     * @param type Tipo: "normal", "neverback", "tender", "greedy"
     * @param location Ubicación del robot
     */
    public void placeRobot(String type, int location) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicacion fuera de limites: " + location;
            showErrorIfVisible();
            return;
        }
        
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe un robot en la ubicacion: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        int[] coords = spiralGrid.getSpiralCoordinates(location);
        String color = colorManager.getUniqueRobotColor();
        Robot newRobot = null;
        
        switch(type.toLowerCase()) {
            case "normal":
                newRobot = new NormalRobot(location, color, coords[0], coords[1]);
                break;
            case "neverback":
                newRobot = new NeverBackRobot(location, color, coords[0], coords[1]);
                break;
            case "tender":
                newRobot = new TenderRobot(location, color, coords[0], coords[1]);
                break;
            case "greedy":
                newRobot = new GreedyRobot(location, color, coords[0], coords[1]);
                break;
            default:
                newRobot = new NormalRobot(location, color, coords[0], coords[1]);
                break;
        }
        
        if (!visible) {
            newRobot.makeInvisible();
        }
        
        robots.add(newRobot);
        ok = true;
    }
    
    /**
     * Elimina un robot usando findRobot.
     * @param location Ubicación del robot a eliminar
     */
    public void removeRobot(int location) {
        Robot toRemove = findRobot(location);
        
        if (toRemove != null) {
            toRemove.makeInvisible();
            robots.remove(toRemove);
            ok = true;
        } else {
            ok = false;
            lastErrorMessage = "No se encontro robot en ubicacion: " + location;
            showErrorIfVisible();
        }
    }
    
    /**
     * Mueve un robot cierto número de metros.
     * @param location Ubicación actual del robot
     * @param meters Pasos a mover (puede ser negativo)
     */
    public void moveRobot(int location, int meters) {
        Robot robotToMove = findRobot(location);
        if (robotToMove == null) {
            ok = false;
            lastErrorMessage = "No se encontro robot en ubicacion: " + location;
            showErrorIfVisible();
            return;
        }
        
        int newLocation = location + meters;
        if (newLocation < 0 || newLocation >= length) {
            ok = false;
            lastErrorMessage = "Movimiento fuera de limites";
            showErrorIfVisible();
            return;
        }
        
        for (Robot r : robots) {
            if (r != robotToMove && r.getLocation() == newLocation) {
                ok = false;
                lastErrorMessage = "Ya hay un robot en la ubicacion destino";
                showErrorIfVisible();
                return;
            }
        }
        
        int[] newCoords = spiralGrid.getSpiralCoordinates(newLocation);
        robotToMove.moveTo(newLocation, newCoords[0], newCoords[1]);
        
        Store store = findStore(newLocation);
        if (store != null && store.getTenges() > 0) {
            int gained = robotToMove.collectFromStore(store);
            robotToMove.addGain(gained);
            currentProfit += gained;
        }
        
        updateProgressBar();
        updateBlinkingRobot();
        ok = true;
    }
    
    /**
     * Mueve un robot de forma óptima (Ciclo 2).
     * @param location Ubicación actual del robot
     */
    public void moveRobot(int location) {
        Robot robotToMove = findRobot(location);
        if (robotToMove == null) {
            ok = false;
            lastErrorMessage = "No se encontro robot en ubicacion: " + location;
            showErrorIfVisible();
            return;
        }
        
        int bestMove = calculateOptimalMove(robotToMove);
        
        if (bestMove != 0) {
            moveRobot(location, bestMove);
        } else {
            ok = false;
            lastErrorMessage = "No hay movimiento beneficioso disponible para el robot";
            showErrorIfVisible();
        }
    }
    
    /**
     * Mueve todos los robots de forma óptima (Ciclo 2).
     */
    public void moveRobots() {
        boolean anyMoved = false;
        
        for (Robot robot : robots) {
            int currentLoc = robot.getLocation();
            int bestMove = calculateOptimalMove(robot);
            
            if (bestMove != 0) {
                moveRobot(currentLoc, bestMove);
                anyMoved = true;
            }
        }
        
        if (!anyMoved) {
            ok = false;
            lastErrorMessage = "No hay movimientos beneficiosos disponibles";
            showErrorIfVisible();
        }
    }
    
    /**
     * Calcula el mejor movimiento para un robot.
     * @param robot Robot a mover. No debe ser null
     * @return Metros a mover (0 si no hay movimiento beneficioso)
     */
    private int calculateOptimalMove(Robot robot) {
        int currentLocation = robot.getLocation();
        int bestMove = 0;
        double maxValue = 0;
        
        for (Store store : stores) {
            if (store.getTenges() > 0) {
                int storeLocation = store.getLocation();
                int distance = Math.abs(storeLocation - currentLocation);
                
                if (distance > 0) {
                    boolean occupied = false;
                    for (Robot other : robots) {
                        if (other != robot && other.getLocation() == storeLocation) {
                            occupied = true;
                            break;
                        }
                    }
                    
                    if (!occupied) {
                        double value = (double) store.getTenges() / (distance * 2);
                        if (value > maxValue) {
                            maxValue = value;
                            bestMove = storeLocation - currentLocation;
                        }
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    /**
     * Reabastece todas las tiendas a su cantidad inicial.
     */
    public void resupplyStores() {
        for (Store s : stores) {
            s.resupply();
        }
        updateProgressBar();
        ok = true;
    }
    
    /**
     * Devuelve todos los robots a posición inicial.
     */
    public void returnRobots() {
        for (Robot r : robots) {
            int initialLocation = r.getInitialLocation();
            int[] coords = spiralGrid.getSpiralCoordinates(initialLocation);
            r.returnToInitialPosition(coords[0], coords[1]);
        }
        ok = true;
    }
    
    /**
     * Reinicia el simulador (simula pasar un día).
     * Reabastece tiendas y devuelve robots a posiciones iniciales.
     */
    public void reboot() {
        for (Store s : stores) {
            s.reset();
        }
        
        for (Robot r : robots) {
            int[] coords = spiralGrid.getSpiralCoordinates(r.getInitialLocation());
            r.returnToInitialPosition(coords[0], coords[1]);
            r.reset();
        }
        
        currentProfit = 0;
        updateProgressBar();
        ok = true;
    }
    
    /**
     * Obtiene la ganancia máxima posible.
     * @return Ganancia actual más tenges disponibles en todas las tiendas
     */
    public int profit() {
        int maxPossible = currentProfit;
        for (Store s : stores) {
            maxPossible += s.getTenges();
        }
        return maxPossible;
    }
    
    /**
     * Obtiene info de tiendas.
     * @return Array int[][] donde cada fila es [ubicación, tenges actual]
     */
    public int[][] stores() {
        int[][] result = new int[stores.size()][2];
        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            result[i][0] = s.getLocation();
            result[i][1] = s.getTenges();
        }
        return result;
    }
    
    /**
     * Obtiene info de robots.
     * @return Array int[][] donde cada fila es [ubicación actual, ganancia total]
     */
    public int[][] robots() {
        int[][] result = new int[robots.size()][2];
        for (int i = 0; i < robots.size(); i++) {
            Robot r = robots.get(i);
            result[i][0] = r.getLocation();
            result[i][1] = r.getTotalGain();
        }
        return result;
    }
    
    /**
     * Obtiene tiendas vaciadas (Ciclo 2).
     * @return Array int[][] donde cada fila es [ubicación tienda, veces que fue vaciada]
     */
    public int[][] emptiedStores() {
        int[][] result = new int[stores.size()][2];
        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            result[i][0] = s.getLocation();
            result[i][1] = s.getTimesEmptied();
        }
        return result;
    }
    
    /**
     * Obtiene ganancias por movimiento (Ciclo 2).
     * @return Array int[][] donde cada fila es un robot y cada columna es ganancia por movimiento
     */
    public int[][] profitPerMove() {
        List<List<Integer>> allGains = new ArrayList<>();
        int maxMoves = 0;
        
        for (Robot r : robots) {
            List<Integer> gains = r.getGainsHistory();
            allGains.add(gains);
            maxMoves = Math.max(maxMoves, gains.size());
        }
        
        int[][] result = new int[robots.size()][maxMoves];
        for (int i = 0; i < robots.size(); i++) {
            List<Integer> gains = allGains.get(i);
            for (int j = 0; j < gains.size(); j++) {
                result[i][j] = gains.get(j);
            }
        }
        
        return result;
    }
    
    /**
     * Obtiene la longitud de la ruta.
     * @return Longitud de la ruta
     */
    public int length() {
        return length;
    }
    
    /**
     * Indica si la última operación fue exitosa.
     * @return true si la última operación fue exitosa, false si falló
     */
    public boolean ok() {
        return ok;
    }
    
    /**
     * Obtiene el último mensaje de error.
     * @return Mensaje de error de la última operación fallida
     */
    public String getLastError() {
        return lastErrorMessage;
    }
    
    /**
     * Hace visible el simulador mostrando todos los elementos en el canvas.
     */
    public void makeVisible() {
        visible = true;
        Canvas.getCanvas().setVisible(true);
        spiralGrid.makeVisible();
        progressBar.makeVisible();
        
        for (Store s : stores) s.makeVisible();
        for (Robot r : robots) r.makeVisible();
        
        updateBlinkingRobot();
    }
    
    /**
     * Hace invisible el simulador ocultando todos los elementos.
     */
    public void makeInvisible() {
        visible = false;
        spiralGrid.makeInvisible();
        progressBar.makeInvisible();
        
        for (Store s : stores) s.makeInvisible();
        for (Robot r : robots) r.makeInvisible();
    }
    
    /**
     * Finaliza el simulador y cierra la ejecución del programa.
     */
    public void finish() {
        makeInvisible();
        spiralGrid.finish();
        progressBar.finish();
        System.exit(0); 
    }
    
    /**
     * Busca un robot por su ubicación.
     * @param location Ubicación del robot buscado
     * @return Robot encontrado o null si no existe
     */
    private Robot findRobot(int location) {
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                return r;
            }
        }
        return null;
    }
    
    /**
     * Busca una tienda por su ubicación.
     * @param location Ubicación de la tienda buscada
     * @return Tienda encontrada o null si no existe
     */
    private Store findStore(int location) {
        for (Store s : stores) {
            if (s.getLocation() == location) {
                return s;
            }
        }
        return null;
    }
    
    /**
     * Actualiza la barra de progreso con la ganancia actual y máxima.
     */
    private void updateProgressBar() {
        int maxTotal = profit();
        progressBar.setMaximum(maxTotal > 0 ? maxTotal : 1);
        progressBar.updateProgress(currentProfit);
    }
    
    /**
     * Hace parpadear el robot con mayor ganancia (Ciclo 2).
     */
    private void updateBlinkingRobot() {
        if (!visible) return;
        
        Robot maxRobot = null;
        int maxGain = 0;
        
        for (Robot r : robots) {
            if (r.getTotalGain() > maxGain) {
                maxGain = r.getTotalGain();
                maxRobot = r;
            }
        }
        
        if (maxRobot != null && maxGain > 0) {
            maxRobot.blink(3);
        }
    }
    
    /**
     * Muestra mensaje de error si el simulador es visible.
     */
    private void showErrorIfVisible() {
        if (visible && !lastErrorMessage.isEmpty()) {
            JOptionPane.showMessageDialog(null, lastErrorMessage, 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
