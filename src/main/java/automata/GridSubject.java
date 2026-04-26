package automata;

public interface GridSubject {
    void addObserver(GridObserver observer);

    void removeObserver(GridObserver observer);

    State getState(int row, int column);

    int getMaxRows();

    int getMaxColumns();

    void setState(int row, int col, State state);

    boolean isInBounds(int row, int column);

    StateSet getStateSet();
}
