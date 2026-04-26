package automata.presets;

import automata.Grid;
import automata.State;

public class BlinkerPreset implements GridPreset{
    @Override
    public void apply(Grid grid) {
        // We want the glider in the center regardless of size
        int centerRow = grid.getMaxRows() / 2;
        int centerCol = grid.getMaxColumns() / 2;

        grid.setState(centerRow -1, centerCol -1, State.ALIVE);
        grid.setState(centerRow, centerCol, State.ALIVE);
        grid.setState(centerRow + 1,  + 1, State.ALIVE);
    }
}
