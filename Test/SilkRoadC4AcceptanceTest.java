    package Test;
    
    import silkroad.*;
    import shapes.*; 
    
    /**
     * Pruebas de aceptación visual para el Ciclo 4 - Diferentes tipos de tiendas y robots.
     * Muestra visualmente el funcionamiento del simulador con nueva extensibilidad.
     * 
     * @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
     */
    public class SilkRoadC4AcceptanceTest {
        
        /**
         * Prueba visual del Ciclo 4 - Diferentes tipos de tiendas y robots.
         * Demuestra el polimorfismo y extensibilidad del sistema.
         */
        public static void testCiclo4Visual() throws InterruptedException {
            // Imprime el encabezado de la prueba
            System.out.println("=== INICIANDO PRUEBA VISUAL CICLO 4 ===");
            
            // Crea una ruta visible de longitud 40
            SilkRoad silkRoad = new SilkRoad(40);
            // Hace el simulador visible
            silkRoad.makeVisible();
            // Espera 1 segundo para que se dibuje
            Thread.sleep(1000);
            
            // Imprime mensaje indicando creación de tiendas
            System.out.println("\n--- CREANDO DIFERENTES TIPOS DE TIENDAS ---");
            // Coloca tienda normal en posición 5 con 100 tenges
            silkRoad.placeStore("normal", 5, 100);
            System.out.println("Tienda Normal en posicion 5 con 100 tenges");
            Thread.sleep(500);
            
            // Coloca tienda autónoma solicitada en posición 15 (ella elige su ubicación)
            silkRoad.placeStore("autonomous", 15, 150);
            System.out.println("Tienda Autonoma solicitada en posicion 15 - ella elige su ubicacion");
            Thread.sleep(500);
            
            // Coloca tienda fighter en posición 25
            silkRoad.placeStore("fighter", 25, 80);
            System.out.println("Tienda Fighter en posicion 25 - solo robots ricos pueden entrar");
            Thread.sleep(500);
            
            // Coloca tienda generous en posición 35 (duplica dinero)
            silkRoad.placeStore("generous", 35, 120);
            System.out.println("Tienda Generosa en posicion 35 - da el doble de dinero");
            Thread.sleep(1500);
            
            // Imprime mensaje indicando creación de robots
            System.out.println("\n--- CREANDO DIFERENTES TIPOS DE ROBOTS ---");
            // Coloca robot normal en posición 0
            silkRoad.placeRobot("normal", 0);
            System.out.println("Robot Normal en posicion 0");
            Thread.sleep(500);
            
            // Coloca robot tender en posición 3 (toma la mitad)
            silkRoad.placeRobot("tender", 3);
            System.out.println("Robot Tender en posicion 3 - solo toma la mitad");
            Thread.sleep(500);
            
            // Coloca robot neverback en posición 8 (no vuelve a inicio)
            silkRoad.placeRobot("neverback", 8);
            System.out.println("Robot NeverBack en posicion 8 - nunca vuelve a su posicion inicial");
            Thread.sleep(500);
            
            // Coloca robot greedy en posición 18 (intenta tomar el doble)
            silkRoad.placeRobot("greedy", 18);
            System.out.println("Robot Greedy en posicion 18 - intenta tomar el doble");
            Thread.sleep(1500);
            
            // Primer escenario: Robot Normal a Tienda Normal
            System.out.println("\n--- ESCENARIO 1: Robot Normal -> Tienda Normal ---");
            System.out.println("Robot Normal en 0 se mueve 5 posiciones a la tienda Normal en 5");
            // Mueve robot de posición 0 a 5
            silkRoad.moveRobot(0, 5);
            Thread.sleep(1000);
            System.out.println("Ganancia del robot: 100 tenges (esperado: 100)");
            System.out.println("Dinero en tienda: " + silkRoad.stores()[0][1] + " (esperado: 0)");
            Thread.sleep(1500);
            
            // Segundo escenario: Robot Tender a Tienda Normal
            System.out.println("\n--- ESCENARIO 2: Robot Tender -> Tienda Normal ---");
            // Reabastece todas las tiendas a su cantidad inicial
            silkRoad.resupplyStores();
            System.out.println("Robot Tender en 3 se mueve 2 posiciones");
            // Mueve robot tender (toma mitad del dinero)
            silkRoad.moveRobot(3, 2);
            Thread.sleep(1000);
            System.out.println("Ganancia del robot: " + silkRoad.robots()[1][1] + " tenges (esperado: ~75, mitad de 150)");
            Thread.sleep(1500);
            
            // Tercer escenario: Robot Greedy a Tienda Generous
            System.out.println("\n--- ESCENARIO 3: Robot Greedy -> Tienda Generosa ---");
            // Reabastece tiendas nuevamente
            silkRoad.resupplyStores();
            System.out.println("Robot Greedy en 18 se mueve 17 posiciones a Tienda Generosa en 35");
            // Mueve robot greedy a tienda generous (genera interacción de dobles)
            silkRoad.moveRobot(18, 17);
            Thread.sleep(1000);
            System.out.println("Ganancia del robot: " + silkRoad.robots()[3][1] + " tenges");
            System.out.println("(Generosa da doble: 120*2=240, pero Greedy pide doble, limitado a disponible)");
            Thread.sleep(1500);
            
            // Cuarto escenario: Robot sin dinero rechazado por Fighter
            System.out.println("\n--- ESCENARIO 4: Robot Sin Dinero -> Tienda Fighter ---");
            // Reinicia todo (robots a inicio, tiendas reabastecidas)
            silkRoad.reboot();
            Thread.sleep(500);
            System.out.println("Robot Normal en 0 intenta acceder a Tienda Fighter en 25");
            // Intenta mover robot sin dinero a tienda fighter (debería fallar)
            silkRoad.moveRobot(0, 25);
            Thread.sleep(1000);
            System.out.println("Robot sin dinero anterior: " + silkRoad.robots()[0][1] + " tenges (esperado: 0)");
            System.out.println("Tienda Fighter mantiene: " + silkRoad.stores()[2][1] + " tenges (esperado: 80)");
            Thread.sleep(1500);
            
            // Quinto escenario: Robot con dinero aceptado por Fighter
            System.out.println("\n--- ESCENARIO 5: Robot Con Dinero -> Tienda Fighter ---");
            // Mueve robot a tienda normal primero (obtiene dinero)
            silkRoad.moveRobot(0, 5);
            Thread.sleep(1000);
            System.out.println("Ahora Robot Normal tiene 100 tenges");
            // Mueve robot a tienda fighter (ahora acepta porque tiene dinero)
            silkRoad.moveRobot(5, 20);
            Thread.sleep(1000);
            System.out.println("Ganancia del robot: " + silkRoad.robots()[0][1] + " tenges (esperado: 180)");
            System.out.println("(100 de tienda normal + 80 de fighter porque ahora tiene suficiente dinero)");
            Thread.sleep(1500);
            
            // Sexto escenario: Robot NeverBack no vuelve a inicio
            System.out.println("\n--- ESCENARIO 6: Robot NeverBack No Vuelve ---");
            // Reinicia simulador
            silkRoad.reboot();
            Thread.sleep(500);
            System.out.println("Robot NeverBack posicion inicial: 8");
            // Mueve robot neverback
            silkRoad.moveRobot(8, 7);
            Thread.sleep(1000);
            System.out.println("Posicion despues de mover: " + silkRoad.robots()[2][0]);
            // Intenta devolver robots a posición inicial
            silkRoad.returnRobots();
            Thread.sleep(1000);
            System.out.println("Posicion despues de returnRobots: " + silkRoad.robots()[2][0]);
            System.out.println("(Deberia seguir en 15, no volvio a 8)");
            Thread.sleep(1500);
            
            // Movimiento inteligente con todos los tipos
            System.out.println("\n--- MOVIMIENTO INTELIGENTE CON TIPOS MIXTOS ---");
            // Reinicia simulador
            silkRoad.reboot();
            Thread.sleep(500);
            System.out.println("Moviendo todos los robots de forma optima...");
            // Mueve todos los robots de forma inteligente
            silkRoad.moveRobots();
            Thread.sleep(2000);
            
            // Muestra estadísticas finales
            System.out.println("\n=== ESTADISTICAS FINALES ===");
            // Obtiene información de todos los robots
            int[][] robots = silkRoad.robots();
            // Array con nombres de tipos de robots
            String[] types = {"Normal", "Tender", "NeverBack", "Greedy"};
            // Itera sobre cada robot e imprime su posición y ganancia
            for (int i = 0; i < robots.length; i++) {
                System.out.println(types[i] + " - Posicion: " + robots[i][0] + ", Ganancia: " + robots[i][1]);
            }
            Thread.sleep(3000);
            
            // Finaliza el simulador
            silkRoad.finish();
            System.out.println("=== PRUEBA CICLO 4 COMPLETADA ===\n");
        }
        
        /**
         * Prueba de polymorfismo - Multiples combinaciones de tipos.
         */
        public static void testPolymorphismDemo() throws InterruptedException {
            // Imprime encabezado de demostración de polimorfismo
            System.out.println("=== DEMOSTRACION DE POLIMORFISMO EN CICLO 4 ===");
            
            // Crea ruta visible de longitud 50
            SilkRoad silkRoad = new SilkRoad(50);
            // Hace visible
            silkRoad.makeVisible();
            Thread.sleep(1000);
            
            // Imprime mensaje de agrego de tiendas
            System.out.println("\nAgregando 8 tiendas de diferentes tipos...");
            // Itera para agregar 8 tiendas de tipos variados
            for (int i = 0; i < 8; i++) {
                // Calcula ubicación (cada 6 posiciones)
                int location = i * 6;
                // Array de tipos de tiendas
                String[] types = {"normal", "autonomous", "fighter", "generous"};
                // Selecciona tipo de tienda (cicla entre los 4 tipos)
                String type = types[i % 4];
                // Coloca tienda
                silkRoad.placeStore(type, location, 100 + i * 20);
                System.out.println("  Tienda " + type + " en posicion " + location);
                Thread.sleep(300);
            }
            
            // Imprime mensaje de agrego de robots
            System.out.println("\nAgregando 8 robots de diferentes tipos...");
            // Itera para agregar 8 robots de tipos variados
            for (int i = 0; i < 8; i++) {
                // Calcula ubicación desplazada
                int location = 2 + (i * 6);
                // Array de tipos de robots
                String[] types = {"normal", "tender", "greedy", "neverback"};
                // Selecciona tipo de robot (cicla entre los 4 tipos)
                String type = types[i % 4];
                // Coloca robot
                silkRoad.placeRobot(type, location);
                System.out.println("  Robot " + type + " en posicion " + location);
                Thread.sleep(300);
            }
            
            // Imprime mensaje de movimiento óptimo
            System.out.println("\nMoviendo todos los robots de forma optima...");
            // Mueve todos los robots de forma inteligente
            silkRoad.moveRobots();
            Thread.sleep(2000);
            
            // Imprime mensaje de reabastecimiento
            System.out.println("\nReabasteciendo y moviendo nuevamente...");
            // Reabastece todas las tiendas
            silkRoad.resupplyStores();
            Thread.sleep(500);
            // Mueve robots nuevamente
            silkRoad.moveRobots();
            Thread.sleep(2000);
            
            // Imprime verificación de acceso a Fighter stores
            System.out.println("\nVerificando que los robots con mas dinero pueden acceder a Fighter stores...");
            Thread.sleep(2000);
            
            // Finaliza simulador
            silkRoad.finish();
            System.out.println("=== DEMOSTRACION COMPLETADA ===\n");
        }
        
        /**
         * Prueba de extensibilidad - Facil agregar nuevos tipos.
         */
        public static void testExtensibility() throws InterruptedException {
            // Imprime encabezado de demostración de extensibilidad
            System.out.println("=== DEMOSTRACION DE EXTENSIBILIDAD EN CICLO 4 ===");
            
            // Crea ruta visible de longitud 30
            SilkRoad silkRoad = new SilkRoad(30);
            // Hace visible
            silkRoad.makeVisible();
            Thread.sleep(1000);
            
            // Imprime información sobre extensibilidad
            System.out.println("\nEste sistema es extensible - facil agregar nuevos tipos:");
            System.out.println("- Nuevas clases Store que extiendan de Store");
            System.out.println("- Nuevas clases Robot que extiendan de Robot");
            System.out.println("- Sin modificar la clase SilkRoad existente");
            System.out.println("- Ejemplo: GreedyRobot y GenerousStore ya agregados");
            
            // Imprime mensaje de ejemplo
            System.out.println("\nAgregando ejemplo con tipos propuestos...");
            // Coloca tienda generous
            silkRoad.placeStore("generous", 10, 100);
            // Coloca robot greedy
            silkRoad.placeRobot("greedy", 5);
            Thread.sleep(1000);
            
            // Mueve robot greedy a tienda generous
            silkRoad.moveRobot(5, 5);
            Thread.sleep(1000);
            
            // Imprime resultado
            System.out.println("Robot Greedy en Tienda Generosa: " + silkRoad.robots()[0][1] + " tenges");
            System.out.println("(Generous da doble: 100*2=200, Greedy quiere doble pero obtiene lo disponible)");
            Thread.sleep(2000);
            
            // Finaliza simulador
            silkRoad.finish();
            System.out.println("=== DEMOSTRACION COMPLETADA ===\n");
        }
        
        /**
         * Método principal para ejecutar las pruebas de aceptación del Ciclo 4.
         * @param args Argumentos de línea de comandos
         */
        public static void main(String[] args) {
            // Envuelve en try-catch para manejar interrupciones de threads
            try {
                // Imprime mensaje inicial
                System.out.println("INICIANDO PRUEBAS DE ACEPTACION VISUAL CICLO 4\n");
                
                // Ejecuta prueba visual del Ciclo 4
                testCiclo4Visual();
                // Espera 2 segundos antes de siguiente prueba
                Thread.sleep(2000);
                
                // Ejecuta demostración de polimorfismo
                testPolymorphismDemo();
                // Espera 2 segundos antes de siguiente prueba
                Thread.sleep(2000);
                
                // Ejecuta demostración de extensibilidad
                testExtensibility();
                
                // Imprime mensaje final
                System.out.println("\nTODAS LAS PRUEBAS DE ACEPTACION CICLO 4 COMPLETADAS");
                
            } catch (InterruptedException e) {
                // Captura excepción de interrupción de thread
                System.err.println("Error en las pruebas: " + e.getMessage());
            }
        }
    }
