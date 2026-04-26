package automata.presets;

import automata.Grid;
import automata.State;

import java.util.List;

public class GliderPreset implements GridPreset, Shape {

    @Override
    public String getName() {
        return "Glider";
    }

    @Override
    public List<int[]> getRelativeCells() {
        return List.of(
            new int[]{ 0,  1},
            new int[]{ 1,  2},
            new int[]{ 2,  0},
            new int[]{ 2,  1},
            new int[]{ 2,  2}
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
