package automata.display;

import automata.Grid;
import automata.Tool;
import automata.controller.GridInteractionController;
import automata.controller.SimulationController;
import automata.presets.*;
import automata.presets.Shape;
import automata.rules.ConwaysRule;
import automata.rules.Rule;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class GameOfLifeApp {
    private static final int DEFAULT_STEP_INTERVAL_MILLIS = 200; // Changed from 1_000

    private final String title;
    private final Grid grid;
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
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // create controllers
            GridInteractionController interactionController = new GridInteractionController(grid, cellSize);
            SimulationController simulationController = new SimulationController(grid, interactionController, stepIntervalMillis);

            GridPanel gridPanel = new GridPanel(grid, cellSize, interactionController);

            // Connect repaint to the controller without giving it a direct reference to GridPanel (MVC)
            interactionController.setRepaintCallback(gridPanel::repaint);

            JPanel container = new JPanel(new BorderLayout());
            container.add(gridPanel, BorderLayout.CENTER); // grid takes all available space

            // Tool buttons pinned to the top left
            JButton lineToolButton = createLineToolButton(interactionController);
            JComboBox<Shape> shapeDropdown = createShapeDropdown(interactionController);
            JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            toolPanel.add(lineToolButton);
            toolPanel.add(shapeDropdown);
            container.add(toolPanel, BorderLayout.NORTH);

            // Simulation buttons along the bottom
            JButton startStopButton = createStartStopButton(simulationController, interactionController, lineToolButton, shapeDropdown);
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(startStopButton);
            buttonPanel.add(createResetButton(simulationController, startStopButton, lineToolButton, shapeDropdown));
            buttonPanel.add(createClearButton(simulationController, startStopButton, lineToolButton, shapeDropdown));
            container.add(buttonPanel, BorderLayout.SOUTH);

            // Set custom icon :)
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/icon.png")));

            frame.setIconImage(icon.getImage());
            frame.add(container);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private JButton createStartStopButton(SimulationController simulationController, GridInteractionController interactionController, JButton lineToolButton, JComboBox<Shape> shapeDropdown) {
        JButton button = new JButton("Start");
        button.addActionListener(_ -> {
            boolean isNowRunning = simulationController.toggleSimulation();
            if (isNowRunning) {
                interactionController.setCurrentTool(Tool.PAINT); // reset tools on sim start
                lineToolButton.setEnabled(false);
                lineToolButton.setText("Line Tool");
                shapeDropdown.setEnabled(false);
                shapeDropdown.setSelectedIndex(0); // reset to "— None —"
                button.setText("Stop");
            } else {
                lineToolButton.setEnabled(true);
                shapeDropdown.setEnabled(true);
                button.setText("Start");
            }
        });
        return button;
    }

    private JButton createResetButton(SimulationController simulationController, JButton startStopButton, JButton lineToolButton, JComboBox<Shape> shapeDropdown) {
        JButton button = new JButton("Reset");
        button.addActionListener(_ -> {
            simulationController.resetSimulation();
            lineToolButton.setEnabled(true);
            shapeDropdown.setEnabled(true);
            startStopButton.setText("Start");
        });
        return button;
    }

    private JButton createClearButton(SimulationController simulationController, JButton startStopButton, JButton lineToolButton, JComboBox<Shape> shapeDropdown) {
        JButton button = new JButton("Clear");
        button.addActionListener(_ -> {
            simulationController.clearGrid();
            lineToolButton.setEnabled(true);
            shapeDropdown.setEnabled(true);
            startStopButton.setText("Start");
        });
        return button;
    }

    private JButton createLineToolButton(GridInteractionController interactionController) {
        JButton button = new JButton("Line Tool");
        button.addActionListener(_ -> {
            if (interactionController.getCurrentTool() == Tool.LINE) {
                interactionController.setCurrentTool(Tool.PAINT);
                button.setText("Line Tool");
            } else {
                interactionController.setCurrentTool(Tool.LINE);
                button.setText("Line Tool: ON");
            }
        });
        return button;
    }

    private JComboBox<Shape> createShapeDropdown(GridInteractionController interactionController) {
        JComboBox<Shape> dropdown = new JComboBox<>();
        dropdown.addItem(null);             // "— None —" option
        dropdown.addItem(new GliderPreset());
        dropdown.addItem(new BlinkerPreset());
        dropdown.addItem(new CloverleafPreset());
        dropdown.addItem(new HammerheadPreset());

        // display shape names, and "— None —" for the null option
        dropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? "— None —" : ((Shape) value).getName());
                return this;
            }
        });

        dropdown.addActionListener(_ -> {
            Shape selected = (Shape) dropdown.getSelectedItem();
            interactionController.setActiveShape(selected); // null clears back to PAINT
        });

        return dropdown;
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
            this.grid = new Grid(rule, rows, columns);
            if (gridPreset != null) {
                gridPreset.apply(grid); // stamps the initial pattern onto the grid
            }
            return new GameOfLifeApp(this);
        }
    }
}
