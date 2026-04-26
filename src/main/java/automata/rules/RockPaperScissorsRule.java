package automata.rules;
import automata.Cell;
import automata.Grid;
import automata.State;
import automata.StateSet;

public class RockPaperScissorsRule implements Rule {

    @Override
    public String getName() {
        return "RockPaperScissors";
    }

    @Override
    public State getNextStateForCell(Grid grid, int xLocation, int yLocation) {
        int numScissors = 0;
        int numRocks = 0;
        int numPapers = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue;
                }

                int neighborRow = xLocation + rowOffset;
                int neighborColumn = yLocation + columnOffset;
                if (!grid.isInBounds(neighborRow, neighborColumn)) {
                    continue;
                }

                Cell neighborCell = grid.getCell(neighborRow, neighborColumn);
                State neighborState = neighborCell.getState();
                switch (neighborState) {
                    case SCISSORS:
                        numScissors++;
                        break;
                    case ROCK:
                        numRocks++;
                        break;
                    case PAPER:
                        numPapers++;
                        break;
                    default:
                        break;
                }
            }
        }
        State currentState = grid.getState(xLocation, yLocation);
        if (State.SCISSORS.equals(currentState) && numRocks > numScissors) {
            return State.ROCK;
        } else if (State.ROCK.equals(currentState) && numPapers > numRocks) {
            return State.PAPER;
        } else if (State.PAPER.equals(currentState) && numScissors > numPapers) {
            return State.SCISSORS;
        }

        return currentState;
    }
    @Override
    public StateSet getStateSet() {
        return StateSet.ROCK_PAPER_SCISSORS;
    }
}
