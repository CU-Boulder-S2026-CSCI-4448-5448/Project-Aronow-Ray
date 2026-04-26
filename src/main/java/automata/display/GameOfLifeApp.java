package automata.display;

import automata.CellFactory;
import automata.Grid;
import automata.GridSnapshot;
import automata.presets.GridPreset;
import automata.rules.ConwaysRule;
import automata.rules.Rule;

import javax.swing.*;
import java.awt.*;

public final class GameOfLifeApp {
    private static final int DEFAULT_STEP_INTERVAL_MILLIS = 200; // Changed from 1_000

    private final String title;
    private final Grid grid;
    private GridSnapshot savedSnapshot;
    private final int cellSize;
    private final int stepIntervalMillis;

    private GameOfLifeApp(Builder builder) {
        this.title = builder.title;
        this.grid = builder.grid;
        this.cellSize = builder.cellSize;
        this.stepIntervalMillis = builder.stepIntervalMillis;
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            Timer simulationTimer = new Timer(stepIntervalMillis, event -> grid.updateAllCells());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GridPanel gridPanel = new GridPanel(grid, cellSize);

            JPanel container = new JPanel(new BorderLayout());
            container.add(gridPanel, BorderLayout.CENTER); // grid takes all available space

            // Add these buttons to a panel at the bottom of the grid
            JButton startStopButton = createStartStopButton(gridPanel, simulationTimer);
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(startStopButton);
            // pass the start/stop button so that it can be updated by the other buttons
            buttonPanel.add(createResetButton(gridPanel, simulationTimer, startStopButton));
            buttonPanel.add(createClearButton(gridPanel, simulationTimer, startStopButton));
            container.add(buttonPanel, BorderLayout.SOUTH);

            frame.add(container);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // function for the button to start/stop simulation
    private JButton createStartStopButton(GridPanel gridPanel, Timer simulationTimer) {

        JButton button = new JButton("Start");
        button.addActionListener(_ -> {
            if (simulationTimer.isRunning()) {
                simulationTimer.stop();
                gridPanel.setEditingEnabled(true);
                button.setText("Start");
            } else {
                this.savedSnapshot = new GridSnapshot(grid); // create a snapshot on run
                simulationTimer.start();
                gridPanel.setEditingEnabled(false); // can only add cells when paused
                button.setText("Stop");
            }
        });
        return button;
    }

    private JButton createResetButton(GridPanel gridPanel, Timer simulationTimer, JButton startStopButton) {

        JButton button = new JButton("Reset");
        button.addActionListener(_ -> {
            if (savedSnapshot == null) return;
            simulationTimer.stop();
            savedSnapshot.restore(grid);
            gridPanel.setEditingEnabled(true);
            startStopButton.setText("Start");
        });
        return button;
    }

    private JButton createClearButton(GridPanel gridPanel, Timer simulationTimer, JButton startStopButton) {

        JButton button = new JButton("Clear");
        button.addActionListener(_ -> {
            simulationTimer.stop();
            grid.clear();
            gridPanel.setEditingEnabled(true);
            startStopButton.setText("Start");
        });
        return button;
    }

    public Grid getGrid() {
        return grid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static void main(String[] args) {
        GameOfLifeApp app = GameOfLifeApp.builder().build();
//        app.getGrid().setState(49, 50, State.ALIVE);
//        app.getGrid().setState(50, 51, State.ALIVE);
//        app.getGrid().setState(51, 49, State.ALIVE);
//        app.getGrid().setState(51, 50, State.ALIVE);
//        app.getGrid().setState(51, 51, State.ALIVE);
        app.show();
    }

    public static final class Builder {
        private String title = "Conway's Game of Life";
        private Rule rule = new ConwaysRule();
        private CellFactory cellFactory = new CellFactory();
        private GridPreset gridPreset = null;
        private Grid grid;
        private int rows = Grid.DEFAULT_MAX_ROWS;
        private int columns = Grid.DEFAULT_MAX_COLUMNS;
        private int cellSize = 8;
        private int stepIntervalMillis = DEFAULT_STEP_INTERVAL_MILLIS;

        private Builder() {
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withRule(Rule rule) {
            this.rule = rule;
            return this;
        }

        public Builder withCellFactory(CellFactory cellFactory) {
            this.cellFactory = cellFactory;
            return this;
        }

        public Builder withRows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder withColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public Builder withCellSize(int cellSize) {
            this.cellSize = cellSize;
            return this;
        }

        public Builder withStepIntervalMillis(int stepIntervalMillis) {
            this.stepIntervalMillis = stepIntervalMillis;
            return this;
        }

        public Builder withInitialState(GridPreset preset) {
            this.gridPreset = preset;
            return this;
        }

        public GameOfLifeApp build() {
            if (rows < 0 || columns < 0) {
                throw new IllegalArgumentException("Grid dimensions must be non-negative.");
            }
            if (cellSize <= 0) {
                throw new IllegalArgumentException("Cell size must be positive.");
            }
            if (stepIntervalMillis <= 0) {
                throw new IllegalArgumentException("Step interval must be positive.");
            }
            this.grid = new Grid(cellFactory, rule, rows, columns);
            if (gridPreset != null) {
                gridPreset.apply(grid); // stamps the initial pattern onto the grid
            }
            return new GameOfLifeApp(this);
        }
    }
}

