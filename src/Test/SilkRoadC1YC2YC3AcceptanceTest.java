package Test;

import silkroad.*;
import shapes.*;

/**
 * Pruebas de aceptación visuales para SilkRoad.
 * Estas pruebas muestran visualmente el funcionamiento del simulador.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadC1YC2YC3AcceptanceTest {
    
    /**
     * Prueba visual del Ciclo 1 - Funcionalidades básicas.
     * Muestra la creación, movimiento y gestión básica del simulador.
     */
    public static void testCiclo1Visual() throws InterruptedException {
        System.out.println("=== INICIANDO PRUEBA VISUAL CICLO 1 ===");
        
        // Crear ruta de la seda visible
        SilkRoad silkRoad = new SilkRoad(20);
        silkRoad.makeVisible();
        Thread.sleep(1000);
        
        // Agregar tiendas con diferentes cantidades
        System.out.println("Agregando tiendas...");
        silkRoad.placeStore(3, 100);
        Thread.sleep(500);
        silkRoad.placeStore(7, 200);
        Thread.sleep(500);
        silkRoad.placeStore(12, 150);
        Thread.sleep(500);
        silkRoad.placeStore(18, 300);
        Thread.sleep(1000);
        
        // Agregar robots
        System.out.println("Agregando robots...");
        silkRoad.placeRobot(0);
        Thread.sleep(500);
        silkRoad.placeRobot(10);
        Thread.sleep(500);
        silkRoad.placeRobot(19);
        Thread.sleep(1000);
        
        // Mover robots manualmente
        System.out.println("Moviendo robots...");
        silkRoad.moveRobot(0, 3);  // Robot en 0 se mueve a tienda en 3
        Thread.sleep(1000);
        silkRoad.moveRobot(10, 2); // Robot en 10 se mueve a tienda en 12
        Thread.sleep(1000);
        silkRoad.moveRobot(19, -1); // Robot en 19 se mueve a tienda en 18
        Thread.sleep(1500);
        
        // Mostrar profit actual
        System.out.println("Ganancia máxima posible: " + silkRoad.profit());
        Thread.sleep(1000);
        
        // Reabastecer tiendas
        System.out.println("Reabasteciendo tiendas...");
        silkRoad.resupplyStores();
        Thread.sleep(1000);
        
        // Devolver robots a posición inicial
        System.out.println("Devolviendo robots a posición inicial...");
        silkRoad.returnRobots();
        Thread.sleep(1500);
        
        // Eliminar un robot y una tienda
        System.out.println("Eliminando robot en posición 0 y tienda en posición 7...");
        silkRoad.removeRobot(0);
        Thread.sleep(500);
        silkRoad.removeStore(7);
        Thread.sleep(1500);
        
        // Reiniciar todo
        System.out.println("Reiniciando simulador...");
        silkRoad.reboot();
        Thread.sleep(2000);
        
        silkRoad.finish();
        System.out.println("=== PRUEBA CICLO 1 COMPLETADA ===\n");
    }
    
    /**
     * Prueba visual del Ciclo 2 - Funcionalidades avanzadas.
     * Muestra movimientos inteligentes y estadísticas.
     */
    public static void testCiclo2Visual() throws InterruptedException {
        System.out.println("=== INICIANDO PRUEBA VISUAL CICLO 2 ===");
        
        // Crear desde entrada de maratón
        String marathonInput = "25 5 3\n" +
                              "5 100\n" +
                              "10 250\n" +
                              "15 150\n" +
                              "20 300\n" +
                              "23 200\n" +
                              "0\n" +
                              "8\n" +
                              "24\n";
        
        SilkRoad silkRoad = new SilkRoad(marathonInput);
        silkRoad.makeVisible();
        Thread.sleep(2000);
        
        System.out.println("Ruta creada desde entrada de maratón");
        System.out.println("Longitud: " + silkRoad.length());
        System.out.println("Tiendas: " + silkRoad.stores().length);
        System.out.println("Robots: " + silkRoad.robots().length);
        Thread.sleep(1500);
        
        // Movimiento inteligente individual
        System.out.println("\nMoviendo robot en posición 0 de forma óptima...");
        silkRoad.moveRobot(0);
        Thread.sleep(1500);
        
        // Movimiento inteligente de todos los robots
        System.out.println("Moviendo todos los robots de forma óptima...");
        silkRoad.moveRobots();
        Thread.sleep(2000);
        
        // Mostrar estadísticas de tiendas vaciadas
        System.out.println("\n=== ESTADÍSTICAS DE TIENDAS ===");
        int[][] emptiedStores = silkRoad.emptiedStores();
        for (int i = 0; i < emptiedStores.length; i++) {
            System.out.println("Tienda en posición " + emptiedStores[i][0] + 
                             " vaciada " + emptiedStores[i][1] + " vez(ces)");
        }
        Thread.sleep(1500);
        
        // Mostrar ganancias por movimiento
        System.out.println("\n=== GANANCIAS POR MOVIMIENTO ===");
        int[][] profitPerMove = silkRoad.profitPerMove();
        for (int i = 0; i < profitPerMove.length; i++) {
            System.out.print("Robot " + i + " ganancias: ");
            for (int j = 0; j < profitPerMove[i].length; j++) {
                if (profitPerMove[i][j] > 0) {
                    System.out.print(profitPerMove[i][j] + " ");
                }
            }
            System.out.println();
        }
        Thread.sleep(2000);
        
        // Reabastecer y mover nuevamente
        System.out.println("\nReabasteciendo tiendas para segundo día...");
        silkRoad.resupplyStores();
        Thread.sleep(1000);
        
        System.out.println("Moviendo robots óptimamente (día 2)...");
        silkRoad.moveRobots();
        Thread.sleep(2000);
        
        // Mostrar robot con máxima ganancia (debería parpadear)
        System.out.println("\nEl robot con máxima ganancia está parpadeando");
        Thread.sleep(3000);
        
        silkRoad.finish();
        System.out.println("=== PRUEBA CICLO 2 COMPLETADA ===\n");
    }
    
    /**
     * Prueba visual de la espiral y colores únicos.
     */
    public static void testEspiralYColores() throws InterruptedException {
        System.out.println("=== PRUEBA DE ESPIRAL Y COLORES ÚNICOS ===");
        
        SilkRoad silkRoad = new SilkRoad(50);
        silkRoad.makeVisible();
        Thread.sleep(1000);
        
        System.out.println("Agregando múltiples tiendas (colores únicos)...");
        for (int i = 0; i < 50; i += 5) {
            silkRoad.placeStore(i, 100 + i * 10);
            Thread.sleep(200);
        }
        
        System.out.println("Agregando múltiples robots (colores únicos)...");
        for (int i = 2; i < 50; i += 7) {
            silkRoad.placeRobot(i);
            Thread.sleep(200);
        }
        
        System.out.println("Observe la forma de espiral y los colores únicos");
        Thread.sleep(5000);
        
        silkRoad.finish();
        System.out.println("=== PRUEBA COMPLETADA ===\n");
    }
    
    /**
     * Método principal para ejecutar todas las pruebas de aceptación.
     */
    public static void main(String[] args) {
        try {
            System.out.println("INICIANDO PRUEBAS DE ACEPTACIÓN VISUAL\n");
            
            // Ejecutar prueba del Ciclo 1
            testCiclo1Visual();
            Thread.sleep(2000);
            
            // Ejecutar prueba del Ciclo 2
            testCiclo2Visual();
            Thread.sleep(2000);
            
            // Ejecutar prueba de espiral y colores
            testEspiralYColores();
            Thread.sleep(2000);
            
            testCiclo3Visual();
            
            System.out.println("\nTODAS LAS PRUEBAS DE ACEPTACIÓN COMPLETADAS");
            
        } catch (InterruptedException e) {
            System.err.println("Error en las pruebas: " + e.getMessage());
        }
    }
    
    /**
     * Prueba visual del Ciclo 3 - Solución del problema de la maratón.
     */
    public static void testCiclo3Visual() throws InterruptedException {
        System.out.println("=== INICIANDO PRUEBA VISUAL CICLO 3 ===");
    
        // Crear datos del problema
        SilkRoadContest contest = new SilkRoadContest(6);
        contest.addEvent(0, 1, 20, 0);
        contest.addEvent(1, 2, 15, 15);
        contest.addEvent(2, 2, 40, 50);
        contest.addEvent(3, 1, 50, 0);
        contest.addEvent(4, 2, 80, 20);
        contest.addEvent(5, 2, 70, 30);
    
        // Resolver
        System.out.println("Resolviendo problema de la maratón...");
        long[] profits = contest.solve();
    
        System.out.println("\n=== GANANCIAS POR DÍA ===");
        for (int i = 0; i < profits.length; i++) {
            System.out.println("Día " + (i + 1) + ": " + profits[i]);
        }
    
        // Simular visualmente
        SilkRoad silkRoad = new SilkRoad(100);
        silkRoad.makeVisible();
        Thread.sleep(1000);
    
        contest.simulate(silkRoad);
    
        Thread.sleep(3000);
        silkRoad.finish();
    
        System.out.println("=== PRUEBA CICLO 3 COMPLETADA ===\n");
    }
}
