package silkroad; 
import java.util.*;

/**
 * Clase para resolver el problema de la maratón ICPC 2024.
 * Calcula la máxima utilidad diaria para el problema "The Silk Road... with Robots!"
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadContest {
    
    private List<Event> events;
    private long[] dailyProfits;
    private int numDays;
    
    /**
     * Clase interna para representar un evento.
     */
    private static class Event {
        int day;
        int type;
        int location;
        int tenges;
        
        Event(int day, int type, int location, int tenges) {
            this.day = day;
            this.type = type;
            this.location = location;
            this.tenges = tenges;
        }
    }
    
    /**
     * Constructor que recibe el número de días.
     * @param n Número de días
     */
    public SilkRoadContest(int n) {
        this.numDays = n;
        this.events = new ArrayList<>();
        this.dailyProfits = new long[n];
    }
    
    /**
     * Agrega un evento de un día.
     * @param day Número de día (0-indexed)
     * @param type Tipo: 1=robot, 2=tienda
     * @param location Ubicación en la ruta
     * @param tenges Cantidad de tenges (solo para tiendas)
     */
    public void addEvent(int day, int type, int location, int tenges) {
        events.add(new Event(day, type, location, tenges));
    }
    
    /**
     * Resuelve el problema completo y calcula las ganancias máximas diarias.
     * @return Array con la ganancia máxima de cada día
     */
    public long[] solve() {
        List<Integer> robotLocations = new ArrayList<>();
        List<int[]> storeData = new ArrayList<>();
        
        for (Event event : events) {
            if (event.type == 1) {
                robotLocations.add(event.location);
            } else if (event.type == 2) {
                storeData.add(new int[]{event.location, event.tenges});
            }
            
            dailyProfits[event.day] = calculateMaxProfit(robotLocations, storeData);
        }
        
        return dailyProfits;
    }
    
    /**
     * Calcula la máxima ganancia usando DP con bitmask.
     * @param robots Lista de ubicaciones de robots
     * @param stores Lista de tiendas [ubicación, tenges]
     * @return Ganancia máxima
     */
    private long calculateMaxProfit(List<Integer> robots, List<int[]> stores) {
        if (robots.isEmpty() || stores.isEmpty()) {
            return 0;
        }
        
        int n = stores.size();
        int m = robots.size();
        
        long[][] robotProfit = new long[m][1 << n];
        
        for (int r = 0; r < m; r++) {
            int robotPos = robots.get(r);
            
            for (int mask = 0; mask < (1 << n); mask++) {
                robotProfit[r][mask] = calculateBestRouteForRobot(robotPos, mask, stores);
            }
        }
        
        long[][] dp = new long[m + 1][1 << n];
        
        for (int i = 0; i <= m; i++) {
            Arrays.fill(dp[i], Long.MIN_VALUE / 2);
        }
        dp[0][0] = 0;
        
        // Para cada robot
        for (int robot = 0; robot < m; robot++) {
            for (int mask = 0; mask < (1 << n); mask++) {
                if (dp[robot][mask] == Long.MIN_VALUE / 2) continue;
                
                dp[robot + 1][mask] = Math.max(dp[robot + 1][mask], dp[robot][mask]);
                
                int remaining = ((1 << n) - 1) ^ mask;
                
                for (int subset = remaining; subset > 0; subset = (subset - 1) & remaining) {
                    long gain = robotProfit[robot][subset];
                    if (gain > 0) {
                        int newMask = mask | subset;
                        dp[robot + 1][newMask] = Math.max(dp[robot + 1][newMask], 
                                                          dp[robot][mask] + gain);
                    }
                }
            }
        }
        
        long maxProfit = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            maxProfit = Math.max(maxProfit, dp[m][mask]);
        }
        
        return maxProfit;
    }
    
    /**
     * Calcula la mejor ruta para un robot visitando un subconjunto específico de tiendas.
     * Usa DP estilo TSP: dp[mask][last] = máxima ganancia visitando tiendas en mask, terminando en last.
     * @param startPos Posición inicial del robot
     * @param storeMask Máscara de tiendas a visitar
     * @param stores Lista de todas las tiendas
     * @return Máxima ganancia posible
     */
    private long calculateBestRouteForRobot(int startPos, int storeMask, List<int[]> stores) {
        if (storeMask == 0) return 0;
        
        int n = stores.size();
        
        List<Integer> toVisit = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if ((storeMask & (1 << i)) != 0) {
                toVisit.add(i);
            }
        }
        
        int k = toVisit.size();
        if (k == 0) return 0;
        
        long[][] dp = new long[1 << k][k];
        for (int i = 0; i < (1 << k); i++) {
            Arrays.fill(dp[i], Long.MIN_VALUE / 2);
        }
        
        for (int j = 0; j < k; j++) {
            int storeIdx = toVisit.get(j);
            long storeLoc = stores.get(storeIdx)[0];
            long storeTenges = stores.get(storeIdx)[1];
            long distance = Math.abs(startPos - storeLoc);
            long profit = storeTenges - distance;
            
            dp[1 << j][j] = profit;
        }
        
        for (int mask = 0; mask < (1 << k); mask++) {
            for (int last = 0; last < k; last++) {
                if ((mask & (1 << last)) == 0) continue;
                if (dp[mask][last] <= Long.MIN_VALUE / 2) continue;
                
                int lastStoreIdx = toVisit.get(last);
                long lastLoc = stores.get(lastStoreIdx)[0];
                
                for (int next = 0; next < k; next++) {
                    if ((mask & (1 << next)) != 0) continue;
                    
                    int nextStoreIdx = toVisit.get(next);
                    long nextLoc = stores.get(nextStoreIdx)[0];
                    long nextTenges = stores.get(nextStoreIdx)[1];
                    long distance = Math.abs(lastLoc - nextLoc);
                    long profit = nextTenges - distance;
                    
                    int newMask = mask | (1 << next);
                    dp[newMask][next] = Math.max(dp[newMask][next], dp[mask][last] + profit);
                }
            }
        }
        
        long maxProfit = 0;
        for (int mask = 0; mask < (1 << k); mask++) {
            for (int last = 0; last < k; last++) {
                if (dp[mask][last] > Long.MIN_VALUE / 2) {
                    maxProfit = Math.max(maxProfit, dp[mask][last]);
                }
            }
        }
        
        return maxProfit;
    }
    
    /**
     * Simula la solución visualmente usando SilkRoad.
     * @param silkRoad Instancia del simulador
     */
    public void simulate(SilkRoad silkRoad) {
        System.out.println("=== SIMULANDO SOLUCIÓN DE LA MARATÓN ===");
        
        for (Event event : events) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {}
            
            if (event.type == 1) {
                silkRoad.placeRobot(event.location);
                System.out.println("Día " + (event.day + 1) + ": Robot agregado en posición " + event.location);
            } else if (event.type == 2) {
                silkRoad.placeStore(event.location, event.tenges);
                System.out.println("Día " + (event.day + 1) + ": Tienda agregada en posición " + 
                                 event.location + " con " + event.tenges + " tenges");
            }
            
            silkRoad.resupplyStores();
            silkRoad.returnRobots();
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
            
            silkRoad.moveRobots();
            
            System.out.println("Día " + (event.day + 1) + ": Ganancia máxima = " + dailyProfits[event.day]);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
        
        System.out.println("=== SIMULACIÓN COMPLETADA ===");
    }
    
    /**
     * Obtiene las ganancias diarias calculadas.
     * @return Array con las ganancias de cada día
     */
    public long[] getDailyProfits() {
        return dailyProfits.clone();
    }
    
    /**
     * Método estático para resolver desde entrada estándar de la maratón.
     * @param input Entrada en formato de la maratón
     * @return Array con las ganancias diarias
     */
    public static long[] solveFromInput(String input) {
        String[] lines = input.trim().split("\n");
        int n = Integer.parseInt(lines[0].trim());
        
        SilkRoadContest contest = new SilkRoadContest(n);
        
        for (int i = 1; i <= n; i++) {
            String[] parts = lines[i].trim().split("\\s+");
            int type = Integer.parseInt(parts[0]);
            int location = Integer.parseInt(parts[1]);
            int tenges = 0;
            
            if (type == 2 && parts.length > 2) {
                tenges = Integer.parseInt(parts[2]);
            }
            
            contest.addEvent(i - 1, type, location, tenges);
        }
        
        return contest.solve();
    }
}
