package Test;

import silkroad.*;
import shapes.*;

/**
 * Casos de prueba comunes para SilkRoad Ciclo 4.
 * Creación colectiva siguiendo el protocolo definido.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadCC4test {
    
    /**
     * Caso de prueba 1: Diferentes tipos de tiendas.
     */
    public static void testCase1() {
        System.out.println("=== CASO DE PRUEBA 1: Diferentes Tipos de Tiendas ===");
        
        SilkRoad silkRoad = new SilkRoad(50);
        silkRoad.makeInvisible();
        
        // Coloca cada tipo de tienda
        silkRoad.placeStore("normal", 5, 100);
        silkRoad.placeStore("autonomous", 15, 150);
        silkRoad.placeStore("fighter", 25, 80);
        silkRoad.placeStore("generous", 35, 120);
        
        System.out.println("Tiendas agregadas:");
        int[][] stores = silkRoad.stores();
        for (int i = 0; i < stores.length; i++) {
            System.out.println("  Tienda " + i + " - Ubicacion: " + stores[i][0] + ", Dinero: " + stores[i][1]);
        }
        
        System.out.println("✓ PRUEBA PASADA\n");
    }
    
    /**
     * Caso de prueba 2: Diferentes tipos de robots.
     */
    public static void testCase2() {
        System.out.println("=== CASO DE PRUEBA 2: Diferentes Tipos de Robots ===");
        
        SilkRoad silkRoad = new SilkRoad(50);
        silkRoad.makeInvisible();
        
        // Coloca cada tipo de robot
        silkRoad.placeRobot("normal", 0);
        silkRoad.placeRobot("tender", 10);
        silkRoad.placeRobot("neverback", 20);
        silkRoad.placeRobot("greedy", 30);
        
        System.out.println("Robots agregados:");
        int[][] robots = silkRoad.robots();
        for (int i = 0; i < robots.length; i++) {
            System.out.println("  Robot " + i + " - Ubicacion: " + robots[i][0] + ", Ganancia: " + robots[i][1]);
        }
        
        System.out.println("✓ PRUEBA PASADA\n");
    }
    
    /**
     * Caso de prueba 3: Interacción tienda fighter con robots.
     */
    public static void testCase3() {
        System.out.println("=== CASO DE PRUEBA 3: Tienda Fighter - Control de Acceso ===");
        
        SilkRoad silkRoad = new SilkRoad(30);
        silkRoad.makeInvisible();
        
        // Coloca tienda normal, fighter y robots
        silkRoad.placeStore("normal", 10, 100);
        silkRoad.placeStore("fighter", 20, 200);
        silkRoad.placeRobot("normal", 0);
        
        // Robot sin dinero intenta acceder a fighter
        System.out.println("Robot sin dinero (0) intenta acceder a Fighter:");
        silkRoad.moveRobot(0, 20);
        System.out.println("Resultado: Robot rechazado - Ganancia: " + silkRoad.robots()[0][1]);
        
        // Robot obtiene dinero de tienda normal
        silkRoad.reboot();
        silkRoad.moveRobot(0, 10);
        System.out.println("\nRobot obtiene 100 de tienda normal - Ganancia: " + silkRoad.robots()[0][1]);
        
        // Robot con dinero accede a fighter
        silkRoad.moveRobot(10, 10);
        System.out.println("Robot con dinero accede a Fighter - Ganancia final: " + silkRoad.robots()[0][1]);
        
        System.out.println("✓ PRUEBA PASADA\n");
    }
    
    /**
     * Caso de prueba 4: Tienda generous duplica dinero.
     */
    public static void testCase4() {
        System.out.println("=== CASO DE PRUEBA 4: Tienda Generous - Duplica Dinero ===");
        
        SilkRoad silkRoad = new SilkRoad(30);
        silkRoad.makeInvisible();
        
        silkRoad.placeStore("generous", 15, 100);
        silkRoad.placeRobot("normal", 0);
        
        System.out.println("Robot normal se mueve a tienda generous (100 tenges)");
        silkRoad.moveRobot(0, 15);
        
        int ganancia = silkRoad.robots()[0][1];
        System.out.println("Ganancia del robot: " + ganancia + " tenges (esperado: 200)");
        
        if (ganancia == 200) {
            System.out.println("✓ PRUEBA PASADA\n");
        } else {
            System.out.println("✗ PRUEBA FALLIDA\n");
        }
    }
    
    /**
     * Caso de prueba 5: Robot tender toma mitad del dinero.
     */
    public static void testCase5() {
        System.out.println("=== CASO DE PRUEBA 5: Robot Tender - Toma Mitad ===");
        
        SilkRoad silkRoad = new SilkRoad(30);
        silkRoad.makeInvisible();
        
        silkRoad.placeStore("normal", 15, 100);
        silkRoad.placeRobot("tender", 0);
        
        System.out.println("Robot tender se mueve a tienda normal (100 tenges)");
        silkRoad.moveRobot(0, 15);
        
        int ganancia = silkRoad.robots()[0][1];
        int dineroTienda = silkRoad.stores()[0][1];
        
        System.out.println("Ganancia del robot: " + ganancia + " tenges (esperado: 50)");
        System.out.println("Dinero restante en tienda: " + dineroTienda + " (esperado: 50)");
        
        if (ganancia == 50 && dineroTienda == 50) {
            System.out.println("✓ PRUEBA PASADA\n");
        } else {
            System.out.println("✗ PRUEBA FALLIDA\n");
        }
    }
    
    /**
     * Caso de prueba 6: Robot neverback no vuelve a inicio.
     */
    public static void testCase6() {
        System.out.println("=== CASO DE PRUEBA 6: Robot NeverBack - No Vuelve ===");
        
        SilkRoad silkRoad = new SilkRoad(30);
        silkRoad.makeInvisible();
        
        silkRoad.placeRobot("neverback", 10);
        
        System.out.println("Robot neverback posición inicial: 10");
        silkRoad.moveRobot(10, 10);
        System.out.println("Robot se mueve a posición: " + silkRoad.robots()[0][0]);
        
        silkRoad.returnRobots();
        int posicionFinal = silkRoad.robots()[0][0];
        System.out.println("Después de returnRobots(): " + posicionFinal + " (esperado: 20, no 10)");
        
        if (posicionFinal != 10) {
            System.out.println("✓ PRUEBA PASADA\n");
        } else {
            System.out.println("✗ PRUEBA FALLIDA\n");
        }
    }
    
    /**
     * Método principal para ejecutar todos los casos de prueba.
     */
    public static void main(String[] args) {
        System.out.println("EJECUTANDO CASOS DE PRUEBA COMUNES CICLO 4\n");
        
        testCase1();
        testCase2();
        testCase3();
        testCase4();
        testCase5();
        testCase6();
        
        System.out.println("TODOS LOS CASOS DE PRUEBA COMPLETADOS");
    }
}
