package automata.rules;

import automata.Grid;
import automata.State;
import automata.StateSet;

public class ConwaysRule implements Rule {

    @Override
    public State getNextStateForCell(Grid grid, int xLocation, int yLocation) {
        State currentState = grid.getState(xLocation, yLocation);
        int liveNeighbors = countLiveNeighbors(grid, xLocation, yLocation);

        if (State.ALIVE.equals(currentState)) {
            return liveNeighbors == 2 || liveNeighbors == 3 ? State.ALIVE : State.DEAD;
        }

        return liveNeighbors == 3 ? State.ALIVE : State.DEAD;
    }

    @Override
    public StateSet getStateSet() {
        return StateSet.CONWAYS_LIFE;
    }

    private int countLiveNeighbors(Grid grid, int xLocation, int yLocation) {
        int liveNeighbors = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue;
                }

                int neighborRow = xLocation + rowOffset;
                int neighborColumn = yLocation + columnOffset;
                if (grid.isInBounds(neighborRow, neighborColumn)
                    && State.ALIVE.equals(grid.getState(neighborRow, neighborColumn))) {
                    liveNeighbors++;
                }
            }
        }
        return liveNeighbors;
    }
}
