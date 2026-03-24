package automata;

import java.util.Objects;

public class Cell {
    private State state;

    public Cell(State state) {
        this.state = Objects.requireNonNull(state, "state");
    }

    public State getState() {
        return state;
    }

    public void updateState(State state) {
        this.state = Objects.requireNonNull(state, "state");
    }
}
