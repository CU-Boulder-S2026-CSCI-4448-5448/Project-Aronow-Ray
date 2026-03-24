package automata;

import automata.rules.ConwaysRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGridObserver {
    @Test
    void observerReceivesCellUpdateNotification() {
        Grid grid = new Grid(new CellFactory(), new ConwaysRule(), 1, 1);
        RecordingObserver observer = new RecordingObserver();

        grid.addObserver(observer);
        grid.setState(0, 0, State.ALIVE);

        assertEquals(1, observer.notificationCount);
        assertEquals(0, observer.lastRow);
        assertEquals(0, observer.lastColumn);
        assertEquals(State.DEAD, observer.previousState);
        assertEquals(State.ALIVE, observer.newState);
    }

    @Test
    void removedObserverStopsReceivingUpdates() {
        Grid grid = new Grid(new CellFactory(), new ConwaysRule(), 1, 1);
        RecordingObserver observer = new RecordingObserver();

        grid.addObserver(observer);
        grid.removeObserver(observer);
        grid.setState(0, 0, State.ALIVE);

        assertEquals(0, observer.notificationCount);
    }

    private static final class RecordingObserver implements GridObserver {
        private int notificationCount;
        private int lastRow = -1;
        private int lastColumn = -1;
        private State previousState;
        private State newState;

        @Override
        public void cellUpdated(GridSubject subject, int row, int column, State previousState, State newState) {
            notificationCount++;
            lastRow = row;
            lastColumn = column;
            this.previousState = previousState;
            this.newState = newState;
        }
    }
}
