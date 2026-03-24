package automata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class TestCellFactory {
    @Test
    void createCellUsesRequestedState() {
        CellFactory factory = new CellFactory();

        Cell cell = factory.createCell(State.ROCK);

        assertEquals(State.ROCK, cell.getState());
    }

    @Test
    void createCellReturnsNewInstanceEachTime() {
        CellFactory factory = new CellFactory();

        Cell first = factory.createCell(State.DEAD);
        Cell second = factory.createCell(State.DEAD);

        assertNotSame(first, second);
    }
}
