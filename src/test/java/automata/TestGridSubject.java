package automata;

import automata.rules.ConwaysRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class TestGridSubject {
    @Test
    void gridImplementsGridSubject() {
        Grid grid = new Grid(new CellFactory(), new ConwaysRule(), 2, 3);

        assertInstanceOf(GridSubject.class, grid);
    }

    @Test
    void subjectReferenceExposesStateAndDimensions() {
        GridSubject subject = new Grid(new CellFactory(), new ConwaysRule(), 2, 3);

        assertEquals(2, subject.getMaxRows());
        assertEquals(3, subject.getMaxColumns());
        assertEquals(State.DEAD, subject.getState(0, 0));
    }
}
