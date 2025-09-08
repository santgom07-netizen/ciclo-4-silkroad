import java.util.ArrayList;
import java.util.List;

/**
 * SilkRoad simulator.
 * Allows creation of a route, management of stores and robots,
 * and basic simulation features.
 */
public class SilkRoad {
    private int length;
    private List<Store> stores;
    private List<Robot> robots;
    private boolean visible;
    private int profit;
    private boolean ok;

    /**
     * Create a Silk Road with a given length.
     * @param length The length of the route.
     */
    public SilkRoad(int length) {
        this.length = length;
        this.stores = new ArrayList<>();
        this.robots = new ArrayList<>();
        this.visible = false;
        this.profit = 0;
        this.ok = true;
    }

    /**
     * If it's possible, place a store at a given location.
     * @param location The store location
     * @param tenges Initial money in the store
     * The conditions are:
     * <ol>
     *   <li>The location is valid (inside the route)</li>
     *   <li>There is not already a store at that location</li>
     * </ol>
     */
    public void placeStore(int location, int tenges) {
        if (location < 0 || location >= length) {
            ok = false;
            return;
        }
        for (Store s : stores) {
            if (s.getLocation() == location) {
                ok = false;
                return;
            }
        }
        stores.add(new Store(location, tenges));
        ok = true;
    }

    /**
     * Remove a store from a given location.
     * @param location The store location
     */
    public void removeStore(int location) {
        stores.removeIf(s -> s.getLocation() == location);
        ok = true;
    }

    /**
     * If it's possible, place a robot at a given location.
     * @param location Initial robot position
     * The conditions are:
     * <ol>
     *   <li>The location is valid (inside the route)</li>
     *   <li>There is not already a robot at that location</li>
     * </ol>
     */
    public void placeRobot(int location) {
        if (location < 0 || location >= length) {
            ok = false;
            return;
        }
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                ok = false;
                return;
            }
        }
        robots.add(new Robot(location));
        ok = true;
    }

    /**
     * Remove a robot from a given location.
     * @param location The robot location
     */
    public void removeRobot(int location) {
        robots.removeIf(r -> r.getLocation() == location);
        ok = true;
    }

    /**
     * Move a robot a certain number of meters.
     * @param location Current robot location
     * @param meters Steps to move
     * The conditions are:
     * <ol>
     *   <li>The robot exists at the given location</li>
     *   <li>The new position after moving is still valid (inside the route)</li>
     * </ol>
     */
    public void moveRobot(int location, int meters) {
        for (Robot r : robots) {
            if (r.getLocation() == location) {
                r.move(meters, length);
                ok = true;
                return;
            }
        }
        ok = false;
    }

    /**
     * Resupply all stores to their initial money.
     */
    public void resupplyStores() {
        for (Store s : stores) {
            s.resupply();
        }
        ok = true;
    }

    /**
     * Return all robots to their initial positions.
     */
    public void returnRobots() {
        for (Robot r : robots) {
            r.resetPosition();
        }
        ok = true;
    }

    /**
     * Restart the Silk Road:
     * <ol>
     *   <li>Resets all stores</li>
     *   <li>Returns all robots to their initial positions</li>
     *   <li>Resets the profit</li>
     * </ol>
     */
    public void reboot() {
        for (Store s : stores) {
            s.reset();
        }
        for (Robot r : robots) {
            r.resetPosition();
        }
        profit = 0;
        ok = true;
    }

    /**
     * @return Current profit
     */
    public int profit() {
        return profit;
    }

    /**
     * @return Number of stores
     */
    public int stores() {
        return stores.size();
    }

    /**
     * @return Number of robots
     */
    public int robots() {
        return robots.size();
    }

    /**
     * Make the simulator visible.
     */
    public void makeVisible() {
        visible = true;
    }

    /**
     * Make the simulator invisible.
     */
    public void makeInvisible() {
        visible = false;
    }

    /**
     * Finish the simulator:
     * <ol>
     *   <li>Clear all stores</li>
     *   <li>Clear all robots</li>
     *   <li>Reset profit</li>
     *   <li>Set visibility to false</li>
     * </ol>
     */
    public void finish() {
        stores.clear();
        robots.clear();
        profit = 0;
        visible = false;
        ok = true;
    }

    /**
     * @return Whether the last operation was successful
     */
    public boolean ok() {
        return ok;
    }
}
