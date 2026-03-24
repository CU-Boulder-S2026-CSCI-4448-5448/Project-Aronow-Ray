package automata.display;

import automata.CellFactory;
import automata.Grid;
import automata.State;
import automata.rules.ConwaysRule;
import automata.rules.Rule;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public final class GameOfLifeApp {
    private static final int DEFAULT_STEP_INTERVAL_MILLIS = 1_000;

    private final String title;
    private final Grid grid;
    private final int cellSize;
    private final int stepIntervalMillis;

    private GameOfLifeApp(Builder builder) {
        this.title = builder.title;
        this.grid = new Grid(builder.cellFactory, builder.rule, builder.rows, builder.columns);
        this.cellSize = builder.cellSize;
        this.stepIntervalMillis = builder.stepIntervalMillis;
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GridPanel(grid, cellSize));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            Timer simulationTimer = new Timer(stepIntervalMillis, event -> grid.updateAllCells());
            simulationTimer.start();
        });
    }

    public Grid getGrid() {
        return grid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static void main(String[] args) {
        GameOfLifeApp app = GameOfLifeApp.builder().build();
        app.getGrid().setState(49, 50, State.ALIVE);
        app.getGrid().setState(50, 51, State.ALIVE);
        app.getGrid().setState(51, 49, State.ALIVE);
        app.getGrid().setState(51, 50, State.ALIVE);
        app.getGrid().setState(51, 51, State.ALIVE);
        app.show();
    }

    public static final class Builder {
        private String title = "Conway's Game of Life";
        private Rule rule = new ConwaysRule();
        private CellFactory cellFactory = new CellFactory();
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
            return new GameOfLifeApp(this);
        }
    }
}

