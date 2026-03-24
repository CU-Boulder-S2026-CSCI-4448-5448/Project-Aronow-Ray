package automata;

public interface GridObserver {
    void cellUpdated(GridSubject subject, int row, int column, State previousState, State newState);
}
