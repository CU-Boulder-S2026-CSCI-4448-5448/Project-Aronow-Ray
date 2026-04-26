package automata.display;

import automata.*;
import automata.presets.Shape;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GridPanel extends JPanel implements GridObserver {
    private final GridSubject grid;
    private final int columns;
    private final int rows;
    private final int cellSize;
    private boolean editingEnabled = true;
    private Tool currentTool = Tool.PAINT;
    private int[] lineStart = null;
    private List<int[]> linePreview = List.of();
    private Shape activeShape = null;
    private List<int[]> shapePreview = List.of();

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

            @Override
            public void mousePressed(MouseEvent e) {
                if (!editingEnabled) return;
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (!grid.isInBounds(row, col)) return;

                if (currentTool == Tool.LINE) {
                    if (lineStart == null) {
                        // first click — set the start point
                        lineStart = new int[]{row, col};
                        linePreview = getLineCells(row, col, row, col);
                        repaint();
                    } else {
                        // second click — apply the preview to the grid
                        for (int[] cell : linePreview) {
                            grid.setState(cell[0], cell[1], State.ALIVE);
                        }
                        lineStart = null;
                        linePreview = List.of();
                    }
                } else if (currentTool == Tool.SHAPE && activeShape != null) {
                    // stamp all preview cells onto the grid
                    for (int[] cell : shapePreview) {
                        if (grid.isInBounds(cell[0], cell[1])) {
                            grid.setState(cell[0], cell[1], State.ALIVE);
                        }
                    }
                } else {
                    // paint tool — toggle and track drag state
                    State current = grid.getState(row, col);
                    dragTargetState = (current == State.ALIVE) ? State.DEAD : State.ALIVE;
                    grid.setState(row, col, dragTargetState);
                }
            }

            // click and drag detection so we paint all cells passed over
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!editingEnabled || currentTool != Tool.PAINT) return;
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (!grid.isInBounds(row, col)) return;
                grid.setState(row, col, dragTargetState);
            }

            // update ghost preview as mouse moves
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                if (currentTool == Tool.LINE && lineStart != null) {
                    if (!grid.isInBounds(row, col)) return;
                    linePreview = getLineCells(lineStart[0], lineStart[1], row, col);
                    repaint();
                } else if (currentTool == Tool.SHAPE && activeShape != null) {
                    shapePreview = new ArrayList<>();
                    for (int[] offset : activeShape.getRelativeCells()) {
                        shapePreview.add(new int[]{row + offset[0], col + offset[1]});
                    }
                    repaint();
                }
            }
        };

        // register listeners
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    public Tool getCurrentTool() { return currentTool; }

    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
        lineStart = null;
        linePreview = List.of();
        shapePreview = List.of();
        repaint();
    }

    public void setActiveShape(Shape shape) {
        this.activeShape = shape;
        this.currentTool = shape != null ? Tool.SHAPE : Tool.PAINT;
        shapePreview = List.of();
        repaint();
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
        for (int[] cell : linePreview) {
            graphics2d.fillRect(cell[1] * cellSize, cell[0] * cellSize, cellSize, cellSize);
        }

        // draw ghost preview for shape tool
        graphics2d.setColor(new Color(20, 100, 100, 50));
        for (int[] cell : shapePreview) {
            if (grid.isInBounds(cell[0], cell[1])) {
                graphics2d.fillRect(cell[1] * cellSize, cell[0] * cellSize, cellSize, cellSize);
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

    /* Bresenham's line algorithm, courtesy of Claude.
        This is a preexisting line drawing algorithm designed for
        pixel grids specifically. It is great because it uses integer
        arithmetic only, so it avoids floating point operations. */
    private List<int[]> getLineCells(int r1, int c1, int r2, int c2) {
        List<int[]> cells = new ArrayList<>();
        int dr = Math.abs(r2 - r1), dc = Math.abs(c2 - c1);
        int sr = r1 < r2 ? 1 : -1, sc = c1 < c2 ? 1 : -1;
        int err = dr - dc;
        while (true) {
            cells.add(new int[]{r1, c1});
            if (r1 == r2 && c1 == c2) break;
            int e2 = 2 * err;
            if (e2 > -dc) { err -= dc; r1 += sr; }
            if (e2 < dr)  { err += dr; c1 += sc; }
        }
        return cells;
    }
}
