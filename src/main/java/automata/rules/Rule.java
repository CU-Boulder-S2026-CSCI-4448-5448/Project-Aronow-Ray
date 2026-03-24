package automata.rules;

import automata.Grid;
import automata.State;
import automata.StateSet;

public interface Rule {
    State getNextStateForCell(Grid grid, int xLocation, int yLocation);

    StateSet getStateSet();
}
