import java.util.*;

/**
 * Clase para manejar las estadísticas y funcionalidades nuevas del Ciclo 2.
 * 
 * @autor Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class StatisticsManager {
    private Map<Integer, List<Integer>> robotGainsHistory; // Ubicación robot -> historial de ganancias
    private Map<Integer, Integer> storeEmptyCount; // Ubicación tienda -> veces vaciada
    private Robot blinkingRobot; // Robot que debe parpadear
    
    public StatisticsManager() {
        this.robotGainsHistory = new HashMap<>();
        this.storeEmptyCount = new HashMap<>();
        this.blinkingRobot = null;
    }
    
    /**
     * Inicializa el historial de ganancias para un robot.
     */
    public void initializeRobotGains(int location) {
        robotGainsHistory.put(location, new ArrayList<>());
    }
    
    /**
     * Inicializa el contador de vaciado para una tienda.
     */
    public void initializeStoreEmptyCount(int location) {
        storeEmptyCount.put(location, 0);
    }
    
    /**
     * Registra una ganancia para un robot y actualiza su ubicación.
     */
    public void recordRobotGain(int oldLocation, int newLocation, int gain) {
        List<Integer> gains = robotGainsHistory.remove(oldLocation);
        if (gains == null) gains = new ArrayList<>();
        gains.add(gain);
        robotGainsHistory.put(newLocation, gains);
    }
    
    /**
     * Incrementa el contador de veces que una tienda ha sido vaciada.
     */
    public void incrementStoreEmptyCount(int location) {
        int count = storeEmptyCount.getOrDefault(location, 0);
        storeEmptyCount.put(location, count + 1);
    }
    
    /**
     * Actualiza el robot que debe parpadear (el de mayor ganancia).
     */
    public void updateBlinkingRobot(List<Robot> robots, boolean visible) {
        if (!visible) {
            blinkingRobot = null;
            return;
        }
        
        int maxGain = 0;
        Robot maxRobot = null;
        
        for (Map.Entry<Integer, List<Integer>> entry : robotGainsHistory.entrySet()) {
            int totalGain = 0;
            for (Integer gain : entry.getValue()) {
                totalGain += gain;
            }
            if (totalGain > maxGain) {
                maxGain = totalGain;
                // Buscar el robot en esa ubicación
                for (Robot r : robots) {
                    if (r.getLocation() == entry.getKey()) {
                        maxRobot = r;
                        break;
                    }
                }
            }
        }
        
        blinkingRobot = (maxRobot != null && maxGain > 0) ? maxRobot : null;
        
        // Cambiar color del robot con mayor ganancia
        if (blinkingRobot != null) {
            blinkingRobot.changeColor("yellow");
        }
    }
    
    /**
     * Consulta el número de veces que cada tienda ha sido desocupada.
     */
    public Map<Integer, Integer> consultStoreEmptyCounts() {
        return new HashMap<>(storeEmptyCount);
    }
    
    /**
     * Consulta el número de veces que una tienda específica ha sido desocupada.
     */
    public int consultStoreEmptyCount(int storeLocation) {
        return storeEmptyCount.getOrDefault(storeLocation, 0);
    }
    
    /**
     * Consulta las ganancias que ha logrado cada robot en cada movimiento.
     */
    public Map<Integer, List<Integer>> consultRobotGains() {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : robotGainsHistory.entrySet()) {
            result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return result;
    }
    
    /**
     * Consulta las ganancias de un robot específico.
     */
    public List<Integer> consultRobotGains(int robotLocation) {
        List<Integer> gains = robotGainsHistory.get(robotLocation);
        return gains != null ? new ArrayList<>(gains) : new ArrayList<>();
    }
    
    /**
     * Limpia todas las estadísticas.
     */
    public void clear() {
        robotGainsHistory.clear();
        storeEmptyCount.clear();
        blinkingRobot = null;
    }
    
    /**
     * Elimina las estadísticas de un robot.
     */
    public void removeRobot(int location) {
        robotGainsHistory.remove(location);
    }
    
    /**
     * Elimina las estadísticas de una tienda.
     */
    public void removeStore(int location) {
        storeEmptyCount.remove(location);
    }
    
    /**
     * Actualiza la ubicación de un robot en las estadísticas.
     */
    public void updateRobotLocation(int oldLocation, int newLocation) {
        if (robotGainsHistory.containsKey(oldLocation)) {
            List<Integer> gains = robotGainsHistory.remove(oldLocation);
            robotGainsHistory.put(newLocation, gains);
        }
    }
}