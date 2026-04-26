package automata.rules;

import automata.Grid;
import automata.State;
import automata.StateSet;

public interface Rule {
    String getName();

    State getNextStateForCell(Grid grid, int xLocation, int yLocation);

    StateSet getStateSet();
}
