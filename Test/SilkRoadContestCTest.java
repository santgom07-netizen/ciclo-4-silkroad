package Test;

import silkroad.*;

/**
 * Casos de prueba comunes para SilkRoadContest.
 * 
 * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
 */
public class SilkRoadContestCTest {
    
    /**
     * Caso de prueba 1: Ejemplo oficial de la maratón.
     */
    public static void testCase1() {
        System.out.println("=== CASO DE PRUEBA 1: Ejemplo Oficial ===");
        
        String input = "6\n" +
                      "1 20\n" +
                      "2 15 15\n" +
                      "2 40 50\n" +
                      "1 50\n" +
                      "2 80 20\n" +
                      "2 70 30\n";
        
        long[] result = SilkRoadContest.solveFromInput(input);
        
        long[] expected = {0, 10, 35, 50, 50, 60};
        
        System.out.println("Resultado esperado: [0, 10, 35, 50, 50, 60]");
        System.out.print("Resultado obtenido: [");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        boolean passed = true;
        for (int i = 0; i < expected.length; i++) {
            if (result[i] != expected[i]) {
                passed = false;
                System.out.println("ERROR en día " + (i + 1) + ": esperado " + expected[i] + ", obtenido " + result[i]);
            }
        }
        
        System.out.println(passed ? "✓ PRUEBA PASADA\n" : "✗ PRUEBA FALLIDA\n");
    }
    
    /**
     * Caso de prueba 2: Escenario simple.
     */
    public static void testCase2() {
        System.out.println("=== CASO DE PRUEBA 2: Escenario Simple ===");
        
        String input = "3\n" +
                      "1 0\n" +
                      "2 5 20\n" +
                      "2 10 30\n";
        
        long[] result = SilkRoadContest.solveFromInput(input);
        
        System.out.print("Resultado: [");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("Verificación: Robot en 0 puede ir a tienda en 5 (ganancia 15) o a tienda en 10 (ganancia 20)");
        System.out.println("Mejor opción: tienda en 10 con ganancia 20\n");
    }
    
    /**
     * Caso de prueba 3: Múltiples robots y tiendas.
     */
    public static void testCase3() {
        System.out.println("=== CASO DE PRUEBA 3: Múltiples Robots y Tiendas ===");
        
        SilkRoadContest contest = new SilkRoadContest(5);
        contest.addEvent(0, 1, 0, 0);
        contest.addEvent(1, 1, 100, 0);
        contest.addEvent(2, 2, 10, 50);
        contest.addEvent(3, 2, 90, 60);
        contest.addEvent(4, 1, 50, 0);
        
        long[] result = contest.solve();
        
        System.out.print("Resultado: [");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]\n");
    }
    
    /**
     * Método principal para ejecutar todos los casos de prueba.
     */
    public static void main(String[] args) {
        System.out.println("EJECUTANDO CASOS DE PRUEBA PARA SILKROADCONTEST\n");
        
        testCase1();
        testCase2();
        testCase3();
        
        System.out.println("TODOS LOS CASOS DE PRUEBA COMPLETADOS");
    }
}
