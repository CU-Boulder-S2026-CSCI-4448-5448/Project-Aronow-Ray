package automata.presets;

import automata.Grid;
import automata.State;

import java.util.List;

public class CloverleafPreset implements GridPreset, Shape {

    @Override
    public String getName() {
        return "Cloverleaf";
    }

    // Taken from: https://www.researchgate.net/figure/Examples-of-stable-patterns-in-Conways-Game-of-Life_fig2_320019435
    @Override
    public List<int[]> getRelativeCells() {
        return List.of(
                new int[]{ 0,  3},
                new int[]{ 0,  5},
                new int[]{ 1,  1},
                new int[]{ 1,  2},
                new int[]{ 1,  3},
                new int[]{ 1,  5},
                new int[]{ 1,  6},
                new int[]{ 1,  7},
                new int[]{ 2,  0},
                new int[]{ 2,  4},
                new int[]{ 2,  8},
                new int[]{ 3,  0},
                new int[]{ 3,  2},
                new int[]{ 3,  6},
                new int[]{ 3,  8},
                new int[]{ 4,  1},
                new int[]{ 4,  2},
                new int[]{ 4,  4},
                new int[]{ 4,  6},
                new int[]{ 4,  7},
                new int[]{ 6,  1},
                new int[]{ 6,  2},
                new int[]{ 6,  4},
                new int[]{ 6,  6},
                new int[]{ 6,  7},
                new int[]{ 7,  0},
                new int[]{ 7,  2},
                new int[]{ 7,  6},
                new int[]{ 7,  8},
                new int[]{ 8,  0},
                new int[]{ 8,  4},
                new int[]{ 8,  8},
                new int[]{ 9,  1},
                new int[]{ 9,  2},
                new int[]{ 9,  3},
                new int[]{ 9,  5},
                new int[]{ 9,  6},
                new int[]{ 9,  7},
                new int[]{10,  3},
                new int[]{10,  5}
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
