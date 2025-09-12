import java.util.ArrayList;
import java.util.List;

/**
 * Simulador de la Ruta de la Seda.
 * Permite la creación de una ruta, la gestión de tiendas y robots,
 * y funciones básicas de simulación con representación visual.
 */
public class SilkRoad {
    private int length;
    private List<Store> stores;
    private List<Robot> robots;
    private boolean visible;
    private int profit;
    private boolean ok;
    private String lastErrorMessage;
    private static final String[] STORE_COLORS = {"green", "red", "yellow", "magenta", "cyan", "orange"};
    private static final String[] ROBOT_COLORS = {"blue", "red", "green", "magenta", "orange", "pink"};

    /**
     * Crea una Ruta de la Seda con una longitud dada.
     * @param length La longitud de la ruta.
     */
    public SilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.visible = false;
        this.profit = 0;
        this.ok = true;
        this.lastErrorMessage = "";
    }

    /**
     * Si es posible, coloca una tienda en una ubicación dada.
     * @param location La ubicación de la tienda
     * @param tenges Dinero inicial en la tienda
     */
    public void placeStore(int location, int tenges) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicación fuera de límites: " + location;
            showErrorIfVisible();
            return;
        }
        
        for (Store s : stores) {
            if (s.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe una tienda en la ubicación: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        String color = STORE_COLORS[stores.size() % STORE_COLORS.length];
        int[] coords = getSpiralCoordinates(location);
        Store newStore = new Store(location, tenges, color, coords[0], coords[1]);
        
        if (!visible) {
            newStore.makeInvisible();
        }
        
        stores.add(newStore);
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Elimina una tienda de una ubicación dada.
     * @param location La ubicación de la tienda
     */
    public void removeStore(int location) {
        boolean removed = stores.removeIf(s -> {
            if (s.getLocation() == location) {
                s.makeInvisible();
                return true;
            }
            return false;
        });
        
        if (removed) {
            ok = true;
            lastErrorMessage = "";
        } else {
            ok = false;
            lastErrorMessage = "No se encontró una tienda en la ubicación: " + location;
            showErrorIfVisible();
        }
    }

    /**
     * Si es posible, coloca un robot en una ubicación dada.
     * @param location Ubicación inicial del robot
     */
    public void placeRobot(int location) {
        if (location < 0 || location >= length) {
            ok = false;
            lastErrorMessage = "Ubicación fuera de límites: " + location;
            showErrorIfVisible();
            return;
        }
        
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                ok = false;
                lastErrorMessage = "Ya existe un robot en la ubicación: " + location;
                showErrorIfVisible();
                return;
            }
        }
        
        String color = ROBOT_COLORS[robots.size() % ROBOT_COLORS.length];
        int[] coords = getSpiralCoordinates(location);
        Robot newRobot = new Robot(location, color, coords[0], coords[1]);
        
        if (!visible) {
            newRobot.makeInvisible();
        }
        
        robots.add(newRobot);
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Elimina un robot de una ubicación dada.
     * @param location La ubicación del robot
     */
    public void removeRobot(int location) {
        boolean removed = robots.removeIf(r -> {
            if (r.getLocation() == location) {
                r.makeInvisible();
                return true;
            }
            return false;
        });
        
        if (removed) {
            ok = true;
            lastErrorMessage = "";
        } else {
            ok = false;
            lastErrorMessage = "No se encontró un robot en la ubicación: " + location;
            showErrorIfVisible();
        }
    }

    /**
     * Mueve un robot cierta cantidad de pasos.
     * @param location Ubicación actual del robot
     * @param meters Pasos a mover
     */
    public void moveRobot(int location, int meters) {
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                int newLocation = r.getLocation() + meters;
                if (newLocation >= 0 && newLocation < length) {
                    int[] newCoords = getSpiralCoordinates(newLocation);
                    r.moveTo(newLocation, newCoords[0], newCoords[1]);
                    checkProfitAtLocation(newLocation);
                    ok = true;
                    lastErrorMessage = "";
                } else {
                    ok = false;
                    lastErrorMessage = "El robot no puede moverse a la ubicación: " + newLocation;
                    showErrorIfVisible();
                }
                return;
            }
        }
        ok = false;
        lastErrorMessage = "No se encontró un robot en la ubicación: " + location;
        showErrorIfVisible();
    }

    /**
     * Reabastece todas las tiendas a su dinero inicial.
     */
    public void resupplyStores() {
        for (Store s : stores) {
            s.resupply();
        }
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Devuelve todos los robots a sus posiciones iniciales.
     */
    public void returnRobots() {
        for (Robot r : robots) {
            int[] coords = getSpiralCoordinates(r.getInitialLocation());
            r.returnToInitialPosition(coords[0], coords[1]);
        }
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * Reinicia la Ruta de la Seda.
     */
    public void reboot() {
        resupplyStores();
        returnRobots();
        profit = 0;
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * @return Ganancia actual
     */
    public int profit() {
        return profit;
    }

    /**
     * @return Número de tiendas
     */
    public int stores() {
        return stores.size();
    }

    /**
     * @return Número de robots
     */
    public int robots() {
        return robots.size();
    }

    /**
     * @return Longitud de la ruta de la seda
     */
    public int length() {
        return length;
    }

    /**
     * Hace visible el simulador.
     */
    public void makeVisible() {
        visible = true;
        for (Store s : stores) {
            s.makeVisible();
        }
        for (Robot r : robots) {
            r.makeVisible();
        }
    }

    /**
     * Hace invisible el simulador.
     */
    public void makeInvisible() {
        visible = false;
        for (Store s : stores) {
            s.makeInvisible();
        }
        for (Robot r : robots) {
            r.makeInvisible();
        }
    }

    /**
     * Finaliza el simulador.
     */
    public void finish() {
        makeInvisible();
        stores.clear();
        robots.clear();
        profit = 0;
        visible = false;
        ok = true;
        lastErrorMessage = "";
    }

    /**
     * @return Indica si la última operación fue exitosa
     */
    public boolean ok() {
        return ok;
    }

    /**
     * Obtiene el último mensaje de error (para pruebas).
     * @return El último mensaje de error
     */
    public String getLastError() {
        return lastErrorMessage;
    }

    /**
     * Calcula coordenadas para posicionamiento en espiral.
     * @param position Posición lineal
     * @return Arreglo con las coordenadas [x, y]
     */
    private int[] getSpiralCoordinates(int position) {
        if (position < 0 || position >= length) {
            return new int[]{50, 50};
        }
        
        int x = 0, y = 0;
        int dx = 1, dy = 0;
        int steps = 1;
        int stepCount = 0;
        int directionChanges = 0;
        
        for (int i = 0; i < position; i++) {
            x += dx;
            y += dy;
            stepCount++;
            
            if (stepCount == steps) {
                stepCount = 0;
                int tempDx = dx;
                dx = -dy;
                dy = tempDx;
                directionChanges++;
                if (directionChanges % 2 == 0) {
                    steps++;
                }
            }
        }
        
        return new int[]{x * 40 + 200, y * 40 + 200};
    }

    /**
     * Verifica si hay ganancias en una ubicación dada.
     * @param location La ubicación a verificar
     */
    private void checkProfitAtLocation(int location) {
        for (Store s : stores) {
            if (s.getLocation() == location && s.getTenges() > 0) {
                profit += 1;
                s.decreaseTenges(1);
                break;
            }
        }
    }

    /**
     * Muestra un mensaje de error solo si es visible.
     */
    private void showErrorIfVisible() {
        if (visible && !lastErrorMessage.isEmpty()) {
            System.out.println("Error: " + lastErrorMessage);
        }
    }
}
