package automata.presets;

import automata.Grid;
import automata.State;

public interface GridPreset extends Shape {
    // stamps the shape centered on the grid, implementors get this for free via getRelativeCells()
    default void apply(Grid grid) {
        int centerRow = grid.getMaxRows() / 2;
        int centerCol = grid.getMaxColumns() / 2;
        for (int[] cell : getRelativeCells()) {
            grid.setState(centerRow + cell[0], centerCol + cell[1], State.ALIVE);
        }
    }
}
