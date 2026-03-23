

public interface IRule {
    public State getNextStateForCell(Grid grid, int xLocation, int yLocation);
}