package automata;

// Save the grid state (allows reset button to work)
public class GridSnapshot {
    private final State[][] states;

    public GridSnapshot(Grid grid) {
        this.states = new State[grid.getMaxRows() + 1][grid.getMaxColumns() + 1];
        for (int row = 0; row <= grid.getMaxRows(); row++)
            for (int col = 0; col <= grid.getMaxColumns(); col++)
                this.states[row][col] = grid.getState(row, col);
    }

    public void restore(Grid grid) {
        for (int row = 0; row < states.length; row++)
            for (int col = 0; col < states[row].length; col++)
                grid.setState(row, col, states[row][col]);
    }
}