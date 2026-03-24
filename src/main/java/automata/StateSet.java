package automata;

import java.util.Set;

public enum StateSet {
    CONWAYS_LIFE(State.DEAD, Set.of(State.DEAD, State.ALIVE)),
    ROCK_PAPER_SCISSORS(State.ROCK, Set.of(State.ROCK, State.PAPER, State.SCISSORS));

    private final State defaultState;
    private final Set<State> states;

    StateSet(State defaultState, Set<State> states) {
        if (!states.contains(defaultState)) {
            throw new IllegalArgumentException("Default state must be part of the state set.");
        }

        this.defaultState = defaultState;
        this.states = Set.copyOf(states);
    }

    public State getDefaultState() {
        return defaultState;
    }

    public Set<State> getStates() {
        return states;
    }

    public boolean supports(State state) {
        return states.contains(state);
    }
}
