package automata.display;

import automata.*;
import automata.controller.GridInteractionController;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GridPanel extends JPanel implements GridObserver {
    private final GridSubject grid;
    private final int columns;
    private final int rows;
    private final int cellSize;
    private final GridInteractionController interactionController;

    public GridPanel(GridSubject grid, int cellSize, GridInteractionController interactionController) {
        this.grid = grid;
        this.cellSize = cellSize;
        this.columns = grid.getMaxColumns() + 1;
        this.rows = grid.getMaxRows() + 1;
        this.interactionController = interactionController;
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));
        grid.addObserver(this);

        // register the interaction controller as the mouse listener
        addMouseListener(interactionController);
        addMouseMotionListener(interactionController);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                State state = grid.getState(row, column);
                graphics2d.setColor(getColorForState(state)); // color in the cell depending on state
                graphics2d.fillRect(column * cellSize, row * cellSize, cellSize, cellSize);
                graphics2d.setColor(Color.LIGHT_GRAY); // grid line color
                graphics2d.drawRect(column * cellSize, row * cellSize, cellSize, cellSize);
            }
        }

        // draw ghost preview for line tool
        graphics2d.setColor(new Color(20, 100, 100, 50));
        for (int[] cell : interactionController.getLinePreview()) {
            graphics2d.fillRect(cell[1] * cellSize, cell[0] * cellSize, cellSize, cellSize);
        }
    }

    @Override
    public void cellUpdated(GridSubject subject, int row, int column, State previousState, State newState) {
        Runnable repaintTask = () -> repaint(column * cellSize, row * cellSize, cellSize, cellSize);
        if (SwingUtilities.isEventDispatchThread()) {
            repaintTask.run();
            return;
        }
        SwingUtilities.invokeLater(repaintTask);
    }

    private Color getColorForState(State state) {
        return switch (state) {
            case ALIVE -> Color.BLACK;
            case ROCK -> Color.GRAY;
            case PAPER -> Color.WHITE;
            case SCISSORS -> Color.RED;
            case DEAD -> new Color(240, 240, 240);
        };
    }
}
