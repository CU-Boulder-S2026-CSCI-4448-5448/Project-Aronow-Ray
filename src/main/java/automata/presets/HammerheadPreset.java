package automata.presets;

import automata.Grid;
import automata.State;

import java.util.List;

public class HammerheadPreset implements GridPreset, Shape {

    @Override
    public String getName() {
        return "Hammerhead";
    }
    // Taken from: https://www.researchgate.net/figure/Examples-of-stable-patterns-in-Conways-Game-of-Life_fig2_320019435
    // I input this by hand. It was painful.
    @Override
    public List<int[]> getRelativeCells() {
        return List.of(
                new int[]{ 0, 0},
                new int[]{ 0, 1},
                new int[]{ 0, 2},
                new int[]{ 0, 3},
                new int[]{ 0, 4},
                new int[]{ 1, 0},
                new int[]{ 1, 5},
                new int[]{ 1, 13},
                new int[]{ 1, 14},
                new int[]{ 2, 0},
                new int[]{ 2, 12},
                new int[]{ 2, 13},
                new int[]{ 2, 15},
                new int[]{ 2, 16},
                new int[]{ 2, 17},
                new int[]{ 3, 1},
                new int[]{ 3, 11},
                new int[]{ 3, 12},
                new int[]{ 3, 14},
                new int[]{ 3, 15},
                new int[]{ 3, 16},
                new int[]{ 3, 17},
                new int[]{ 4, 3},
                new int[]{ 4, 4},
                new int[]{ 4, 8},
                new int[]{ 4, 9},
                new int[]{ 4, 11},
                new int[]{ 4, 12},
                new int[]{ 4, 15},
                new int[]{ 4, 16},
                new int[]{ 5, 5},
                new int[]{ 5, 10},
                new int[]{ 5, 13},
                new int[]{ 6, 6},
                new int[]{ 6, 8},
                new int[]{ 6, 10},
                new int[]{ 6, 12},
                new int[]{ 7, 7},
                new int[]{ 8, 7},
                new int[]{ 9, 6},
                new int[]{ 9, 8},
                new int[]{ 9, 10},
                new int[]{ 9, 12},
                new int[]{ 10, 5},
                new int[]{ 10, 10},
                new int[]{ 10, 13},
                new int[]{ 11, 3},
                new int[]{ 11, 4},
                new int[]{ 11, 8},
                new int[]{ 11, 9},
                new int[]{ 11, 11},
                new int[]{ 11, 12},
                new int[]{ 11, 15},
                new int[]{ 12, 1},
                new int[]{ 12, 11},
                new int[]{ 12, 12},
                new int[]{ 12, 14},
                new int[]{ 12, 15},
                new int[]{ 12, 16},
                new int[]{ 12, 17},
                new int[]{ 13, 0},
                new int[]{ 13, 12},
                new int[]{ 13, 13},
                new int[]{ 13, 15},
                new int[]{ 13, 16},
                new int[]{ 13, 17},
                new int[]{ 14, 0},
                new int[]{ 14, 5},
                new int[]{ 14, 13},
                new int[]{ 14, 14},
                new int[]{ 15, 0},
                new int[]{ 15, 1},
                new int[]{ 15, 2},
                new int[]{ 15, 3},
                new int[]{ 15, 4}
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
