package automata.controller;

import automata.Grid;
import automata.State;
import automata.rules.ConwaysRule;
import automata.rules.RockPaperScissorsRule;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void rockPaperScissorsPaintUsesSelectedDrawState() {
        Grid grid = new Grid(new RockPaperScissorsRule(), 1, 1);
        GridInteractionController controller = new GridInteractionController(grid, 10);
        controller.setSelectedDrawState(State.PAPER);

        controller.mousePressed(mouseEvent(MouseEvent.MOUSE_PRESSED, 5, 5));

        assertEquals(State.PAPER, grid.getState(0, 0));
    }

    @Test
    void rockPaperScissorsRightClickResetsToDefaultState() {
        Grid grid = new Grid(new RockPaperScissorsRule(), 1, 1);
        GridInteractionController controller = new GridInteractionController(grid, 10);
        grid.setState(0, 0, State.SCISSORS);

        controller.mousePressed(mouseEvent(MouseEvent.MOUSE_PRESSED, 5, 5, MouseEvent.BUTTON3));

        assertEquals(State.ROCK, grid.getState(0, 0));
    }

    @Test
    void largerBrushPaintsSquareArea() {
        Grid grid = new Grid(new ConwaysRule(), 2, 2);
        GridInteractionController controller = new GridInteractionController(grid, 10);
        controller.setBrushSize(3);

        controller.mousePressed(mouseEvent(MouseEvent.MOUSE_PRESSED, 15, 15));

        assertEquals(State.ALIVE, grid.getState(0, 0));
        assertEquals(State.ALIVE, grid.getState(0, 1));
        assertEquals(State.ALIVE, grid.getState(0, 2));
        assertEquals(State.ALIVE, grid.getState(1, 0));
        assertEquals(State.ALIVE, grid.getState(1, 1));
        assertEquals(State.ALIVE, grid.getState(1, 2));
        assertEquals(State.ALIVE, grid.getState(2, 0));
        assertEquals(State.ALIVE, grid.getState(2, 1));
        assertEquals(State.ALIVE, grid.getState(2, 2));
    }

    @Test
    void brushSizeMustBePositive() {
        Grid grid = new Grid(new ConwaysRule(), 0, 0);
        GridInteractionController controller = new GridInteractionController(grid, 10);

        assertThrows(IllegalArgumentException.class, () -> controller.setBrushSize(0));
    }

    private static MouseEvent mouseEvent(int eventType, int x, int y) {
        return new MouseEvent(new JPanel(), eventType, System.currentTimeMillis(), 0, x, y, 1, false);
    }

    private static MouseEvent mouseEvent(int eventType, int x, int y, int button) {
        return new MouseEvent(new JPanel(), eventType, System.currentTimeMillis(), 0, x, y, 1, false, button);
    }
}
