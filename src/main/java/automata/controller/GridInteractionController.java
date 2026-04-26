package automata.controller;

import automata.GridSubject;
import automata.State;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class GridInteractionController extends MouseAdapter {
    private final GridSubject grid;
    private final int cellSize;
    private boolean editingEnabled = true;
    private State dragTargetState = State.DEAD;

    public GridInteractionController(GridSubject grid, int cellSize) {
        if (cellSize <= 0) {
            throw new IllegalArgumentException("Cell size must be positive.");
        }

        this.grid = Objects.requireNonNull(grid, "grid");
        this.cellSize = cellSize;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (!editingEnabled) {
            return;
        }

        int row = event.getY() / cellSize;
        int column = event.getX() / cellSize;
        if (!grid.isInBounds(row, column)) {
            return;
        }

        State current = grid.getState(row, column);
        dragTargetState = current == State.ALIVE ? State.DEAD : State.ALIVE;
        grid.setState(row, column, dragTargetState);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (!editingEnabled) {
            return;
        }

        int row = event.getY() / cellSize;
        int column = event.getX() / cellSize;
        if (!grid.isInBounds(row, column)) {
            return;
        }

        grid.setState(row, column, dragTargetState);
    }

    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }
}
