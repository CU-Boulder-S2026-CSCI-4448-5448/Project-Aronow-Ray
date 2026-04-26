package automata.display;

import automata.State;
import automata.presets.BlinkerPreset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestGameOfLifeApp {
    @Test
    void builderAppliesInitialPresetToGrid() {
        GameOfLifeApp app = GameOfLifeApp.builder()
            .withRows(4)
            .withColumns(4)
            .withInitialState(new BlinkerPreset())
            .build();

        assertEquals(State.ALIVE, app.getGrid().getState(1, 2));
        assertEquals(State.ALIVE, app.getGrid().getState(2, 2));
        assertEquals(State.ALIVE, app.getGrid().getState(3, 2));
    }

    @Test
    void builderRejectsInvalidCellSize() {
        assertThrows(IllegalArgumentException.class, () -> GameOfLifeApp.builder().withCellSize(0).build());
    }

    @Test
    void builderRejectsInvalidStepInterval() {
        assertThrows(IllegalArgumentException.class, () -> GameOfLifeApp.builder().withStepIntervalMillis(0).build());
    }
}
