package silkroad; 

import shapes.Circle;  
import java.util.List;
import java.util.ArrayList;

/**
* Clase abstracta base para todos los robots en la Ruta de la Seda.
* Define comportamiento comun y permite extensibilidad mediante herencia.
* 
* @author Santiago Andres Gomez Rojas y Miguel Angel Sandoval
*/

public abstract class Robot {
        protected Circle visualRepresentation;
        protected int location;
        protected int initialLocation;
        protected List<Integer> gainsHistory;
        protected int totalGain;
        protected String originalColor;
        
        /**
         * Constructor protegido para subclases.
         * @param location Posicion inicial del robot en la ruta (0 <= location < length)
         * @param color Color del robot para visualizacion
         * @param x Coordenada X visual en pixels
         * @param y Coordenada Y visual en pixels
         */
        protected Robot(int location, String color, int x, int y) {
            this.location = location;
            this.initialLocation = location;
            this.gainsHistory = new ArrayList<>();
            this.totalGain = 0;
            this.originalColor = color;
            
            this.visualRepresentation = new Circle();
            visualRepresentation.changeColor(color);
            visualRepresentation.changeSize(20);
            visualRepresentation.moveHorizontal(x + 5 - visualRepresentation.getXPosition());
            visualRepresentation.moveVertical(y + 5 - visualRepresentation.getYPosition());
            visualRepresentation.makeVisible();
        }
        
        /**
         * Obtiene la ubicacion actual del robot en la ruta.
         * @return Ubicacion actual en la ruta
         */
        public int getLocation() {
            return location;
        }
        
        /**
         * Obtiene la ubicacion inicial del robot antes de cualquier movimiento.
         * @return Ubicacion inicial en la ruta
         */
        public int getInitialLocation() {
            return initialLocation;
        }
        
        /**
         * Obtiene el historial completo de ganancias del robot.
         * Cada elemento representa la ganancia obtenida en cada movimiento.
         * @return Lista con las ganancias por cada movimiento del robot
         */
        public List<Integer> getGainsHistory() {
            return new ArrayList<>(gainsHistory);
        }
        
        /**
         * Obtiene la ganancia total acumulada por el robot.
         * @return Ganancia total acumulada desde el inicio del simulador
         */
        public int getTotalGain() {
            return totalGain;
        }
        
        /**
         * Metodo abstracto que define como el robot recolecta dinero de una tienda.
         * Cada tipo de robot implementa su propia estrategia de recoleccion.
         * @param store Tienda de la que recolectar dinero. No debe ser null
         * @return Cantidad de dinero recolectada por el robot
         */
        public abstract int collectFromStore(Store store);
        
        /**
         * Metodo abstracto que indica si el robot puede volver a su posicion inicial.
         * Cada tipo de robot define su propio comportamiento de retorno.
         * @return true si el robot puede volver, false si no puede retornar
         */
        public abstract boolean canReturnToInitial();
        
        /**
         * Agrega una ganancia al historial del robot.
         * @param gain Ganancia obtenida en el movimiento (>= 0)
         */
        public void addGain(int gain) {
            gainsHistory.add(gain);
            totalGain += gain;
        }
        
        /**
         * Mueve el robot a una nueva ubicacion en la ruta.
         * Actualiza tanto la posicion logica como la representacion visual.
         * @param newLocation Nueva posicion logica en la ruta
         * @param x Nueva coordenada X para la visualizacion en pixels
         * @param y Nueva coordenada Y para la visualizacion en pixels
         */
        public void moveTo(int newLocation, int x, int y) {
            int currentX = visualRepresentation.getXPosition();
            int currentY = visualRepresentation.getYPosition();
            visualRepresentation.moveHorizontal((x + 5) - currentX);
            visualRepresentation.moveVertical((y + 5) - currentY);
            this.location = newLocation;
        }
        
        /**
         * Devuelve el robot a su posicion inicial.
         * Solo funciona si canReturnToInitial() retorna true.
         * @param x Coordenada X de la posicion inicial en pixels
         * @param y Coordenada Y de la posicion inicial en pixels
         */
        public void returnToInitialPosition(int x, int y) {
            if (canReturnToInitial()) {
                moveTo(initialLocation, x, y);
            }
        }
        
        /**
         * Reinicia el robot a su estado inicial.
         * Limpia el historial de ganancias y lo devuelve a posicion inicial.
         */
        public void reset() {
            location = initialLocation;
            gainsHistory.clear();
            totalGain = 0;
        }
        
        /**
         * Hace que el robot parpadee para indicar maxima ganancia.
         * El parpadeo ocurre en un thread separado.
         * @param times Numero de veces que debe parpadear el robot
         */
        public void blink(int times) {
            new Thread(() -> {
                for (int i = 0; i < times; i++) {
                    visualRepresentation.makeInvisible();
                    try { Thread.sleep(200); } catch (InterruptedException e) {}
                    visualRepresentation.makeVisible();
                    try { Thread.sleep(200); } catch (InterruptedException e) {}
                }
            }).start();
        }
        
        /**
         * Obtiene el color actual del robot.
         * @return Color del robot
         */
        public String getColor() {
            return originalColor;
        }
        
        /**
         * Cambia el color del robot tanto logico como visualmente.
         * @param color Nuevo color para el robot
         */
        public void changeColor(String color) {
            this.originalColor = color;
            visualRepresentation.changeColor(color);
        }
        
        /**
         * Obtiene el tipo del robot como String.
         * @return Tipo del robot ("normal", "neverback", "tender", "greedy", etc.)
         */
        public abstract String getType();
        
        /**
         * Hace visible el robot en el canvas.
         */
        public void makeVisible() {
            visualRepresentation.makeVisible();
        }
        
        /**
         * Hace invisible el robot en el canvas.
         */
        public void makeInvisible() {
            visualRepresentation.makeInvisible();
        }
    }