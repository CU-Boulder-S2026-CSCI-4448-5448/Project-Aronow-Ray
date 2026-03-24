package automata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStateSet {
    @Test
    void conwaysLifeHasDeadAsDefaultAndSupportsAlive() {
        assertEquals(State.DEAD, StateSet.CONWAYS_LIFE.getDefaultState());
        assertTrue(StateSet.CONWAYS_LIFE.supports(State.ALIVE));
    }

    @Test
    void rockPaperScissorsSupportsOnlyRpsStates() {
        assertEquals(State.ROCK, StateSet.ROCK_PAPER_SCISSORS.getDefaultState());
        assertTrue(StateSet.ROCK_PAPER_SCISSORS.supports(State.PAPER));
        assertFalse(StateSet.ROCK_PAPER_SCISSORS.supports(State.DEAD));
    }
}
