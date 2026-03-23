import java.util.HashMap;
import java.util.Map;

public class Cell {

    State state;
    Position position;

    //TODO: maybe instead of a map we use a list, since we probably won't need to
    // Look up cells by position if we have the neighbors already, and the
    // visualization will just iterate over the entire list to show it
    Map<Position, Cell> neighbors = new HashMap<>();

    // TODO: NOTE, it may make more sense to handle neighbors in Grid rather than Cell.

    public Cell(int x, int y, State state) {
        this.position = new Position(x, y); // Unsure if we need this yet
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public void updateState(State state) {
        this.state = state;
    }

    //TODO
    public void setPosition() {

    }

    public Map<Position, Cell> getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(Map<Position, Cell> neighbors) {}

}
