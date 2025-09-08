/**
 * Store class in the Silk Road.
 * A store is represented as a rectangle in the visual simulator.
 */
public class Store extends Rectangle {
    private int location;
    private int tenges;
    private int initialTenges;

    /**
     * Create a store at a given location with initial money.
     * @param location Position of the store in the Silk Road
     * @param tenges Initial amount of money in the store
     */
    public Store(int location, int tenges) {
        super();
        this.location = location;
        this.tenges = tenges;
        this.initialTenges = tenges;
        changeColor("green");
        makeVisible();
    }

    /**
     * @return Store location
     */
    public int getLocation() {
        return location;
    }

    /**
     * @return Current stock of money
     */
    public int getTenges() {
        return tenges;
    }

    /**
     * Resupply store to its initial money.
     */
    public void resupply() {
        this.tenges = initialTenges;
    }

    /**
     * Reset store (same as resupply).
     */
    public void reset() {
        resupply();
    }
}
