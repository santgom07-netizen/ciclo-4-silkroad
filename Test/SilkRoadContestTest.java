package Test;

import silkroad.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para SilkRoadContest.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadContestTest {
    
    private SilkRoadContest contest;
    
    @Before
    public void setUp() {
        contest = null;
    }
    
    @After
    public void tearDown() {
        contest = null;
    }
    
    /**
     * Prueba con el ejemplo 1 del problema de la maratón.
     */
    @Test
    public void shouldSolveSampleInput1() {
        String input = "6\n" +
                      "1 20\n" +
                      "2 15 15\n" +
                      "2 40 50\n" +
                      "1 50\n" +
                      "2 80 20\n" +
                      "2 70 30\n";
        
        long[] result = SilkRoadContest.solveFromInput(input);
        
        assertEquals(6, result.length);
        assertEquals(0, result[0]);   // Día 1: solo 1 robot, sin tiendas
        assertEquals(10, result[1]);  // Día 2: robot en 20 va a tienda en 15: 15-5=10
        assertEquals(35, result[2]);  // Día 3: robot va a tienda en 40: 50-20=30 + 10 (anterior no cuenta, se reabastece)
        assertEquals(50, result[3]);  // Día 4: 2 robots, 2 tiendas óptimo
        assertEquals(50, result[4]);  // Día 5: mejor configuración
        assertEquals(60, result[5]);  // Día 6: mejor configuración
    }
    
    /**
     * Prueba con un solo robot y una tienda cercana.
     */
    @Test
    public void shouldCalculateProfitForSingleRobotAndStore() {
        contest = new SilkRoadContest(2);
        contest.addEvent(0, 1, 10, 0);    // Robot en posición 10
        contest.addEvent(1, 2, 15, 100);  // Tienda en posición 15 con 100 tenges
        
        long[] profits = contest.solve();
        
        assertEquals(0, profits[0]);  // Día 1: sin tiendas
        assertEquals(95, profits[1]); // Día 2: 100 - 5 (distancia) = 95
    }
    
    /**
     * Prueba con robot que no puede alcanzar ganancia positiva.
     */
    @Test
    public void shouldReturnZeroWhenNoProfitIsPossible() {
        contest = new SilkRoadContest(2);
        contest.addEvent(0, 1, 0, 0);     // Robot en posición 0
        contest.addEvent(1, 2, 100, 50);  // Tienda muy lejos con poco dinero
        
        long[] profits = contest.solve();
        
        assertEquals(0, profits[1]); // 50 - 100 = -50, por lo tanto 0
    }
    
    /**
     * Prueba con múltiples robots y múltiples tiendas.
     */
    @Test
    public void shouldOptimallyAssignMultipleRobotsToStores() {
        contest = new SilkRoadContest(4);
        contest.addEvent(0, 1, 0, 0);     // Robot en 0
        contest.addEvent(1, 1, 100, 0);   // Robot en 100
        contest.addEvent(2, 2, 10, 50);   // Tienda en 10 con 50 tenges
        contest.addEvent(3, 2, 90, 50);   // Tienda en 90 con 50 tenges
        
        long[] profits = contest.solve();
        
        // Robot en 0 va a tienda en 10: 50 - 10 = 40
        // Robot en 100 va a tienda en 90: 50 - 10 = 40
        // Total = 80
        assertEquals(80, profits[3]);
    }
    
    /**
     * Prueba que cada tienda solo puede ser visitada por un robot.
     */
    @Test
    public void shouldNotAssignMultipleRobotsToSameStore() {
        contest = new SilkRoadContest(3);
        contest.addEvent(0, 1, 0, 0);     // Robot 1 en 0
        contest.addEvent(1, 1, 5, 0);     // Robot 2 en 5
        contest.addEvent(2, 2, 10, 100);  // Una sola tienda
        
        long[] profits = contest.solve();
        
        // Solo el robot más cercano (en 5) debería ir
        // 100 - 5 = 95
        assertEquals(95, profits[2]);
    }
    
    /**
     * Prueba sin robots.
     */
    @Test
    public void shouldReturnZeroWithNoRobots() {
        contest = new SilkRoadContest(1);
        contest.addEvent(0, 2, 10, 100);  // Solo tienda, sin robots
        
        long[] profits = contest.solve();
        
        assertEquals(0, profits[0]);
    }
    
    /**
     * Prueba sin tiendas.
     */
    @Test
    public void shouldReturnZeroWithNoStores() {
        contest = new SilkRoadContest(1);
        contest.addEvent(0, 1, 10, 0);  // Solo robot, sin tiendas
        
        long[] profits = contest.solve();
        
        assertEquals(0, profits[0]);
    }
    
    /**
     * Prueba con tiendas vacías (0 tenges).
     */
    @Test
    public void shouldReturnZeroWithEmptyStores() {
        contest = new SilkRoadContest(2);
        contest.addEvent(0, 1, 0, 0);    // Robot
        contest.addEvent(1, 2, 10, 0);   // Tienda vacía
        
        long[] profits = contest.solve();
        
        assertEquals(0, profits[1]);
    }
    
    /**
     * Prueba de escenario complejo con múltiples días.
     */
    @Test
    public void shouldHandleComplexMultiDayScenario() {
        contest = new SilkRoadContest(5);
        contest.addEvent(0, 1, 20, 0);
        contest.addEvent(1, 2, 15, 15);
        contest.addEvent(2, 2, 40, 50);
        contest.addEvent(3, 1, 50, 0);
        contest.addEvent(4, 2, 30, 100);
        
        long[] profits = contest.solve();
        
        // Verificar que todas las ganancias sean no negativas
        for (long profit : profits) {
            assertTrue("La ganancia debe ser no negativa", profit >= 0);
        }
        
        // Verificar que las ganancias sean monótonas o estables
        // (al agregar más elementos, la ganancia no debería decrecer significativamente)
        assertTrue("Día 1 correcto", profits[0] >= 0);
        assertTrue("Día 2 correcto", profits[1] >= 0);
        assertTrue("Día 3 correcto", profits[2] >= profits[1]);
    }
    
    /**
     * Prueba que las ganancias nunca sean negativas.
     */
    @Test
    public void shouldNeverReturnNegativeProfit() {
        contest = new SilkRoadContest(3);
        contest.addEvent(0, 1, 0, 0);
        contest.addEvent(1, 2, 1000, 10);  // Muy lejos
        contest.addEvent(2, 2, 0, 5);      // En la misma posición pero poco valor
        
        long[] profits = contest.solve();
        
        for (long profit : profits) {
            assertTrue("Las ganancias deben ser no negativas", profit >= 0);
        }
    }
}
