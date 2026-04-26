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

    private static void assertRelativeCells(List<int[]> expected, List<int[]> actual) {
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertArrayEquals(expected.get(index), actual.get(index));
        }
    }
}
