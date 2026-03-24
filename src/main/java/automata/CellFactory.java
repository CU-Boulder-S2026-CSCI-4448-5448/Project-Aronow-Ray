package automata;

public class CellFactory {
    public Cell createCell(State state) {
        return new Cell(state);
    }
}
