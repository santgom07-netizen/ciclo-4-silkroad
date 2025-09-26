import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Clase de pruebas unitarias para SilkRoad - Ciclo 2.
 * Pruebas en modo invisible que cubren todos los requisitos funcionales nuevos.
 * 
 * @author [Santiago Andres Gomez Rojas y Miguel Angel Sandoval]
 */
public class SilkRoadC2Test {
    
    private SilkRoad silkRoad;
    
    @Before
    public void setUp() {
        // Inicializar antes de cada prueba
        silkRoad = null;
    }
    
    @After
    public void tearDown() {
        // Limpiar después de cada prueba
        if (silkRoad != null) {
            silkRoad.finish();
        }
    }
    
    // ========== PRUEBAS REQUISITO 10: CREAR DESDE ENTRADA DE MARATÓN ==========
    
    @Test
    public void shouldCreateSilkRoadFromMarathonInput() {
        String input = "10 3 2\n" +  // longitud=10, 3 tiendas, 2 robots
                      "2 100\n" +      // tienda en posición 2 con 100 tenges
                      "5 200\n" +      // tienda en posición 5 con 200 tenges  
                      "8 150\n" +      // tienda en posición 8 con 150 tenges
                      "0\n" +          // robot en posición 0
                      "9\n";           // robot en posición 9
        
        silkRoad = new SilkRoad(input);
        
        assertEquals(10, silkRoad.length());
        assertEquals(3, silkRoad.stores());
        assertEquals(2, silkRoad.robots());
        assertTrue(silkRoad.ok());
    }
    
    @Test
    public void shouldNotCreateSilkRoadFromInvalidMarathonInput() {
        try {
            silkRoad = new SilkRoad("invalid input");
            fail("Debería lanzar excepción con entrada inválida");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Error al parsear"));
        }
    }
    
    @Test
    public void shouldCreateSilkRoadWithArrayConstructor() {
        int[][] storeData = {{2, 100}, {5, 200}, {8, 150}};
        int[] robotPositions = {0, 9};
        
        silkRoad = new SilkRoad(10, storeData, robotPositions);
        
        assertEquals(10, silkRoad.length());
        assertEquals(3, silkRoad.stores());
        assertEquals(2, silkRoad.robots());
        assertTrue(silkRoad.ok());
    }
    
    // ========== PRUEBAS REQUISITO 11: MOVIMIENTOS INTELIGENTES ==========
    
    @Test
    public void shouldMoveRobotOptimally() {
        silkRoad = new SilkRoad(10);
        silkRoad.placeStore(3, 100);
        silkRoad.placeStore(7, 50);
        silkRoad.placeRobot(0);
        
        // El robot debe moverse a la tienda más rentable
        silkRoad.moveRobot(0);
        
        assertTrue(silkRoad.ok());
        // El robot debería haberse movido hacia la tienda con mejor relación ganancia/distancia
    }
    
    @Test
    public void shouldNotMoveRobotWhenNoGoodMoveAvailable() {
        silkRoad = new SilkRoad(10);
        silkRoad.placeStore(3, 0); // Tienda vacía
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0);
        
