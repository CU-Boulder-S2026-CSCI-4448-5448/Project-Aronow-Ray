package automata.controller;

import automata.GridSubject;
import automata.State;
import automata.Tool;
import automata.presets.Shape;

import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GridInteractionController extends MouseAdapter {
    private final GridSubject grid;
    private final int cellSize;
    private Runnable repaintCallback = () -> {}; // no-op until set to avoid nullptr exception
    private boolean editingEnabled = true;
    private State dragTargetState = State.DEAD;
    private Tool currentTool = Tool.PAINT;
    private int[] lineStart = null;
    private List<int[]> linePreview = List.of();
    private Shape activeShape = null;
    private List<int[]> shapePreview = List.of();

    public GridInteractionController(GridSubject grid, int cellSize) {
        if (cellSize <= 0) {
            throw new IllegalArgumentException("Cell size must be positive.");
        }

        this.grid = Objects.requireNonNull(grid, "grid");
        this.cellSize = cellSize;
    }

    // MVC: the controller needs to trigger repaints but shouldn't hold a direct reference to the view.
    // Instead, the view passes in a callback (gridPanel::repaint) so the controller can notify it
    // without being coupled to it.
    public void setRepaintCallback(Runnable repaintCallback) {
        this.repaintCallback = Objects.requireNonNull(repaintCallback, "repaintCallback");
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (!editingEnabled) {
            return;
        }

        // convert pixel coordinates to grid coordinates
        int row = event.getY() / cellSize;
        int column = event.getX() / cellSize;
        // ignore clicks outside the grid
        if (!grid.isInBounds(row, column)) {
            return;
        }

        if (currentTool == Tool.LINE) {
            if (lineStart == null) {
                // first click: set the start point
                lineStart = new int[]{row, column};
                linePreview = getLineCells(row, column, row, column);
                repaintCallback.run();
            } else {
                // second click: apply the preview to the grid
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
            // paint tool. Left click toggles, right click erases
            if (SwingUtilities.isRightMouseButton(event)) {
                dragTargetState = State.DEAD;
            } else {
                // check what the clicked cell currently is
                State current = grid.getState(row, column);
                // toggle: if it was alive, drag will erase. If dead, drag will paint.
                dragTargetState = current == State.ALIVE ? State.DEAD : State.ALIVE;
            }
            grid.setState(row, column, dragTargetState);
        }
    }

    // click and drag detection so we paint all cells passed over
    @Override
    public void mouseDragged(MouseEvent event) {
        if (!editingEnabled || currentTool != Tool.PAINT) {
            return;
        }

        int row = event.getY() / cellSize;
        int column = event.getX() / cellSize;
        if (!grid.isInBounds(row, column)) {
            return;
        }

        grid.setState(row, column, dragTargetState);
    }

    // update ghost preview as mouse moves
    @Override
    public void mouseMoved(MouseEvent event) {
        int row = event.getY() / cellSize;
        int column = event.getX() / cellSize;

        if (currentTool == Tool.LINE && lineStart != null) {
            if (!grid.isInBounds(row, column)) return;
            linePreview = getLineCells(lineStart[0], lineStart[1], row, column);
            repaintCallback.run();
        } else if (currentTool == Tool.SHAPE && activeShape != null) {
            shapePreview = new ArrayList<>();
            for (int[] offset : activeShape.getRelativeCells()) {
                shapePreview.add(new int[]{row + offset[0], column + offset[1]});
            }
            repaintCallback.run();
        }
    }

    public void setEditingEnabled(boolean editingEnabled) {
        this.editingEnabled = editingEnabled;
    }

    public boolean isEditingEnabled() {
        return editingEnabled;
    }

    public Tool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
        lineStart = null;
        linePreview = List.of();
        shapePreview = List.of();
        repaintCallback.run();
    }

    public void setActiveShape(Shape shape) {
        this.activeShape = shape;
        this.currentTool = shape != null ? Tool.SHAPE : Tool.PAINT;
        shapePreview = List.of();
        repaintCallback.run();
    }

    public List<int[]> getLinePreview() {
        return linePreview;
    }

    public List<int[]> getShapePreview() {
        return shapePreview;
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
