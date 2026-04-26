package automata.controller;

import automata.GridSubject;
import automata.State;
import automata.StateSet;
import automata.Tool;
import automata.presets.Shape;

import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GridInteractionController extends MouseAdapter {
    private final GridSubject grid;
    private final int cellSize;
    private final StateSet stateSet;
    private final State defaultState;
    private Runnable repaintCallback = () -> {}; // no-op until set to avoid nullptr exception
    private boolean editingEnabled = true;
    private State dragTargetState;
    private State selectedDrawState;
    private int brushSize = 1;
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
        this.stateSet = grid.getStateSet();
        this.defaultState = stateSet.getDefaultState();
        this.dragTargetState = defaultState;
        this.selectedDrawState = stateSet == StateSet.CONWAYS_LIFE ? State.ALIVE : defaultState;
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
                linePreview = expandBrushAlongCells(getLineCells(row, column, row, column));
                repaintCallback.run();
            } else {
                // second click: apply the preview to the grid
                for (int[] cell : linePreview) {
                    grid.setState(cell[0], cell[1], selectedDrawState);
                }
                lineStart = null;
                linePreview = List.of();
            }
        } else if (currentTool == Tool.SHAPE && activeShape != null) {
            // stamp all preview cells onto the grid
            for (int[] cell : shapePreview) {
                if (grid.isInBounds(cell[0], cell[1])) {
                    grid.setState(cell[0], cell[1], selectedDrawState);
                }
            }
        } else {
            if (SwingUtilities.isRightMouseButton(event)) {
                dragTargetState = defaultState;
            } else if (stateSet == StateSet.CONWAYS_LIFE) {
                State current = grid.getState(row, column);
                dragTargetState = current == State.ALIVE ? State.DEAD : State.ALIVE;
            } else {
                dragTargetState = selectedDrawState;
            }
            applyBrushAt(row, column, dragTargetState);
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

        applyBrushAt(row, column, dragTargetState);
    }

    // update ghost preview as mouse moves
    @Override
    public void mouseMoved(MouseEvent event) {
        int row = event.getY() / cellSize;
        int column = event.getX() / cellSize;

        if (currentTool == Tool.LINE && lineStart != null) {
            if (!grid.isInBounds(row, column)) return;
            linePreview = expandBrushAlongCells(getLineCells(lineStart[0], lineStart[1], row, column));
            repaintCallback.run();
        } else if (currentTool == Tool.SHAPE && activeShape != null) {
            shapePreview = new ArrayList<>();
            for (int[] offset : activeShape.getRelativeCells()) {
                shapePreview.addAll(getBrushCells(row + offset[0], column + offset[1]));
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

    public void setSelectedDrawState(State state) {
        State nonNullState = Objects.requireNonNull(state, "state");
        if (!stateSet.supports(nonNullState)) {
            throw new IllegalArgumentException("State " + state + " is not supported by this grid.");
        }
        this.selectedDrawState = nonNullState;
    }

    public State getSelectedDrawState() {
        return selectedDrawState;
    }

    public void setBrushSize(int brushSize) {
        if (brushSize <= 0) {
            throw new IllegalArgumentException("Brush size must be positive.");
        }
        this.brushSize = brushSize;
        linePreview = List.of();
        shapePreview = List.of();
        repaintCallback.run();
    }

    public int getBrushSize() {
        return brushSize;
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

    private void applyBrushAt(int row, int column, State state) {
        for (int[] cell : getBrushCells(row, column)) {
            grid.setState(cell[0], cell[1], state);
        }
    }

    private List<int[]> expandBrushAlongCells(List<int[]> centers) {
        Set<String> seen = new LinkedHashSet<>();
        List<int[]> expanded = new ArrayList<>();
        for (int[] center : centers) {
            for (int[] cell : getBrushCells(center[0], center[1])) {
                String key = cell[0] + "," + cell[1];
                if (seen.add(key)) {
                    expanded.add(cell);
                }
            }
        }
        return expanded;
    }

    private List<int[]> getBrushCells(int centerRow, int centerColumn) {
        List<int[]> cells = new ArrayList<>();
        int rowStart = centerRow - brushSize / 2;
        int rowEnd = centerRow + (brushSize - 1) / 2;
        int columnStart = centerColumn - brushSize / 2;
        int columnEnd = centerColumn + (brushSize - 1) / 2;
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                if (grid.isInBounds(row, column)) {
                    cells.add(new int[]{row, column});
                }
            }
        }
        return cells;
    }
}
