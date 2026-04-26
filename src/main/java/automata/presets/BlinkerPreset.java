package automata.presets;

import automata.Grid;
import automata.State;

import java.util.List;

public class BlinkerPreset implements GridPreset, Shape {

    @Override
    public String getName() {
        return "Blinker";
    }

    @Override
    public List<int[]> getRelativeCells() {
        return List.of(
            new int[]{-1, 0},
            new int[]{ 0, 0},
            new int[]{ 1, 0}
        );
    }

    @Override
    public void apply(Grid grid) {
        int centerRow = grid.getMaxRows() / 2;
        int centerCol = grid.getMaxColumns() / 2;
        for (int[] cell : getRelativeCells()) {
            grid.setState(centerRow + cell[0], centerCol + cell[1], State.ALIVE);
        }
    }
}
