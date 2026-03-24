package automata;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestState {
    @Test
    void includesConwayStates() {
        EnumSet<State> allStates = EnumSet.allOf(State.class);

        assertTrue(allStates.contains(State.DEAD));
        assertTrue(allStates.contains(State.ALIVE));
    }

    @Test
    void includesRockPaperScissorsStates() {
        EnumSet<State> allStates = EnumSet.allOf(State.class);

        assertTrue(allStates.contains(State.ROCK));
        assertTrue(allStates.contains(State.PAPER));
        assertTrue(allStates.contains(State.SCISSORS));
    }
}
