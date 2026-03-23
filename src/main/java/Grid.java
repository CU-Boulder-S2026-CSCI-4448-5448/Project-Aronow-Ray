import java.util.HashMap;
import java.util.Map;

public class Grid {

    // TODO: NOTE, it may make more sense to handle neighbors in Grid rather than Cell.

    Map<Position, Cell> cells = new HashMap<>();
    private int MAX_ROWS = 99;
    private int MAX_COLUMNS = 99;

    // Creates a (MAX_X + 1)x(MAX_Y + 1) grid
    public Grid() {
        for (int x = 0; x <= MAX_ROWS; x++) {
            for (int y = 0; y <= MAX_COLUMNS; y++) {
                Position pos = new Position(x, y);
                cells.put(pos, new Cell(x, y, State.DEAD));
            }
        }
    }

    //TODO
    public void updateAllCells () {

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
