package automata.controller;

import automata.Grid;
import automata.State;
import automata.rules.ConwaysRule;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGridInteractionController {
    @Test
    void mouseInputUpdatesTargetCellAndDragUsesSameState() {
        Grid grid = new Grid(new ConwaysRule(), 1, 1);
        GridInteractionController controller = new GridInteractionController(grid, 10);

        controller.mousePressed(mouseEvent(MouseEvent.MOUSE_PRESSED, 5, 5));
        controller.mouseDragged(mouseEvent(MouseEvent.MOUSE_DRAGGED, 15, 5));

        assertEquals(State.ALIVE, grid.getState(0, 0));
        assertEquals(State.ALIVE, grid.getState(0, 1));
    }

    @Test
    void disabledEditingIgnoresMouseInput() {
        Grid grid = new Grid(new ConwaysRule(), 0, 0);
        GridInteractionController controller = new GridInteractionController(grid, 10);
        controller.setEditingEnabled(false);

        controller.mousePressed(mouseEvent(MouseEvent.MOUSE_PRESSED, 5, 5));

        assertEquals(State.DEAD, grid.getState(0, 0));
    }

    @Test
    void editingEnabledDefaultsToTrue() {
        Grid grid = new Grid(new ConwaysRule(), 0, 0);
        GridInteractionController controller = new GridInteractionController(grid, 10);

        assertTrue(controller.isEditingEnabled());
    }

    private static MouseEvent mouseEvent(int eventType, int x, int y) {
        return new MouseEvent(new JPanel(), eventType, System.currentTimeMillis(), 0, x, y, 1, false);
    }
}
