package automata.rules;

import automata.Grid;
import automata.State;
import automata.StateSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRockPaperScissorsRule {
    @Test
    void scissorsBecomesRockWhenOutnumberedByRocks() {
        RockPaperScissorsRule rule = new RockPaperScissorsRule();
        Grid grid = new Grid(rule, 2, 2);
        grid.setState(1, 1, State.SCISSORS);
        grid.setState(0, 1, State.ROCK);
        grid.setState(1, 0, State.ROCK);

        assertEquals(State.ROCK, rule.getNextStateForCell(grid, 1, 1));
    }

    @Test
    void rockBecomesPaperWhenOutnumberedByPapers() {
        RockPaperScissorsRule rule = new RockPaperScissorsRule();
        Grid grid = new Grid(rule, 2, 2);
        grid.setState(1, 1, State.ROCK);
        grid.setState(0, 1, State.PAPER);
        grid.setState(1, 0, State.PAPER);

        assertEquals(State.PAPER, rule.getNextStateForCell(grid, 1, 1));
    }

    @Test
    void edgeCellsIgnoreOutOfBoundsNeighbors() {
        RockPaperScissorsRule rule = new RockPaperScissorsRule();
        Grid grid = new Grid(rule, 1, 1);
        grid.setState(0, 0, State.PAPER);
        grid.setState(0, 1, State.SCISSORS);
        grid.setState(1, 0, State.SCISSORS);

        assertEquals(State.SCISSORS, rule.getNextStateForCell(grid, 0, 0));
    }

    @Test
    void reportsRockPaperScissorsStateSet() {
        RockPaperScissorsRule rule = new RockPaperScissorsRule();

        assertEquals(StateSet.ROCK_PAPER_SCISSORS, rule.getStateSet());
    }
}
