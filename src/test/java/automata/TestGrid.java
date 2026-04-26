package automata;

import automata.rules.Rule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestGrid {
    @Test
    void constructorUsesRuleDefaultStateForAllCells() {
        Grid grid = new Grid(new FixedStateRule(StateSet.ROCK_PAPER_SCISSORS), 1, 1);

        assertEquals(State.ROCK, grid.getState(0, 0));
        assertEquals(State.ROCK, grid.getState(1, 1));
    }

    @Test
    void setStateRejectsStateOutsideRuleStateSet() {
        Grid grid = new Grid(new FixedStateRule(StateSet.CONWAYS_LIFE), 1, 1);

        assertThrows(IllegalArgumentException.class, () -> grid.setState(0, 0, State.ROCK));
    }

    private static final class FixedStateRule implements Rule {
        private final StateSet stateSet;

        private FixedStateRule(StateSet stateSet) {
            this.stateSet = stateSet;
        }

        @Override
        public State getNextStateForCell(Grid grid, int xLocation, int yLocation) {
            return stateSet.getDefaultState();
        }

        @Override
        public StateSet getStateSet() {
            return stateSet;
        }
    }
}
