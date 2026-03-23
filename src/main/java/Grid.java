import java.util.HashMap;
import java.util.Map;

public class Grid {
    IRule rule;
    CellFactory cellFactory;
    private Cell[][] grid;
    private int MAX_ROWS = 99;
    private int MAX_COLUMNS = 99;


    public Grid(CellFactory cellFactory, IRule rule) {
        this.grid = new Cell[MAX_ROWS + 1][MAX_COLUMNS + 1];
        this.cellFactory = cellFactory;
        this.rule = rule;
        for (int x = 0; x <= MAX_ROWS; x++) {
            for (int y = 0; y <= MAX_COLUMNS; y++) {
                grid[x][y] = cellFactory.createCell(State.DEAD);
            }
        }
    }

    public void updateAllCells () {
        for (int x = 0; x <= MAX_ROWS; x++) {
            for (int y = 0; y <= MAX_COLUMNS; y++) {
                Cell cell = grid[x][y];
                State newState = rule.getNextStateForCell(grid, x, y);
                cell.updateState(newState);
            }
        }
    }

    public int getMaxRows() {
        return MAX_ROWS;
    }

    public int getMaxColumns() {
        return MAX_COLUMNS;
    }

    public void setMaxRows (int x) {
        MAX_ROWS = x;
    }

    public void setMaxColumns (int y) {
        MAX_COLUMNS = y;
    }
}
