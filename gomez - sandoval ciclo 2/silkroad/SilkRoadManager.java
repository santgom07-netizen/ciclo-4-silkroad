import java.util.*;

/**
 * Gestor de movimientos y estrategias para robots en la Ruta de la Seda.
 * Implementa la lógica de decisión de movimientos para maximizar ganancias (Requisito 11).
 */
public class SilkRoadManager {
    private List<Robot> robots;
    private List<Store> stores;
    private int roadLength;
    private Map<Robot, List<Integer>> robotGains; // Historial de ganancias por movimiento
    private Map<Store, Integer> storeEmptyCount; // Contador de veces que se vacía cada tienda
    
    /**
     * Constructor del gestor de movimientos.
     */
    public SilkRoadManager(int roadLength) {
        this.roadLength = roadLength;
        this.robots = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.robotGains = new HashMap<>();
        this.storeEmptyCount = new HashMap<>();
    }
    
    /**
     * Registra un robot en el sistema.
     */
    public void registerRobot(Robot robot) {
        robots.add(robot);
        robotGains.put(robot, new ArrayList<>());
    }
    
    /**
     * Registra una tienda en el sistema.
     */
    public void registerStore(Store store) {
        stores.add(store);
        storeEmptyCount.put(store, 0);
    }
    
    /**
     * Desregistra un robot del sistema.
     */
    public void unregisterRobot(Robot robot) {
        robots.remove(robot);
        robotGains.remove(robot);
    }
    
    /**
     * Desregistra una tienda del sistema.
     */
    public void unregisterStore(Store store) {
        stores.remove(store);
        storeEmptyCount.remove(store);
    }
    
    /**
     * Calcula el movimiento óptimo para un robot buscando maximizar ganancias.
     * Implementación del Requisito 11.
     * 
     * @param robot El robot para el cual calcular el movimiento
     * @return Número de pasos a mover (positivo o negativo)
     */
    public int calculateOptimalMove(Robot robot) {
        int currentLocation = robot.getLocation();
        int bestMove = 0;
        int maxProfit = 0;
        
        // Evaluar todas las tiendas alcanzables
        for (Store store : stores) {
            int storeLocation = store.getLocation();
            int distance = storeLocation - currentLocation;
            
            // Verificar si el movimiento es válido
            if (storeLocation >= 0 && storeLocation < roadLength) {
                // Verificar que no haya otro robot en esa posición
                boolean occupied = false;
                for (Robot other : robots) {
                    if (other != robot && other.getLocation() == storeLocation) {
                        occupied = true;
                        break;
                    }
                }
                
                if (!occupied && store.getTenges() > 0) {
                    // Calcular ganancia potencial considerando el costo del movimiento
                    int potentialProfit = calculateProfitWithCost(store.getTenges(), Math.abs(distance));
                    
                    if (potentialProfit > maxProfit) {
                        maxProfit = potentialProfit;
                        bestMove = distance;
                    }
                }
            }
        }
        
        return bestMove;
    }
    
    /**
     * Calcula la ganancia considerando el costo del movimiento.
     */
    private int calculateProfitWithCost(int storeValue, int distance) {
        // Estrategia simple: restar un costo por distancia
        int movementCost = distance * 2; // Costo arbitrario por unidad de distancia
        return Math.max(0, storeValue - movementCost);
    }
    
    /**
     * Registra una ganancia para un robot.
     */
    public void recordGain(Robot robot, int gain) {
        if (robotGains.containsKey(robot)) {
            robotGains.get(robot).add(gain);
        }
    }
    
    /**
     * Registra cuando una tienda es vaciada.
     */
    public void recordStoreEmptied(Store store) {
        if (storeEmptyCount.containsKey(store)) {
            storeEmptyCount.put(store, storeEmptyCount.get(store) + 1);
        }
    }
    
    /**
     * Consulta el número de veces que una tienda ha sido vaciada (Requisito 12).
     * 
     * @param storeLocation Ubicación de la tienda
     * @return Número de veces que ha sido vaciada
     */
    public int getStoreEmptyCount(int storeLocation) {
        for (Store store : stores) {
            if (store.getLocation() == storeLocation) {
                return storeEmptyCount.getOrDefault(store, 0);
            }
        }
        return 0;
    }
    
    /**
     * Consulta todas las estadísticas de vaciado de tiendas (Requisito 12).
     * 
     * @return Mapa con ubicación de tienda y número de veces vaciada
     */
    public Map<Integer, Integer> getAllStoreEmptyCounts() {
        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<Store, Integer> entry : storeEmptyCount.entrySet()) {
            result.put(entry.getKey().getLocation(), entry.getValue());
        }
        return result;
    }
    
    /**
     * Consulta las ganancias de un robot por movimiento (Requisito 13).
     * 
     * @param robotLocation Ubicación actual del robot
     * @return Lista de ganancias por cada movimiento
     */
    public List<Integer> getRobotGains(int robotLocation) {
        for (Robot robot : robots) {
            if (robot.getLocation() == robotLocation) {
                return new ArrayList<>(robotGains.getOrDefault(robot, new ArrayList<>()));
            }
        }
        return new ArrayList<>();
    }
    
    /**
     * Consulta todas las ganancias de todos los robots (Requisito 13).
     * 
     * @return Mapa con ubicación del robot y sus ganancias históricas
     */
    public Map<Integer, List<Integer>> getAllRobotGains() {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (Map.Entry<Robot, List<Integer>> entry : robotGains.entrySet()) {
            result.put(entry.getKey().getLocation(), new ArrayList<>(entry.getValue()));
        }
        return result;
    }
    
    /**
     * Calcula el robot con mayor ganancia total.
     * 
     * @return El robot con mayor ganancia acumulada
     */
    public Robot getRobotWithMaxGain() {
        Robot maxRobot = null;
        int maxTotalGain = -1;
        
        for (Map.Entry<Robot, List<Integer>> entry : robotGains.entrySet()) {
            int totalGain = entry.getValue().stream().mapToInt(Integer::intValue).sum();
            if (totalGain > maxTotalGain) {
                maxTotalGain = totalGain;
                maxRobot = entry.getKey();
            }
        }
        
        return maxRobot;
    }
    
    /**
     * Reinicia todas las estadísticas.
     */
    public void resetStatistics() {
        for (Robot robot : robotGains.keySet()) {
            robotGains.put(robot, new ArrayList<>());
        }
        for (Store store : storeEmptyCount.keySet()) {
            storeEmptyCount.put(store, 0);
        }
    }
}