package automata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCell {
    @Test
    void constructorStoresInitialState() {
        Cell cell = new Cell(State.ALIVE);

        assertEquals(State.ALIVE, cell.getState());
    }

    @Test
    void updateStateRejectsNull() {
        Cell cell = new Cell(State.DEAD);

        assertThrows(NullPointerException.class, () -> cell.updateState(null));
    }
}
