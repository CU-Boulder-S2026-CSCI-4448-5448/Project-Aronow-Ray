package automata.controller;

import automata.Grid;
import automata.GridSnapshot;

import javax.swing.Timer;
import java.util.Objects;

public class SimulationController {
    private final Grid grid;
    private final GridInteractionController gridInteractionController;
    private final Timer simulationTimer;
    private GridSnapshot savedSnapshot;

    public SimulationController(Grid grid, GridInteractionController gridInteractionController, int stepIntervalMillis) {
        if (stepIntervalMillis <= 0) {
            throw new IllegalArgumentException("Step interval must be positive.");
        }

        this.grid = Objects.requireNonNull(grid, "grid");
        this.gridInteractionController = Objects.requireNonNull(gridInteractionController, "gridInteractionController");
        this.simulationTimer = new Timer(stepIntervalMillis, event -> grid.updateAllCells());
    }

    public boolean toggleSimulation() {
        if (simulationTimer.isRunning()) {
            stopSimulation();
            return false;
        }

        startSimulation();
        return true;
    }

    public void startSimulation() {
        if (simulationTimer.isRunning()) {
            return;
        }

        savedSnapshot = new GridSnapshot(grid);
        simulationTimer.start();
        gridInteractionController.setEditingEnabled(false);
    }

    public void stopSimulation() {
        simulationTimer.stop();
        gridInteractionController.setEditingEnabled(true);
    }

    public void resetSimulation() {
        if (savedSnapshot == null) {
            return;
        }

        simulationTimer.stop();
        savedSnapshot.restore(grid);
        gridInteractionController.setEditingEnabled(true);
    }

    public void clearGrid() {
        simulationTimer.stop();
        grid.clear();
        gridInteractionController.setEditingEnabled(true);
    }

    public boolean isRunning() {
        return simulationTimer.isRunning();
    }
}
