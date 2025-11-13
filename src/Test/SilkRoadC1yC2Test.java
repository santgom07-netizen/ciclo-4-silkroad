package Test;

import silkroad.*;
import shapes.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Clase de pruebas unitarias completas para SilkRoad - Ciclo 1 y Ciclo 2.
 * Incluye pruebas para TODOS los métodos de los requisitos funcionales.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadC1yC2Test {
    
    private SilkRoad silkRoad;
    
    @Before
    public void setUp() {
        // Inicializa silkRoad a null antes de cada prueba
        silkRoad = null;
    }
    
    @After
    public void tearDown() {
        // Limpia el simulador y lo hace invisible después de cada prueba
        if (silkRoad != null) {
            silkRoad.makeInvisible();
            silkRoad = null;
        }
    }
    
    @Test
    public void shouldCreateSilkRoadWithValidLength() {
        // Crea un simulador con longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar GUI
        silkRoad.makeInvisible();
        // Verifica que la longitud es correcta
        assertEquals(20, silkRoad.length());
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidLength() {
        // Intenta crear simulador con longitud 0 (inválido)
        silkRoad = new SilkRoad(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNegativeLength() {
        // Intenta crear simulador con longitud negativa (inválido)
        silkRoad = new SilkRoad(-5);
    }
    
    @Test
    public void shouldPlaceStoreSuccessfully() {
        // Crea simulador de longitud 10
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda en posición 5 con 100 tenges
        silkRoad.placeStore(5, 100);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Obtiene información de tiendas
        int[][] stores = silkRoad.stores();
        // Verifica cantidad de tiendas
        assertEquals(1, stores.length);
        // Verifica ubicación
        assertEquals(5, stores[0][0]);
        // Verifica dinero
        assertEquals(100, stores[0][1]);
    }
    
    @Test
    public void shouldNotPlaceStoreOutOfBounds() {
        // Crea simulador de longitud 10
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Intenta colocar tienda fuera de rango (ubicación 10 >= longitud 10)
        silkRoad.placeStore(10, 100);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
        // Verifica que no se agregó la tienda
        assertEquals(0, silkRoad.stores().length);
    }
    
    @Test
    public void shouldNotPlaceStoreWithNegativeTenges() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Intenta colocar tienda con dinero negativo (inválido)
        silkRoad.placeStore(5, -100);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
        // Verifica que no se agregó la tienda
        assertEquals(0, silkRoad.stores().length);
    }
    
    @Test
    public void shouldNotPlaceDuplicateStore() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda en posición 5
        silkRoad.placeStore(5, 100);
        // Intenta colocar otra tienda en la misma posición
        silkRoad.placeStore(5, 200);
        
        // Verifica que falló el segundo
        assertFalse(silkRoad.ok());
        // Verifica que solo hay 1 tienda
        assertEquals(1, silkRoad.stores().length);
    }
    
    @Test
    public void shouldRemoveExistingStore() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda
        silkRoad.placeStore(5, 100);
        // Elimina la tienda
        silkRoad.removeStore(5);
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica que no hay tiendas
        assertEquals(0, silkRoad.stores().length);
    }
    
    @Test
    public void shouldNotRemoveNonExistentStore() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Intenta eliminar tienda que no existe
        silkRoad.removeStore(5);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
    }
    
    @Test
    public void shouldPlaceRobotSuccessfully() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot en posición 3
        silkRoad.placeRobot(3);
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Obtiene información de robots
        int[][] robots = silkRoad.robots();
        // Verifica cantidad
        assertEquals(1, robots.length);
        // Verifica ubicación
        assertEquals(3, robots[0][0]);
        // Verifica ganancia inicial (0)
        assertEquals(0, robots[0][1]);
    }
    
    @Test
    public void shouldNotPlaceRobotOutOfBounds() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Intenta colocar robot fuera de rango
        silkRoad.placeRobot(15);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
        // Verifica que no se agregó
        assertEquals(0, silkRoad.robots().length);
    }
    
    @Test
    public void shouldNotPlaceDuplicateRobot() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot en posición 3
        silkRoad.placeRobot(3);
        // Intenta colocar otro robot en la misma posición
        silkRoad.placeRobot(3);
        
        // Verifica que falló el segundo
        assertFalse(silkRoad.ok());
        // Verifica que solo hay 1 robot
        assertEquals(1, silkRoad.robots().length);
    }
    
    @Test
    public void shouldRemoveExistingRobot() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot
        silkRoad.placeRobot(3);
        // Elimina el robot
        silkRoad.removeRobot(3);
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica que no hay robots
        assertEquals(0, silkRoad.robots().length);
    }
    
    @Test
    public void shouldNotRemoveNonExistentRobot() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Intenta eliminar robot que no existe
        silkRoad.removeRobot(3);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
    }
    
    @Test
    public void shouldMoveRobotManually() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot en posición 0
        silkRoad.placeRobot(0);
        // Mueve robot 5 pasos (de 0 a 5)
        silkRoad.moveRobot(0, 5);
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica nueva posición
        assertEquals(5, silkRoad.robots()[0][0]);
    }
    
    @Test
    public void shouldCollectTengesWhenMovingToStore() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda en posición 5 con 100 tenges
        silkRoad.placeStore(5, 100);
        // Coloca robot en posición 0
        silkRoad.placeRobot(0);
        // Mueve robot a la tienda (de 0 a 5)
        silkRoad.moveRobot(0, 5);
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica que robot tiene los tenges
        assertEquals(100, silkRoad.robots()[0][1]);
        // Verifica que tienda quedó vacía
        assertEquals(0, silkRoad.stores()[0][1]);
    }
    
    @Test
    public void shouldNotMoveRobotOutOfBounds() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot en posición 5
        silkRoad.placeRobot(5);
        // Intenta mover robot fuera del rango (10 pasos desde 5 = 15 >= 10)
        silkRoad.moveRobot(5, 10);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
        // Verifica que robot se quedó en posición original
        assertEquals(5, silkRoad.robots()[0][0]);
    }
    
    @Test
    public void shouldNotMoveRobotToOccupiedPosition() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot 1 en posición 3
        silkRoad.placeRobot(3);
        // Coloca robot 2 en posición 5
        silkRoad.placeRobot(5);
        // Intenta mover robot 1 a posición 5 (ocupada)
        silkRoad.moveRobot(3, 2);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
        // Verifica que robot 1 se quedó en posición original
        assertEquals(3, silkRoad.robots()[0][0]);
    }
    
    @Test
    public void shouldResupplyStores() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda con 100 tenges
        silkRoad.placeStore(5, 100);
        // Coloca robot
        silkRoad.placeRobot(0);
        // Mueve robot a tienda (se vacía)
        silkRoad.moveRobot(0, 5);
        
        // Verifica que tienda está vacía
        assertEquals(0, silkRoad.stores()[0][1]);
        
        // Reabastece tiendas
        silkRoad.resupplyStores();
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica que tienda tiene sus tenges de nuevo
        assertEquals(100, silkRoad.stores()[0][1]);
    }
    
    @Test
    public void shouldReturnRobotsToInitialPosition() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca robot 1 en posición 0
        silkRoad.placeRobot(0);
        // Coloca robot 2 en posición 9
        silkRoad.placeRobot(9);
        
        // Mueve robot 1 cinco pasos
        silkRoad.moveRobot(0, 5);
        // Mueve robot 2 tres pasos hacia atrás
        silkRoad.moveRobot(9, -3);
        
        // Devuelve todos a posiciones iniciales
        silkRoad.returnRobots();
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica robot 1 en posición inicial
        assertEquals(0, silkRoad.robots()[0][0]);
        // Verifica robot 2 en posición inicial
        assertEquals(9, silkRoad.robots()[1][0]);
    }
    
    @Test
    public void shouldRebootSimulator() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda
        silkRoad.placeStore(5, 100);
        // Coloca robot
        silkRoad.placeRobot(0);
        
        // Mueve robot a tienda
        silkRoad.moveRobot(0, 5);
        // Verifica estado después del movimiento
        assertEquals(100, silkRoad.robots()[0][1]);
        assertEquals(0, silkRoad.stores()[0][1]);
        
        // Reinicia todo
        silkRoad.reboot();
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica robot en posición inicial
        assertEquals(0, silkRoad.robots()[0][0]);
        // Verifica ganancia reiniciada
        assertEquals(0, silkRoad.robots()[0][1]);
        // Verifica tienda reabastecida
        assertEquals(100, silkRoad.stores()[0][1]);
    }
    
    @Test
    public void shouldCalculateMaxProfit() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda 1 con 100
        silkRoad.placeStore(3, 100);
        // Coloca tienda 2 con 200
        silkRoad.placeStore(5, 200);
        // Coloca robot
        silkRoad.placeRobot(0);
        
        // Verifica que ganancia máxima es 300 (100 + 200)
        assertEquals(300, silkRoad.profit());
        
        // Mueve robot a primera tienda
        silkRoad.moveRobot(0, 3);
        // Verifica que ganancia máxima sigue siendo 300
        assertEquals(300, silkRoad.profit());
    }
    
    @Test
    public void shouldReturnStoresInfo() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda 1
        silkRoad.placeStore(3, 100);
        // Coloca tienda 2
        silkRoad.placeStore(7, 200);
        
        // Obtiene información de tiendas
        int[][] stores = silkRoad.stores();
        
        // Verifica cantidad
        assertEquals(2, stores.length);
        // Verifica tienda 1
        assertEquals(3, stores[0][0]);
        assertEquals(100, stores[0][1]);
        // Verifica tienda 2
        assertEquals(7, stores[1][0]);
        assertEquals(200, stores[1][1]);
    }
    
    @Test
    public void shouldReturnRobotsInfo() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda
        silkRoad.placeStore(5, 100);
        // Coloca robot 1
        silkRoad.placeRobot(0);
        // Coloca robot 2
        silkRoad.placeRobot(9);
        
        // Mueve robot 1 a tienda
        silkRoad.moveRobot(0, 5);
        
        // Obtiene información de robots
        int[][] robots = silkRoad.robots();
        
        // Verifica cantidad
        assertEquals(2, robots.length);
        // Verifica robot 1 (en tienda con ganancia)
        assertEquals(5, robots[0][0]);
        assertEquals(100, robots[0][1]);
        // Verifica robot 2 (no se movió)
        assertEquals(9, robots[1][0]);
        assertEquals(0, robots[1][1]);
    }
    
    @Test
    public void shouldReturnOkStatus() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Verifica ok inicial
        assertTrue(silkRoad.ok());
        
        // Intenta operación inválida (fuera de rango)
        silkRoad.placeStore(15, 100);
        // Verifica que ok es false
        assertFalse(silkRoad.ok());
        
        // Hace operación válida
        silkRoad.placeStore(5, 100);
        // Verifica que ok es true
        assertTrue(silkRoad.ok());
    }
    
    @Test
    public void shouldCreateFromMarathonInput() {
        // Crea simulador desde entrada de maratón
        String input = "15 3 2\n5 100\n10 200\n14 150\n0\n7\n";
        silkRoad = new SilkRoad(input);
        // Lo hace invisible
        silkRoad.makeInvisible();
        
        // Verifica longitud
        assertEquals(15, silkRoad.length());
        // Verifica tiendas
        assertEquals(3, silkRoad.stores().length);
        // Verifica robots
        assertEquals(2, silkRoad.robots().length);
    }
    
    @Test
    public void shouldCreateFromDaysArray() {
        // Crea simulador desde array de días
        int[][] days = {
            {15, 3, 2},
            {5, 100},
            {10, 200},
            {14, 150},
            {0},
            {7}
        };
        silkRoad = new SilkRoad(days);
        // Lo hace invisible
        silkRoad.makeInvisible();
        
        // Verifica longitud
        assertEquals(15, silkRoad.length());
        // Verifica tiendas
        assertEquals(3, silkRoad.stores().length);
        // Verifica robots
        assertEquals(2, silkRoad.robots().length);
    }
    
    @Test
    public void shouldMoveRobotOptimally() {
        // Crea simulador
        silkRoad = new SilkRoad(20);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda 1 (peor ratio)
        silkRoad.placeStore(5, 100);
        // Coloca tienda 2 (mejor ratio)
        silkRoad.placeStore(15, 500);
        // Coloca robot
        silkRoad.placeRobot(0);
        
        // Mueve robot óptimamente
        silkRoad.moveRobot(0);
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica que fue a tienda mejor (15)
        assertEquals(15, silkRoad.robots()[0][0]);
        // Verifica que recogió dinero
        assertEquals(500, silkRoad.robots()[0][1]);
    }
    
    @Test
    public void shouldNotMoveRobotWhenNoStoresAvailable() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda vacía
        silkRoad.placeStore(5, 0);
        // Coloca robot
        silkRoad.placeRobot(0);
        
        // Intenta mover robot óptimamente (sin tiendas disponibles)
        silkRoad.moveRobot(0);
        
        // Verifica que falló
        assertFalse(silkRoad.ok());
        // Verifica que robot no se movió
        assertEquals(0, silkRoad.robots()[0][0]);
    }
    
    @Test
    public void shouldMoveAllRobotsOptimally() {
        // Crea simulador
        silkRoad = new SilkRoad(20);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda 1
        silkRoad.placeStore(5, 100);
        // Coloca tienda 2
        silkRoad.placeStore(15, 200);
        // Coloca robot 1 en inicio
        silkRoad.placeRobot(0);
        // Coloca robot 2 en final
        silkRoad.placeRobot(19);
        
        // Mueve todos los robots óptimamente
        silkRoad.moveRobots();
        
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica que robot 1 fue a tienda cercana
        assertEquals(5, silkRoad.robots()[0][0]);
        // Verifica que robot 2 fue a tienda cercana
        assertEquals(15, silkRoad.robots()[1][0]);
    }
    
    @Test
    public void shouldHandleMultipleRobotsCompetingForSameStore() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca una sola tienda
        silkRoad.placeStore(5, 100);
        // Coloca robot 1 cerca (posición 4)
        silkRoad.placeRobot(4);
        // Coloca robot 2 cerca (posición 6)
        silkRoad.placeRobot(6);
        
        // Intenta mover ambos robots
        silkRoad.moveRobots();
        
        // Cuenta cuántos robots llegaron a la tienda
        int robotsInStore = 0;
        for (int[] robot : silkRoad.robots()) {
            if (robot[0] == 5) robotsInStore++;
        }
        // Verifica que solo uno logró entrar
        assertEquals(1, robotsInStore);
    }
    
    @Test
    public void shouldTrackEmptiedStores() {
        // Crea simulador
        silkRoad = new SilkRoad(10);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda
        silkRoad.placeStore(5, 100);
        // Coloca robot
        silkRoad.placeRobot(0);
        
        // Obtiene info de tiendas vaciadas (antes de movimiento)
        int[][] emptied = silkRoad.emptiedStores();
        assertEquals(5, emptied[0][0]);
        assertEquals(0, emptied[0][1]);
        
        // Mueve robot a tienda (se vacía)
        silkRoad.moveRobot(0, 5);
        // Obtiene info de tiendas vaciadas (después de primer movimiento)
        emptied = silkRoad.emptiedStores();
        assertEquals(1, emptied[0][1]);
        
        // Reabastece y devuelve robot
        silkRoad.resupplyStores();
        silkRoad.returnRobots();
        // Mueve robot a tienda de nuevo (se vacía segunda vez)
        silkRoad.moveRobot(0, 5);
        
        // Obtiene info de tiendas vaciadas (después del segundo movimiento)
        emptied = silkRoad.emptiedStores();
        assertEquals(2, emptied[0][1]);
    }
    
    @Test
    public void shouldTrackProfitPerMove() {
        // Crea simulador
        silkRoad = new SilkRoad(15);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda 1
        silkRoad.placeStore(5, 100);
        // Coloca tienda 2
        silkRoad.placeStore(10, 200);
        // Coloca robot 1
        silkRoad.placeRobot(0);
        // Coloca robot 2
        silkRoad.placeRobot(14);
        
        // Mueve robot 1 a tienda 1
        silkRoad.moveRobot(0, 5);
        // Mueve robot 2 a tienda 2
        silkRoad.moveRobot(14, -4);
        
        // Obtiene historial de ganancias por movimiento
        int[][] profits = silkRoad.profitPerMove();
        
        // Verifica cantidad de robots
        assertEquals(2, profits.length);
        // Verifica ganancia de robot 1
        assertEquals(100, profits[0][0]);
        // Verifica ganancia de robot 2
        assertEquals(200, profits[1][0]);
    }
    
    @Test
    public void shouldHandleSingleLocationRoad() {
        // Crea simulador con longitud 1
        silkRoad = new SilkRoad(1);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda
        silkRoad.placeStore(0, 100);
        // Coloca robot
        silkRoad.placeRobot(0);
        
        // Verifica que robot comienza sin ganancia
        assertEquals(0, silkRoad.robots()[0][1]);
        
        // Intenta mover robot fuera del rango
        silkRoad.moveRobot(0, 1);
        // Verifica que falló
        assertFalse(silkRoad.ok());
    }
    
    @Test
    public void shouldHandleLargeRoad() {
        // Crea simulador con longitud grande
        silkRoad = new SilkRoad(1000);
        // Lo hace invisible
        silkRoad.makeInvisible();
        // Coloca tienda al final
        silkRoad.placeStore(999, 1000);
        // Coloca robot al inicio
        silkRoad.placeRobot(0);
        
        // Mueve robot al final
        silkRoad.moveRobot(0, 999);
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
        // Verifica posición final
        assertEquals(999, silkRoad.robots()[0][0]);
        // Verifica ganancia
        assertEquals(1000, silkRoad.robots()[0][1]);
    }
    
    @Test
    public void shouldHandleMaximumStoresAndRobots() {
        // Crea simulador
        silkRoad = new SilkRoad(100);
        // Lo hace invisible
        silkRoad.makeInvisible();
        
        // Coloca 50 tiendas
        for (int i = 0; i < 50; i++) {
            silkRoad.placeStore(i, 100 + i);
        }
        
        // Coloca 50 robots
        for (int i = 50; i < 100; i++) {
            silkRoad.placeRobot(i);
        }
        
        // Verifica cantidad de tiendas
        assertEquals(50, silkRoad.stores().length);
        // Verifica cantidad de robots
        assertEquals(50, silkRoad.robots().length);
        
        // Mueve todos los robots óptimamente
        silkRoad.moveRobots();
        // Verifica que fue exitoso
        assertTrue(silkRoad.ok());
    }
}
