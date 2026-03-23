import java.util.HashMap;
import java.util.Map;

public class Cell {

    State state;
    public Cell(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public void updateState(State state) {
        this.state = state;
    }
}
