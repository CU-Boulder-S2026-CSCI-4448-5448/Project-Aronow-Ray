package automata;

import automata.rules.ConwaysRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGridSnapshot {
    @Test
    void restoreRevertsGridToCapturedStates() {
        Grid grid = new Grid(new ConwaysRule(), 2, 2);
        grid.setState(0, 0, State.ALIVE);
        grid.setState(1, 1, State.ALIVE);
        GridSnapshot snapshot = new GridSnapshot(grid);

        grid.setState(0, 0, State.DEAD);
        grid.setState(2, 2, State.ALIVE);
        snapshot.restore(grid);

        assertEquals(State.ALIVE, grid.getState(0, 0));
        assertEquals(State.ALIVE, grid.getState(1, 1));
        assertEquals(State.DEAD, grid.getState(2, 2));
    }
}
