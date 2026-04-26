package automata.display;

import automata.Grid;
import automata.GridObserver;
import automata.GridSubject;
import automata.State;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridPanel extends JPanel implements GridObserver {
    private final GridSubject grid;
    private final int columns;
    private final int rows;
    private final int cellSize;
    private boolean editingEnabled = true;

    public GridPanel(Grid grid) {
        this(grid, 8);
    }

    public GridPanel(GridSubject grid, int cellSize) {
        this.grid = grid;
        this.cellSize = cellSize;
        this.columns = grid.getMaxColumns() + 1;
        this.rows = grid.getMaxRows() + 1;
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));
        grid.addObserver(this);

        // listener to detect user clicks/click+drags for interacting with UI
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private State dragTargetState;

            // single click detection
            @Override
            public void mousePressed(MouseEvent e) {
                if (!editingEnabled) return;
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (!grid.isInBounds(row, col)) return;
                State current = grid.getState(row, col);
                dragTargetState = (current == State.ALIVE) ? State.DEAD : State.ALIVE;
                grid.setState(row, col, dragTargetState);
            }

            // click and drag detection so we paint all cells passed over
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!editingEnabled) return;
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (!grid.isInBounds(row, col)) return;
                grid.setState(row, col, dragTargetState);
            }
        };

        // register listeners
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

    }

    void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                State state = grid.getState(row, column);
                graphics2d.setColor(getColorForState(state));
                graphics2d.fillRect(column * cellSize, row * cellSize, cellSize, cellSize);
                graphics2d.setColor(Color.LIGHT_GRAY);
                graphics2d.drawRect(column * cellSize, row * cellSize, cellSize, cellSize);
            }
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
