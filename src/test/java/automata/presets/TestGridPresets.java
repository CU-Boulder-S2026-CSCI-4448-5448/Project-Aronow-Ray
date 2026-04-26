package automata.presets;

import automata.Grid;
import automata.State;
import automata.rules.ConwaysRule;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGridPresets {
    @Test
    void blinkerProvidesExpectedNameAndRelativeCells() {
        BlinkerPreset preset = new BlinkerPreset();

        assertEquals("Blinker", preset.getName());
        assertRelativeCells(
            List.of(new int[]{-1, 0}, new int[]{0, 0}, new int[]{1, 0}),
            preset.getRelativeCells()
        );
    }

    @Test
    void gliderProvidesExpectedNameAndRelativeCells() {
        GliderPreset preset = new GliderPreset();

        assertEquals("Glider", preset.getName());
        assertRelativeCells(
            List.of(new int[]{0, 1}, new int[]{1, 2}, new int[]{2, 0}, new int[]{2, 1}, new int[]{2, 2}),
            preset.getRelativeCells()
        );
    }

    @Test
    void blinkerApplyStampsAliveCellsAtGridCenter() {
        Grid grid = new Grid(new ConwaysRule(), 4, 4);

        new BlinkerPreset().apply(grid);

        assertEquals(State.ALIVE, grid.getState(1, 2));
        assertEquals(State.ALIVE, grid.getState(2, 2));
        assertEquals(State.ALIVE, grid.getState(3, 2));
        assertEquals(State.DEAD, grid.getState(2, 1));
    }

    @Test
    void gliderApplyStampsAliveCellsAtGridCenter() {
        Grid grid = new Grid(new ConwaysRule(), 6, 6);

        new GliderPreset().apply(grid);

        assertEquals(State.ALIVE, grid.getState(3, 4));
        assertEquals(State.ALIVE, grid.getState(4, 5));
        assertEquals(State.ALIVE, grid.getState(5, 3));
        assertEquals(State.ALIVE, grid.getState(5, 4));
        assertEquals(State.ALIVE, grid.getState(5, 5));
        assertEquals(State.DEAD, grid.getState(3, 3));
    }

    @Test
    void cloverleafProvidesExpectedNameAndRelativeCells() {
        CloverleafPreset preset = new CloverleafPreset();

        assertEquals("Cloverleaf", preset.getName());
        assertRelativeCells(
            List.of(
                new int[]{ 0,  3}, new int[]{ 0,  5},
                new int[]{ 1,  1}, new int[]{ 1,  2}, new int[]{ 1,  3}, new int[]{ 1,  5}, new int[]{ 1,  6}, new int[]{ 1,  7},
                new int[]{ 2,  0}, new int[]{ 2,  4}, new int[]{ 2,  8},
                new int[]{ 3,  0}, new int[]{ 3,  2}, new int[]{ 3,  6}, new int[]{ 3,  8},
                new int[]{ 4,  1}, new int[]{ 4,  2}, new int[]{ 4,  4}, new int[]{ 4,  6}, new int[]{ 4,  7},
                new int[]{ 6,  1}, new int[]{ 6,  2}, new int[]{ 6,  4}, new int[]{ 6,  6}, new int[]{ 6,  7},
                new int[]{ 7,  0}, new int[]{ 7,  2}, new int[]{ 7,  6}, new int[]{ 7,  8},
                new int[]{ 8,  0}, new int[]{ 8,  4}, new int[]{ 8,  8},
                new int[]{ 9,  1}, new int[]{ 9,  2}, new int[]{ 9,  3}, new int[]{ 9,  5}, new int[]{ 9,  6}, new int[]{ 9,  7},
                new int[]{10,  3}, new int[]{10,  5}
            ),
            preset.getRelativeCells()
        );
    }

    @Test
    void cloverleafApplyStampsAliveCellsAtGridCenter() {
        Grid grid = new Grid(new ConwaysRule(), 25, 25); // centerRow=12, centerCol=12

        new CloverleafPreset().apply(grid);

        assertEquals(State.ALIVE, grid.getState(12, 15)); // {0, 3}
        assertEquals(State.ALIVE, grid.getState(13, 13)); // {1, 1}
        assertEquals(State.ALIVE, grid.getState(22, 15)); // {10, 3}
        assertEquals(State.DEAD,  grid.getState(17, 17)); // row 5 has no entries
    }

    @Test
    void hammerheadProvidesExpectedNameAndRelativeCells() {
        HammerheadPreset preset = new HammerheadPreset();

        assertEquals("Hammerhead", preset.getName());
        assertRelativeCells(
            List.of(
                new int[]{ 0,  0}, new int[]{ 0,  1}, new int[]{ 0,  2}, new int[]{ 0,  3}, new int[]{ 0,  4},
                new int[]{ 1,  0}, new int[]{ 1,  5}, new int[]{ 1, 13}, new int[]{ 1, 14},
                new int[]{ 2,  0}, new int[]{ 2, 12}, new int[]{ 2, 13}, new int[]{ 2, 15}, new int[]{ 2, 16}, new int[]{ 2, 17},
                new int[]{ 3,  1}, new int[]{ 3, 11}, new int[]{ 3, 12}, new int[]{ 3, 14}, new int[]{ 3, 15}, new int[]{ 3, 16}, new int[]{ 3, 17},
                new int[]{ 4,  3}, new int[]{ 4,  4}, new int[]{ 4,  8}, new int[]{ 4,  9}, new int[]{ 4, 11}, new int[]{ 4, 12}, new int[]{ 4, 15}, new int[]{ 4, 16},
                new int[]{ 5,  5}, new int[]{ 5, 10}, new int[]{ 5, 13},
                new int[]{ 6,  6}, new int[]{ 6,  8}, new int[]{ 6, 10}, new int[]{ 6, 12},
                new int[]{ 7,  7},
                new int[]{ 8,  7},
                new int[]{ 9,  6}, new int[]{ 9,  8}, new int[]{ 9, 10}, new int[]{ 9, 12},
                new int[]{10,  5}, new int[]{10, 10}, new int[]{10, 13},
                new int[]{11,  3}, new int[]{11,  4}, new int[]{11,  8}, new int[]{11,  9}, new int[]{11, 11}, new int[]{11, 12}, new int[]{11, 15},
                new int[]{12,  1}, new int[]{12, 11}, new int[]{12, 12}, new int[]{12, 14}, new int[]{12, 15}, new int[]{12, 16}, new int[]{12, 17},
                new int[]{13,  0}, new int[]{13, 12}, new int[]{13, 13}, new int[]{13, 15}, new int[]{13, 16}, new int[]{13, 17},
                new int[]{14,  0}, new int[]{14,  5}, new int[]{14, 13}, new int[]{14, 14},
                new int[]{15,  0}, new int[]{15,  1}, new int[]{15,  2}, new int[]{15,  3}, new int[]{15,  4}
            ),
            preset.getRelativeCells()
        );
    }

    @Test
    void hammerheadApplyStampsAliveCellsAtGridCenter() {
        Grid grid = new Grid(new ConwaysRule(), 40, 45); // centerRow=20, centerCol=22

        new HammerheadPreset().apply(grid);

        assertEquals(State.ALIVE, grid.getState(20, 22)); // {0, 0}
        assertEquals(State.ALIVE, grid.getState(20, 26)); // {0, 4}
        assertEquals(State.ALIVE, grid.getState(27, 29)); // {7, 7}
        assertEquals(State.ALIVE, grid.getState(35, 26)); // {15, 4}
        assertEquals(State.DEAD,  grid.getState(20, 27)); // {0, 5} not in pattern
    }

    private static void assertRelativeCells(List<int[]> expected, List<int[]> actual) {
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertArrayEquals(expected.get(index), actual.get(index));
        }
    }
}
