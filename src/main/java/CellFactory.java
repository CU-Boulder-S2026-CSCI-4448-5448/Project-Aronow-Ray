public class CellFactory {

    //TODO: Currently using the setup in Cell
    public Cell makeCell (int x, int y, State state) {
        return new Cell(x, y, state);
    }
}
