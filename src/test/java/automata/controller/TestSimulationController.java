package automata.controller;

import automata.Grid;
import automata.State;
import automata.rules.ConwaysRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSimulationController {
    @Test
    void toggleSimulationStartsAndStopsTimerAndEditing() {
        Grid grid = new Grid(new ConwaysRule(), 1, 1);
        GridInteractionController gridInteractionController = new GridInteractionController(grid, 10);
        SimulationController simulationController =
            new SimulationController(grid, gridInteractionController, 100_000);

        boolean started = simulationController.toggleSimulation();
        boolean stopped = simulationController.toggleSimulation();

        assertTrue(started);
        assertFalse(stopped);
        assertFalse(simulationController.isRunning());
        assertTrue(gridInteractionController.isEditingEnabled());
    }

    @Test
    void resetRestoresSavedSnapshot() { //Also in TestGridSnapshot
        Grid grid = new Grid(new ConwaysRule(), 1, 1);
        GridInteractionController gridInteractionController = new GridInteractionController(grid, 10);
        SimulationController simulationController =
            new SimulationController(grid, gridInteractionController, 100_000);
        grid.setState(0, 0, State.ALIVE);

        simulationController.startSimulation();
        grid.setState(0, 0, State.DEAD);
        simulationController.resetSimulation();

        assertEquals(State.ALIVE, grid.getState(0, 0));
        assertFalse(simulationController.isRunning());
        assertTrue(gridInteractionController.isEditingEnabled());
    }

    @Test
    void clearStopsSimulationAndClearsGrid() {
        Grid grid = new Grid(new ConwaysRule(), 1, 1);
        GridInteractionController gridInteractionController = new GridInteractionController(grid, 10);
        SimulationController simulationController =
            new SimulationController(grid, gridInteractionController, 100_000);
        grid.setState(0, 0, State.ALIVE);

        simulationController.startSimulation();
        simulationController.clearGrid();

        assertEquals(State.DEAD, grid.getState(0, 0));
        assertFalse(simulationController.isRunning());
        assertTrue(gridInteractionController.isEditingEnabled());
    }
}
