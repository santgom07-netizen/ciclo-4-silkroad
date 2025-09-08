/**
 * Robot class in the Silk Road.
 * A robot is represented as a circle in the visual simulator.
 */
public class Robot extends Circle {
    private int location;
    private int initialLocation;

    /**
     * Create a robot at a given position.
     * @param location Initial position of the robot
     */
    public Robot(int location) {
        super();
        this.location = location;
        this.initialLocation = location;
        changeColor("blue");
        makeVisible();
    }

    /**
     * @return Robot location
     */
    public int getLocation() {
        return location;
    }

    /**
     * Move the robot a certain number of meters.
     * @param meters Steps to move (positive or negative)
     * @param roadLength The length of the Silk Road
     * The conditions are:
     * <ol>
     *   <li>The new position must be inside the route (>= 0 and &lt; roadLength)</li>
     * </ol>
     */
    public void move(int meters, int roadLength) {
        int newPos = location + meters;
        if (newPos >= 0 && newPos < roadLength) {
            moveHorizontal(meters * 20); // escala visual
            location = newPos;
        }
    }

    /**
     * Reset robot to its initial position.
     */
    public void resetPosition() {
        moveHorizontal((initialLocation - location) * 20);
        location = initialLocation;
    }
}