        assertFalse(silkRoad.ok());
        assertEquals("No hay movimiento beneficioso disponible para el robot", 
                    silkRoad.getLastError());
    }
    
    @Test
    public void shouldMoveAllRobotsOptimally() {
        silkRoad = new SilkRoad(15);
        silkRoad.placeStore(5, 100);
        silkRoad.placeStore(10, 200);
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(14);
        
        int initialProfit = silkRoad.profit();
        silkRoad.moveAllRobots();
        
        assertTrue(silkRoad.ok());
        // Al menos uno de los robots debería haber obtenido ganancias
        assertTrue(silkRoad.profit() > initialProfit);
    }
    
    // ========== PRUEBAS REQUISITO 12: CONSULTAR TIENDAS VACIADAS ==========
    
    @Test
    public void shouldCountStoreEmptyTimes() {
        silkRoad = new SilkRoad(10);
        silkRoad.placeStore(3, 100);
        silkRoad.placeRobot(0);
        
        // Mover robot a la tienda para vaciarla
        silkRoad.moveRobot(0, 3);
        assertEquals(1, silkRoad.consultStoreEmptyCount(3));
        
        // Reabastecer y vaciar de nuevo
        silkRoad.resupplyStores();
        silkRoad.moveRobot(3, 1); // Mover fuera
        silkRoad.moveRobot(4, -1); // Regresar
        assertEquals(2, silkRoad.consultStoreEmptyCount(3));
    }
    
    @Test
    public void shouldGetAllStoreEmptyCounts() {
        silkRoad = new SilkRoad(10);
        silkRoad.placeStore(2, 50);
        silkRoad.placeStore(5, 100);
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0, 2); // Vaciar tienda en 2
        silkRoad.moveRobot(2, 3); // Vaciar tienda en 5
        
        Map<Integer, Integer> counts = silkRoad.consultStoreEmptyCounts();
        
        assertEquals(2, counts.size());
        assertEquals(Integer.valueOf(1), counts.get(2));
        assertEquals(Integer.valueOf(1), counts.get(5));
    }
    
    @Test
    public void shouldNotCountPartialStoreEmpty() {
        silkRoad = new SilkRoad(10);
        silkRoad.placeStore(3, 100);
        silkRoad.placeRobot(0);
        
        // Si implementas toma parcial, ajustar esta prueba
        silkRoad.moveRobot(0, 3);
        
        assertEquals(1, silkRoad.consultStoreEmptyCount(3));
    }
    
    // ========== PRUEBAS REQUISITO 13: CONSULTAR GANANCIAS DE ROBOTS ==========
    
    @Test
    public void shouldTrackRobotGains() {
        silkRoad = new SilkRoad(15);
        silkRoad.placeStore(3, 100);
        silkRoad.placeStore(7, 200);
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0, 3); // Ganancia: 100
        silkRoad.moveRobot(3, 4); // Ganancia: 200
        
        List<Integer> gains = silkRoad.consultRobotGains(7);
        
        assertEquals(2, gains.size());
        assertEquals(Integer.valueOf(100), gains.get(0));
        assertEquals(Integer.valueOf(200), gains.get(1));
    }
    
    @Test
    public void shouldTrackAllRobotGains() {
        silkRoad = new SilkRoad(20);
        silkRoad.placeStore(5, 100);
        silkRoad.placeStore(15, 200);
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(10);
        
        silkRoad.moveRobot(0, 5);  // Robot en 0 gana 100
        silkRoad.moveRobot(10, 5); // Robot en 10 gana 200
        
        Map<Integer, List<Integer>> allGains = silkRoad.consultRobotGains();
        
        assertEquals(2, allGains.size());
        assertTrue(allGains.containsKey(5));  // Robot ahora en posición 5
        assertTrue(allGains.containsKey(15)); // Robot ahora en posición 15
    }
    
    @Test
    public void shouldTrackZeroGainsWhenStoreEmpty() {
        silkRoad = new SilkRoad(10);
        silkRoad.placeStore(3, 0); // Tienda vacía
        silkRoad.placeRobot(0);
        
        silkRoad.moveRobot(0, 3);
        
        List<Integer> gains = silkRoad.consultRobotGains(3);
        assertTrue(gains.isEmpty() || (gains.size() == 1 && gains.get(0) == 0));
    }
    
    // ========== PRUEBAS DE INTEGRACIÓN ==========
    
    @Test
    public void shouldHandleCompleteScenario() {
        // Crear desde entrada de maratón
        String input = "20 4 3\n" +
                      "5 100\n" +
                      "10 200\n" +
                      "15 150\n" +
                      "18 300\n" +
                      "0\n" +
                      "7\n" +
                      "19\n";
        
        silkRoad = new SilkRoad(input);
        
        // Mover robots inteligentemente
        silkRoad.moveAllRobots();
        assertTrue(silkRoad.profit() > 0);
        
        // Consultar estadísticas
        Map<Integer, Integer> storeEmptyCounts = silkRoad.consultStoreEmptyCounts();
        assertNotNull(storeEmptyCounts);
        
        Map<Integer, List<Integer>> robotGains = silkRoad.consultRobotGains();
        assertNotNull(robotGains);
        
        // Reabastecer y continuar
        silkRoad.resupplyStores();
        silkRoad.moveAllRobots();
        
        assertTrue(silkRoad.ok());
    }
    
    @Test
    public void shouldMaintainConsistencyAfterReboot() {
        silkRoad = new SilkRoad(15);
        silkRoad.placeStore(5, 100);
        silkRoad.placeStore(10, 200);
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(14);
        
        // Realizar movimientos y obtener ganancias
        silkRoad.moveRobot(0, 5);
        silkRoad.moveRobot(14, -4);
        int profitBeforeReboot = silkRoad.profit();
        
        // Reiniciar
        silkRoad.reboot();
        
        // Verificar estado reiniciado
        assertEquals(0, silkRoad.profit());
        assertEquals(2, silkRoad.stores());
        assertEquals(2, silkRoad.robots());
        
        // Las estadísticas deberían estar limpias
        Map<Integer, Integer> counts = silkRoad.consultStoreEmptyCounts();
        for (Integer count : counts.values()) {
            assertEquals(Integer.valueOf(0), count);
        }
    }
    
    // ========== PRUEBAS DE CASOS EXTREMOS ==========
    
    @Test
    public void shouldHandleEmptyRoad() {
        silkRoad = new SilkRoad(5);
        
        // No hay tiendas ni robots
        silkRoad.moveAllRobots();
        assertTrue(silkRoad.ok());
        
        Map<Integer, Integer> emptyCounts = silkRoad.consultStoreEmptyCounts();
        assertTrue(emptyCounts.isEmpty());
        
        Map<Integer, List<Integer>> gains = silkRoad.consultRobotGains();
        assertTrue(gains.isEmpty());
    }
    
    @Test
    public void shouldHandleMaximumCapacity() {
        // Crear ruta muy larga
        silkRoad = new SilkRoad(1000);
        
        // Agregar muchas tiendas y robots
        for (int i = 0; i < 100; i += 10) {
            silkRoad.placeStore(i, i * 10);
        }
        
        for (int i = 5; i < 100; i += 20) {
            silkRoad.placeRobot(i);
        }
        
        assertTrue(silkRoad.ok());
        assertEquals(10, silkRoad.stores());
        assertEquals(5, silkRoad.robots());
    }
}