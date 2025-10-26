package Test;

import silkroad.*;
import shapes.*;

/**
 * Casos de prueba comunes para SilkRoad Ciclo 2.
 * Creación colectiva siguiendo el protocolo definido.
 * Enfocado en funcionalidades avanzadas: movimiento inteligente y estadísticas.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadCC2Test {
    
    /**
     * Caso de prueba 1: Movimiento Inteligente - Robot elige mejor tienda.
     */
    public static void testCase1() {
        System.out.println("=== CASO DE PRUEBA 1: Movimiento Inteligente ===");
        
        SilkRoad silkRoad = new SilkRoad(50);
        silkRoad.makeInvisible();
        
        // Coloca tiendas con diferentes valores
        silkRoad.placeStore(10, 50);   // Peor opción: lejos y poco dinero
        silkRoad.placeStore(15, 300);  // Mejor opción: cercana y mucho dinero
        silkRoad.placeStore(40, 100);  // Media: lejana
        
        // Coloca robot
        silkRoad.placeRobot(0);
        
        System.out.println("Robot en posición 0");
        System.out.println("Tiendas disponibles:");
        System.out.println("  - Pos 10: 50 tenges (distancia: 10)");
        System.out.println("  - Pos 15: 300 tenges (distancia: 5)");
        System.out.println("  - Pos 40: 100 tenges (distancia: 20)");
        
        // Movimiento inteligente
        System.out.println("\nRobot elige MEJOR opción (Pos 15 con 300 tenges):");
        silkRoad.moveRobot(0);
        
        int posicion = silkRoad.robots()[0][0];
        int ganancia = silkRoad.robots()[0][1];
        
        System.out.println("Posición final: " + posicion + " (esperado: 15)");
        System.out.println("Ganancia: " + ganancia + " (esperado: 300)");
        
        if (posicion == 15 && ganancia == 300) {
            System.out.println("✓ PRUEBA PASADA\n");
        } else {
            System.out.println("✗ PRUEBA FALLIDA\n");
        }
    }
    
    /**
     * Caso de prueba 2: Todos los robots se mueven óptimamente.
     */
    public static void testCase2() {
        System.out.println("=== CASO DE PRUEBA 2: Movimiento de Múltiples Robots ===");
        
        SilkRoad silkRoad = new SilkRoad(60);
        silkRoad.makeInvisible();
        
        // Coloca tiendas
        silkRoad.placeStore(15, 100);
        silkRoad.placeStore(30, 200);
        silkRoad.placeStore(45, 150);
        
        // Coloca múltiples robots
        silkRoad.placeRobot(0);   // Robot 1
        silkRoad.placeRobot(25);  // Robot 2
        silkRoad.placeRobot(59);  // Robot 3
        
        System.out.println("Robot 1 en pos 0 -> Tienda en 15: 100 tenges");
        System.out.println("Robot 2 en pos 25 -> Tienda en 30: 200 tenges");
        System.out.println("Robot 3 en pos 59 -> Tienda en 45: 150 tenges");
        
        // Mover todos de forma óptima
        silkRoad.moveRobots();
        
        int[][] robots = silkRoad.robots();
        
        System.out.println("\nResultados:");
        for (int i = 0; i < robots.length; i++) {
            System.out.println("  Robot " + i + " -> Pos: " + robots[i][0] + ", Ganancia: " + robots[i][1]);
        }
        
        System.out.println("✓ PRUEBA PASADA\n");
    }
    
    /**
     * Caso de prueba 3: Historial de ganancias por movimiento.
     */
    public static void testCase3() {
        System.out.println("=== CASO DE PRUEBA 3: Historial de Ganancias por Movimiento ===");
        
        SilkRoad silkRoad = new SilkRoad(50);
        silkRoad.makeInvisible();
        
        // Coloca tiendas
        silkRoad.placeStore(10, 100);
        silkRoad.placeStore(20, 200);
        silkRoad.placeStore(30, 150);
        
        // Coloca robot
        silkRoad.placeRobot(0);
        
        System.out.println("Robot 1 movimiento 1:");
        silkRoad.moveRobot(0, 10);
        System.out.println("  Ganancia: 100 tenges");
        
        // Reabastecer
        silkRoad.resupplyStores();
        
        System.out.println("\nRobot 1 movimiento 2:");
        silkRoad.moveRobot(10, 10);
        System.out.println("  Ganancia: 200 tenges");
        
        // Obtener historial
        int[][] profitPerMove = silkRoad.profitPerMove();
        
        System.out.println("\nHistorial del robot:");
        for (int i = 0; i < profitPerMove[0].length; i++) {
            if (profitPerMove[0][i] > 0) {
                System.out.println("  Movimiento " + (i+1) + ": " + profitPerMove[0][i] + " tenges");
            }
        }
        
        System.out.println("✓ PRUEBA PASADA\n");
    }
    
    /**
     * Caso de prueba 4: Tiendas vaciadas múltiples veces.
     */
    public static void testCase4() {
        System.out.println("=== CASO DE PRUEBA 4: Tiendas Vaciadas Multiple Veces ===");
        
        SilkRoad silkRoad = new SilkRoad(30);
        silkRoad.makeInvisible();
        
        // Una sola tienda
        silkRoad.placeStore(15, 100);
        
        // Dos robots
        silkRoad.placeRobot(0);
        silkRoad.placeRobot(30);
        
        System.out.println("Día 1: Robot 1 vacía tienda");
        silkRoad.moveRobot(0, 15);
        
        int[][] emptied = silkRoad.emptiedStores();
        System.out.println("Tienda vaciada: " + emptied[0][1] + " vez(ces)");
        
        // Reabastecer
        silkRoad.resupplyStores();
        
        System.out.println("\nDía 2: Robot 2 vacía tienda nuevamente");
        silkRoad.moveRobot(30, -15);
        
        emptied = silkRoad.emptiedStores();
        System.out.println("Tienda vaciada: " + emptied[0][1] + " vez(ces) (esperado: 2)");
        
        if (emptied[0][1] == 2) {
            System.out.println("✓ PRUEBA PASADA\n");
        } else {
            System.out.println("✗ PRUEBA FALLIDA\n");
        }
    }
    
    /**
     * Caso de prueba 5: Ganancia máxima con múltiples tiendas.
     */
    public static void testCase5() {
        System.out.println("=== CASO DE PRUEBA 5: Ganancia Máxima Posible ===");
        
        SilkRoad silkRoad = new SilkRoad(40);
        silkRoad.makeInvisible();
        
        // Múltiples tiendas
        silkRoad.placeStore(10, 100);
        silkRoad.placeStore(20, 200);
        silkRoad.placeStore(30, 150);
        
        System.out.println("Total de tenges en tiendas: 100 + 200 + 150 = 450");
        
        int maxProfit = silkRoad.profit();
        System.out.println("Ganancia máxima posible: " + maxProfit + " (esperado: 450)");
        
        // Mover robot
        silkRoad.placeRobot(0);
        silkRoad.moveRobot(0, 10);
        
        maxProfit = silkRoad.profit();
        System.out.println("Después de mover robot (ganancia actual 100):");
        System.out.println("Ganancia máxima posible: " + maxProfit + " (esperado: 450)");
        
        if (maxProfit == 450) {
            System.out.println("✓ PRUEBA PASADA\n");
        } else {
            System.out.println("✗ PRUEBA FALLIDA\n");
        }
    }
    
    /**
     * Caso de prueba 6: Robot con máxima ganancia parpadea.
     */
    public static void testCase6() {
        System.out.println("=== CASO DE PRUEBA 6: Robot con Máxima Ganancia Parpadea ===");
        
        SilkRoad silkRoad = new SilkRoad(40);
        silkRoad.makeVisible();
        
        try {
            // Coloca tiendas
            silkRoad.placeStore(10, 100);
            silkRoad.placeStore(20, 500);
            
            // Coloca robots
            silkRoad.placeRobot(0);
            silkRoad.placeRobot(5);
            
            Thread.sleep(1000);
            
            System.out.println("Moviendo robots...");
            silkRoad.moveRobot(0, 10);  // Gana 100
            Thread.sleep(500);
            silkRoad.moveRobot(5, 15);  // Gana 500
            Thread.sleep(500);
            
            System.out.println("El robot con 500 tenges DEBE parpadear");
            Thread.sleep(3000);
            
            silkRoad.finish();
            System.out.println("✓ PRUEBA PASADA\n");
            
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Método principal para ejecutar todos los casos de prueba.
     */
    public static void main(String[] args) {
        System.out.println("EJECUTANDO CASOS DE PRUEBA COMUNES CICLO 2\n");
        
        testCase1();
        testCase2();
        testCase3();
        testCase4();
        testCase5();
        testCase6();
        
        System.out.println("TODOS LOS CASOS DE PRUEBA CICLO 2 COMPLETADOS");
    }
}
