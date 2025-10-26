package Test;

import silkroad.*;
import shapes.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Pruebas unitarias para SilkRoadCiclo4.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadC4Test {
    
    private SilkRoad silkRoad;
    
    @Before
    public void setUp() {
        // Inicializa el simulador a null antes de cada prueba
        silkRoad = null;
    }
    
    @After
    public void tearDown() {
        // Limpia el simulador después de cada prueba
        silkRoad = null;
    }
    
    /**
     * Prueba creación de tienda normal.
     */
    @Test
    public void shouldCreateNormalStore() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda normal en la ubicación 10 con 100 tenges
        silkRoad.placeStore("normal", 10, 100);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 tienda
        assertEquals(1, silkRoad.stores().length);
        // Verifica que la tienda está en la ubicación correcta
        assertEquals(10, silkRoad.stores()[0][0]);
        // Verifica que la tienda tiene la cantidad correcta de tenges
        assertEquals(100, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba creación de tienda autónoma.
     */
    @Test
    public void shouldCreateAutonomousStore() {
        // Crea un simulador de longitud 50
        silkRoad = new SilkRoad(50);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda autónoma (puede cambiar de ubicación)
        silkRoad.placeStore("autonomous", 25, 100);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Obtiene la ubicación actual de la tienda autónoma
        int actualLocation = silkRoad.stores()[0][0];
        // Verifica que la ubicación está dentro del rango válido
        assertTrue("Ubicacion debe estar en rango valido", actualLocation >= 0 && actualLocation < 50);
    }
    
    /**
     * Prueba creación de tienda fighter.
     */
    @Test
    public void shouldCreateFighterStore() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda fighter (rechaza robots débiles)
        silkRoad.placeStore("fighter", 10, 100);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 tienda
        assertEquals(1, silkRoad.stores().length);
    }
    
    /**
     * Prueba creación de tienda generous.
     */
    @Test
    public void shouldCreateGenerousStore() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda generous (duplica dinero)
        silkRoad.placeStore("generous", 10, 100);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 tienda
        assertEquals(1, silkRoad.stores().length);
    }
    
    /**
     * Prueba creación de robot normal.
     */
    @Test
    public void shouldCreateNormalRobot() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca un robot normal en la ubicación 5
        silkRoad.placeRobot("normal", 5);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 robot
        assertEquals(1, silkRoad.robots().length);
    }
    
    /**
     * Prueba creación de robot neverback.
     */
    @Test
    public void shouldCreateNeverBackRobot() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca un robot neverback (no vuelve a posición inicial)
        silkRoad.placeRobot("neverback", 5);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 robot
        assertEquals(1, silkRoad.robots().length);
    }
    
    /**
     * Prueba creación de robot tender.
     */
    @Test
    public void shouldCreateTenderRobot() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca un robot tender (toma la mitad del dinero)
        silkRoad.placeRobot("tender", 5);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 robot
        assertEquals(1, silkRoad.robots().length);
    }
    
    /**
     * Prueba creación de robot greedy.
     */
    @Test
    public void shouldCreateGreedyRobot() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca un robot greedy (tipo especial de robot)
        silkRoad.placeRobot("greedy", 5);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 robot
        assertEquals(1, silkRoad.robots().length);
    }
    
    /**
     * Prueba que cualquier robot puede tomar dinero de tienda normal.
     */
    @Test
    public void shouldAllowAnyRobotToTakeFromNormalStore() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda normal en ubicación 10 con 100 tenges
        silkRoad.placeStore("normal", 10, 100);
        // Coloca un robot normal en ubicación 5
        silkRoad.placeRobot("normal", 5);
        
        // Mueve el robot 5 pasos (de 5 a 10, donde está la tienda)
        silkRoad.moveRobot(5, 5);
        
        // Verifica que el robot obtuvo los 100 tenges
        assertEquals(100, silkRoad.robots()[0][1]);
        // Verifica que la tienda quedó vacía
        assertEquals(0, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba que tienda fighter rechaza robot sin suficiente dinero.
     */
    @Test
    public void shouldFighterStoreRejectRobotWithLessMoney() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda fighter en ubicación 10 con 100 tenges
        silkRoad.placeStore("fighter", 10, 100);
        // Coloca un robot normal (sin dinero) en ubicación 5
        silkRoad.placeRobot("normal", 5);
        
        // Intenta mover el robot a la tienda fighter
        silkRoad.moveRobot(5, 5);
        
        // Verifica que el robot no obtuvo dinero (rechazado)
        assertEquals(0, silkRoad.robots()[0][1]);
        // Verifica que la tienda mantiene su dinero
        assertEquals(100, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba que tienda fighter acepta robot con suficiente dinero.
     */
    @Test
    public void shouldFighterStoreAllowRobotWithMoreMoney() {
        // Crea un simulador de longitud 30
        silkRoad = new SilkRoad(30);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca tienda normal en ubicación 5 con 150 tenges
        silkRoad.placeStore("normal", 5, 150);
        // Coloca tienda fighter en ubicación 15 con 100 tenges
        silkRoad.placeStore("fighter", 15, 100);
        // Coloca robot normal en ubicación 0
        silkRoad.placeRobot("normal", 0);
        
        // Mueve robot de 0 a 5 (recoge 150 tenges)
        silkRoad.moveRobot(0, 5);
        // Verifica que el robot tiene 150 tenges
        assertEquals(150, silkRoad.robots()[0][1]);
        
        // Mueve robot de 5 a 15 (15 > 5, ahora puede entrar a fighter)
        silkRoad.moveRobot(5, 10);
        // Obtiene la ganancia final del robot
        int finalGain = silkRoad.robots()[0][1];
        // Verifica que el robot recogió ambas cantidades (150 + 100)
        assertEquals(250, finalGain);
        // Verifica que la tienda fighter quedó vacía
        assertEquals(0, silkRoad.stores()[1][1]);
    }
    
    /**
     * Prueba que tienda generous duplica el dinero.
     */
    @Test
    public void shouldGenerousStoreGiveDoubleMoney() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda generous en ubicación 10 con 100 tenges
        silkRoad.placeStore("generous", 10, 100);
        // Coloca un robot normal en ubicación 5
        silkRoad.placeRobot("normal", 5);
        
        // Mueve el robot a la tienda generous
        silkRoad.moveRobot(5, 5);
        
        // Verifica que el robot recibió el doble (100 * 2 = 200)
        assertEquals(200, silkRoad.robots()[0][1]);
        // Verifica que la tienda quedó vacía
        assertEquals(0, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba que robot tender solo toma la mitad del dinero.
     */
    @Test
    public void shouldTenderRobotTakeHalfMoney() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda normal en ubicación 10 con 100 tenges
        silkRoad.placeStore("normal", 10, 100);
        // Coloca un robot tender en ubicación 5
        silkRoad.placeRobot("tender", 5);
        
        // Mueve el robot tender a la tienda
        silkRoad.moveRobot(5, 5);
        
        // Verifica que el robot tender tomó la mitad (100 / 2 = 50)
        assertEquals(50, silkRoad.robots()[0][1]);
        // Verifica que la tienda dejó dinero (la otra mitad)
        assertEquals(50, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba que robot neverback no vuelve a posición inicial.
     */
    @Test
    public void shouldNeverBackRobotNotReturnToInitial() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca un robot neverback en ubicación 5
        silkRoad.placeRobot("neverback", 5);
        // Mueve el robot 10 pasos (de 5 a 15)
        silkRoad.moveRobot(5, 10);
        
        // Verifica que el robot está en la nueva ubicación (15)
        assertEquals(15, silkRoad.robots()[0][0]);
        
        // Intenta devolver todos los robots a posiciones iniciales
        silkRoad.returnRobots();
        
        // Verifica que el robot neverback se quedó donde estaba (no volvió)
        assertEquals(15, silkRoad.robots()[0][0]);
    }
    
    /**
     * Prueba que robot normal sí vuelve a posición inicial.
     */
    @Test
    public void shouldNormalRobotReturnToInitialPosition() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca un robot normal en ubicación 5
        silkRoad.placeRobot("normal", 5);
        // Mueve el robot 10 pasos (de 5 a 15)
        silkRoad.moveRobot(5, 10);
        
        // Verifica que el robot está en la nueva ubicación (15)
        assertEquals(15, silkRoad.robots()[0][0]);
        
        // Devuelve todos los robots a posiciones iniciales
        silkRoad.returnRobots();
        
        // Verifica que el robot normal volvió a su posición inicial (5)
        assertEquals(5, silkRoad.robots()[0][0]);
    }
    
    /**
     * Prueba interacción de robot greedy con tienda generous.
     */
    @Test
    public void shouldHandleGreedyRobotWithGenerousStore() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda generous en ubicación 10 con 100 tenges
        silkRoad.placeStore("generous", 10, 100);
        // Coloca un robot greedy en ubicación 5
        silkRoad.placeRobot("greedy", 5);
        
        // Mueve el robot greedy a la tienda generous
        silkRoad.moveRobot(5, 5);
        
        // Verifica que el robot recibió el dinero duplicado (100 * 2 = 200)
        assertEquals(200, silkRoad.robots()[0][1]);
        // Verifica que la tienda quedó vacía
        assertEquals(0, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba interacción de robot tender con tienda generous.
     */
    @Test
    public void shouldHandleTenderRobotWithGenerousStore() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda generous en ubicación 10 con 100 tenges (se duplica a 200)
        silkRoad.placeStore("generous", 10, 100);
        // Coloca un robot tender en ubicación 5
        silkRoad.placeRobot("tender", 5);
        
        // Mueve el robot tender a la tienda generous
        silkRoad.moveRobot(5, 5);
        
        // Verifica que el tender tomó la mitad del duplicado (200 / 2 = 100)
        assertEquals(100, silkRoad.robots()[0][1]);
        // Verifica que quedó el otro 50% en la tienda
        assertEquals(50, silkRoad.stores()[0][1]);
    }
    
    /**
     * Prueba compatibilidad con firma antigua de placeStore.
     */
    @Test
    public void shouldSupportOldPlaceStoreSignature() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Usa la firma antigua de placeStore (crea tienda normal por defecto)
        silkRoad.placeStore(10, 100);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 tienda
        assertEquals(1, silkRoad.stores().length);
    }
    
    /**
     * Prueba compatibilidad con firma antigua de placeRobot.
     */
    @Test
    public void shouldSupportOldPlaceRobotSignature() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Usa la firma antigua de placeRobot (crea robot normal por defecto)
        silkRoad.placeRobot(10);
        
        // Verifica que la operación fue exitosa
        assertTrue(silkRoad.ok());
        // Verifica que hay exactamente 1 robot
        assertEquals(1, silkRoad.robots().length);
    }
    
    /**
     * Prueba que reboot preserva tipos de robots.
     */
    @Test
    public void shouldRebootPreserveRobotTypes() {
        // Crea un simulador de longitud 20
        silkRoad = new SilkRoad(20);
        // Lo hace invisible para no mostrar canvas
        silkRoad.makeInvisible();
        // Coloca una tienda normal en ubicación 10 con 100 tenges
        silkRoad.placeStore("normal", 10, 100);
        // Coloca un robot neverback en ubicación 5
        silkRoad.placeRobot("neverback", 5);
        
        // Mueve el robot neverback a la tienda
        silkRoad.moveRobot(5, 5);
        // Obtiene la posición después del movimiento
        int positionAfterMove = silkRoad.robots()[0][0];
        // Verifica que el robot está en la ubicación 10
        assertEquals(10, positionAfterMove);
        
        // Ejecuta reboot (reinicia todo)
        silkRoad.reboot();
        
        // Verifica que el robot volvió a su posición inicial (5)
        assertEquals(5, silkRoad.robots()[0][0]);
        // Verifica que la ganancia del robot se reinició
        assertEquals(0, silkRoad.robots()[0][1]);
        // Verifica que la tienda fue reabastecida
        assertEquals(100, silkRoad.stores()[0][1]);
    }
}
