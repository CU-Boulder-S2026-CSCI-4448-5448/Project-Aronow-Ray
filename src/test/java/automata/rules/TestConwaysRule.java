package automata.rules;

import automata.CellFactory;
import automata.Grid;
import automata.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConwaysRule {
    @Test
    void liveCellWithTwoNeighborsSurvives() {
        ConwaysRule rule = new ConwaysRule();
        Grid grid = new Grid(new CellFactory(), rule, 2, 2);
        grid.setState(1, 1, State.ALIVE);
        grid.setState(1, 0, State.ALIVE);
        grid.setState(0, 1, State.ALIVE);

        assertEquals(State.ALIVE, rule.getNextStateForCell(grid, 1, 1));
    }

    @Test
    void deadCellWithThreeNeighborsBecomesAlive() {
        ConwaysRule rule = new ConwaysRule();
        Grid grid = new Grid(new CellFactory(), rule, 2, 2);
        grid.setState(0, 1, State.ALIVE);
        grid.setState(1, 0, State.ALIVE);
        grid.setState(1, 2, State.ALIVE);

        assertEquals(State.ALIVE, rule.getNextStateForCell(grid, 1, 1));
    }
}
