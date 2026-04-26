package automata;

import automata.rules.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Grid implements GridSubject {
    public static final int DEFAULT_MAX_ROWS = 99;
    public static final int DEFAULT_MAX_COLUMNS = 99;

    private final Rule rule;
    private final Cell[][] grid;
    private final int maxRows;
    private final int maxColumns;
    private final List<GridObserver> observers = new ArrayList<>();

    public Grid(Rule rule, int maxRows, int maxColumns) {
        if (maxRows < 0 || maxColumns < 0) {
            throw new IllegalArgumentException("Grid dimensions must be non-negative.");
        }

        this.rule = Objects.requireNonNull(rule, "rule");
        this.maxRows = maxRows;
        this.maxColumns = maxColumns;
        this.grid = new Cell[maxRows + 1][maxColumns + 1];

        State initialState = rule.getStateSet().getDefaultState();
        for (int x = 0; x <= maxRows; x++) {
            for (int y = 0; y <= maxColumns; y++) {
                grid[x][y] = new Cell(initialState);
            }
        }
    }

    public Grid(Rule rule) {
        this(rule, DEFAULT_MAX_ROWS, DEFAULT_MAX_COLUMNS);
    }

    public void updateAllCells() {
        State[][] nextStates = new State[maxRows + 1][maxColumns + 1];
        for (int x = 0; x <= maxRows; x++) {
            for (int y = 0; y <= maxColumns; y++) {
                nextStates[x][y] = rule.getNextStateForCell(this, x, y);
            }
        }

        for (int x = 0; x <= maxRows; x++) {
            for (int y = 0; y <= maxColumns; y++) {
                updateCellState(x, y, nextStates[x][y]);
            }
        }
    }

    // clear all cells
    public void clear() {
        for (int row = 0; row <= maxRows; row++)
            for (int col = 0; col <= maxColumns; col++)
                setState(row, col, State.DEAD);
    }

    public Cell getCell(int row, int column) {
        validatePosition(row, column);
        return grid[row][column];
    }

    public State getState(int row, int column) {
        return getCell(row, column).getState();
    }

    public void setState(int row, int column, State state) {
        validatePosition(row, column);
        if (!rule.getStateSet().supports(state)) {
            throw new IllegalArgumentException("State " + state + " is not supported by this rule.");
        }
        updateCellState(row, column, state);
    }

    @Override
    public void addObserver(GridObserver observer) {
        observers.add(Objects.requireNonNull(observer, "observer"));
    }

    @Override
    public void removeObserver(GridObserver observer) {
        observers.remove(observer);
    }

    public boolean isInBounds(int row, int column) {
        return row >= 0 && row <= maxRows && column >= 0 && column <= maxColumns;
    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public int getMaxColumns() {
        return maxColumns;
    }

    private void validatePosition(int row, int column) {
        if (!isInBounds(row, column)) {
            throw new IndexOutOfBoundsException("Position out of bounds: (" + row + ", " + column + ")");
        }
    }

    private void updateCellState(int row, int column, State newState) {
        State previousState = grid[row][column].getState();
        if (previousState == newState) {
            return;
        }

        grid[row][column].updateState(newState);
        notifyCellUpdated(row, column, previousState, newState);
    }

    private void notifyCellUpdated(int row, int column, State previousState, State newState) {
        for (GridObserver observer : observers) {
            observer.cellUpdated(this, row, column, previousState, newState);
        }
    }
}
