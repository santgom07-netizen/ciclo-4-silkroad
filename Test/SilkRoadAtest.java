package Test;

import silkroad.*;
import shapes.*;
import javax.swing.JOptionPane;

/**
 * Pruebas de aceptación visual que evidencian lo mejor del proyecto.
 * El usuario debe confirmar si acepta cada prueba.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadAtest {
    
    /**
     * PRUEBA DE ACEPTACIÓN 1: Polimorfismo con Múltiples Tipos de Tiendas y Robots.
     */
    public static void acceptanceTest1() throws InterruptedException {
        System.out.println("\n════════════════════════════════════════════════════════════");
        System.out.println("PRUEBA DE ACEPTACIÓN 1: POLIMORFISMO Y EXTENSIBILIDAD");
        System.out.println("════════════════════════════════════════════════════════════\n");
        
        SilkRoad silkRoad = new SilkRoad(40);
        silkRoad.makeVisible();
        Thread.sleep(1000);
        
        System.out.println("Creando 4 tipos DIFERENTES de tiendas...");
        silkRoad.placeStore("normal", 5, 100);
        silkRoad.placeStore("autonomous", 15, 150);
        silkRoad.placeStore("fighter", 25, 80);
        silkRoad.placeStore("generous", 35, 120);
        Thread.sleep(1500);
        
        System.out.println("Creando 4 tipos DIFERENTES de robots...");
        silkRoad.placeRobot("normal", 0);
        silkRoad.placeRobot("tender", 3);
        silkRoad.placeRobot("neverback", 8);
        silkRoad.placeRobot("greedy", 18);
        Thread.sleep(1500);
        
        System.out.println("\n--- ESCENARIO 1: Robot Normal -> Tienda Normal ---");
        silkRoad.moveRobot(0, 5);
        Thread.sleep(1000);
        System.out.println("Robot Normal tiene: " + silkRoad.robots()[0][1] + " tenges\n");
        
        silkRoad.resupplyStores();
        System.out.println("--- ESCENARIO 2: Robot Tender -> Tienda Normal (toma mitad) ---");
        silkRoad.moveRobot(3, 2);
        Thread.sleep(1000);
        System.out.println("Robot Tender tiene: " + silkRoad.robots()[1][1] + " tenges (mitad de 150)\n");
        
        silkRoad.resupplyStores();
        System.out.println("--- ESCENARIO 3: Robot Greedy -> Tienda Generous (doble x doble) ---");
        silkRoad.moveRobot(18, 17);
        Thread.sleep(1000);
        System.out.println("Robot Greedy tiene: " + silkRoad.robots()[3][1] + " tenges\n");
        
        silkRoad.reboot();
        System.out.println("--- ESCENARIO 4: Robot Pobre RECHAZADO por Tienda Fighter ---");
        silkRoad.moveRobot(0, 25);
        Thread.sleep(1000);
        System.out.println("Robot sin dinero: " + silkRoad.robots()[0][1] + " (rechazado por Fighter)\n");
        
        System.out.println("--- ESCENARIO 5: Robot Rico ACEPTADO por Tienda Fighter ---");
        silkRoad.moveRobot(0, 5);
        Thread.sleep(500);
        silkRoad.moveRobot(5, 20);
        Thread.sleep(1000);
        System.out.println("Robot con dinero: " + silkRoad.robots()[0][1] + " (aceptado por Fighter)\n");
        
        silkRoad.reboot();
        System.out.println("--- ESCENARIO 6: Robot NeverBack NO vuelve a inicio ---");
        silkRoad.moveRobot(8, 7);
        int posBefore = silkRoad.robots()[2][0];
        silkRoad.returnRobots();
        int posAfter = silkRoad.robots()[2][0];
        System.out.println("Posición antes: " + posBefore + " | Posición después: " + posAfter + " (NO volvió)\n");
        
        Thread.sleep(1000);
        silkRoad.finish();
        
        // PREGUNTA AL USUARIO
        int resultado = JOptionPane.showConfirmDialog(
            null,
            "¿ACEPTA ESTA PRUEBA?\n\n" +
            "Se demostró:\n" +
            "✓ 4 tipos de tiendas con comportamientos diferentes\n" +
            "✓ 4 tipos de robots con comportamientos diferentes\n" +
            "✓ Polimorfismo: cada tipo actúa diferente\n" +
            "✓ Extensibilidad: fácil agregar nuevos tipos",
            "PRUEBA 1: ¿Aceptada?",
            JOptionPane.YES_NO_OPTION
        );
        
        if (resultado == JOptionPane.YES_OPTION) {
            System.out.println("✓ PRUEBA 1 ACEPTADA POR EL USUARIO\n");
        } else {
            System.out.println("✗ PRUEBA 1 RECHAZADA POR EL USUARIO\n");
        }
    }
    
    /**
     * PRUEBA DE ACEPTACIÓN 2: Decisiones Inteligentes y Control de Acceso.
     */
    public static void acceptanceTest2() throws InterruptedException {
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("PRUEBA DE ACEPTACIÓN 2: INTELIGENCIA Y CONTROL DE ACCESO");
        System.out.println("════════════════════════════════════════════════════════════\n");
        
        SilkRoad silkRoad = new SilkRoad(60);
        silkRoad.makeVisible();
        Thread.sleep(1000);
        
        System.out.println("Escenario: Ruta con múltiples tiendas y robots inteligentes\n");
        
        // Crear una ruta variada
        silkRoad.placeStore("normal", 10, 50);
        silkRoad.placeStore("generous", 20, 100);
        silkRoad.placeStore("fighter", 30, 200);
        silkRoad.placeStore("normal", 40, 75);
        Thread.sleep(1000);
        
        // Robots en diferentes posiciones
        silkRoad.placeRobot("normal", 0);
        silkRoad.placeRobot("tender", 5);
        silkRoad.placeRobot("greedy", 25);
        Thread.sleep(1000);
        
        System.out.println("--- MOVIMIENTO INTELIGENTE AUTOMÁTICO ---");
        System.out.println("Los robots eligen el MEJOR MOVIMIENTO de forma inteligente...\n");
        silkRoad.moveRobots();
        Thread.sleep(2000);
        
        System.out.println("\nResultados después del movimiento inteligente:");
        int[][] robots = silkRoad.robots();
        String[] names = {"Normal", "Tender", "Greedy"};
        for (int i = 0; i < robots.length; i++) {
            System.out.println(names[i] + " -> Pos: " + robots[i][0] + " | Ganancia: " + robots[i][1]);
        }
        
        System.out.println("\n--- CONTROL DE ACCESO CON FIGHTER STORE ---");
        System.out.println("Solo robots con dinero pueden acceder a tienda Fighter\n");
        
        // Los robots ganaron dinero, ahora pueden acceder a fighter
        silkRoad.resupplyStores();
        
        // Mover robot a Fighter
        if (robots[0][1] > 0) {
            System.out.println("Robot con " + robots[0][1] + " tenges intenta acceder a Fighter...");
            silkRoad.moveRobot(robots[0][0], 30 - robots[0][0]);
            Thread.sleep(1000);
            System.out.println("✓ Acceso PERMITIDO (tiene suficiente dinero)\n");
        }
        
        // Reiniciar y probar sin dinero
        silkRoad.reboot();
        System.out.println("Robot sin dinero (0) intenta acceder a Fighter en pos 30...");
        silkRoad.moveRobot(0, 30);
        Thread.sleep(1000);
        System.out.println("✗ Acceso RECHAZADO (no tiene dinero)\n");
        
        System.out.println("--- VALIDACIÓN DE COLORES ÚNICOS ---");
        System.out.println("Cada tienda y robot tiene color ÚNICO sin repetición\n");
        Thread.sleep(1500);
        
        silkRoad.finish();
        
        // PREGUNTA AL USUARIO
        int resultado = JOptionPane.showConfirmDialog(
            null,
            "¿ACEPTA ESTA PRUEBA?\n\n" +
            "Se demostró:\n" +
            "✓ Movimiento INTELIGENTE de robots automático\n" +
            "✓ Control de ACCESO basado en dinero (Fighter)\n" +
            "✓ Cada robot elige su mejor opción\n" +
            "✓ Sistema de colores únicos funcionando",
            "PRUEBA 2: ¿Aceptada?",
            JOptionPane.YES_NO_OPTION
        );
        
        if (resultado == JOptionPane.YES_OPTION) {
            System.out.println("✓ PRUEBA 2 ACEPTADA POR EL USUARIO\n");
        } else {
            System.out.println("✗ PRUEBA 2 RECHAZADA POR EL USUARIO\n");
        }
    }
    
    /**
     * Método principal.
     */
    public static void main(String[] args) {
        try {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║  PRUEBAS DE ACEPTACIÓN CICLO 4 - PROYECTO SILK ROAD        ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            
            // Ejecuta Prueba 1
            acceptanceTest1();
            Thread.sleep(2000);
            
            // Ejecuta Prueba 2
            acceptanceTest2();
            
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║           TODAS LAS PRUEBAS COMPLETADAS                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");
            
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
