package automata.display;

import automata.Grid;
import automata.State;
import automata.StateSet;
import automata.Tool;
import automata.controller.GridInteractionController;
import automata.controller.SimulationController;
import automata.presets.*;
import automata.presets.Shape;
import automata.rules.ConwaysRule;
import automata.rules.RockPaperScissorsRule;
import automata.rules.Rule;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class GameOfLifeApp {
    private static final int DEFAULT_STEP_INTERVAL_MILLIS = 200; // Changed from 1_000
    private static final int DEFAULT_GRID_HEIGHT = 50;
    private static final int DEFAULT_GRID_WIDTH = 50;
    private static final int DEFAULT_CELL_SIZE = 10;

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
            JComboBox<State> brushDropdown = createBrushDropdown(interactionController);
            JComboBox<Integer> brushSizeDropdown = createBrushSizeDropdown(interactionController);
            JComboBox<Shape> shapeDropdown = createShapeDropdown(interactionController);
            JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            toolPanel.add(brushDropdown);
            toolPanel.add(brushSizeDropdown);
            toolPanel.add(lineToolButton);
            if (grid.getStateSet() == StateSet.CONWAYS_LIFE) {
                toolPanel.add(shapeDropdown);
            }
            container.add(toolPanel, BorderLayout.NORTH);

            // Simulation buttons along the bottom
            JButton startStopButton = createStartStopButton(simulationController, interactionController, lineToolButton, brushDropdown, brushSizeDropdown, shapeDropdown);
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(startStopButton);
            buttonPanel.add(createResetButton(simulationController, startStopButton, lineToolButton, brushDropdown, brushSizeDropdown, shapeDropdown));
            buttonPanel.add(createClearButton(simulationController, startStopButton, lineToolButton, brushDropdown, brushSizeDropdown, shapeDropdown));
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

    private JButton createStartStopButton(SimulationController simulationController, GridInteractionController interactionController, JButton lineToolButton, JComboBox<State> brushDropdown, JComboBox<Integer> brushSizeDropdown, JComboBox<Shape> shapeDropdown) {
        JButton button = new JButton("Start");
        button.addActionListener(event -> {
            boolean isNowRunning = simulationController.toggleSimulation();
            if (isNowRunning) {
                interactionController.setCurrentTool(Tool.PAINT); // reset tools on sim start
                lineToolButton.setEnabled(false);
                brushDropdown.setEnabled(false);
                brushSizeDropdown.setEnabled(false);
                lineToolButton.setText("Line Tool");
                shapeDropdown.setEnabled(false);
                shapeDropdown.setSelectedIndex(0); // reset to "— None —"
                button.setText("Stop");
            } else {
                lineToolButton.setEnabled(true);
                brushDropdown.setEnabled(true);
                brushSizeDropdown.setEnabled(true);
                shapeDropdown.setEnabled(true);
                button.setText("Start");
            }
        });
        return button;
    }

    private JButton createResetButton(SimulationController simulationController, JButton startStopButton, JButton lineToolButton, JComboBox<State> brushDropdown, JComboBox<Integer> brushSizeDropdown, JComboBox<Shape> shapeDropdown) {
        JButton button = new JButton("Reset");
        button.addActionListener(event -> {
            simulationController.resetSimulation();
            lineToolButton.setEnabled(true);
            brushDropdown.setEnabled(true);
            brushSizeDropdown.setEnabled(true);
            shapeDropdown.setEnabled(true);
            startStopButton.setText("Start");
        });
        return button;
    }

    private JButton createClearButton(SimulationController simulationController, JButton startStopButton, JButton lineToolButton, JComboBox<State> brushDropdown, JComboBox<Integer> brushSizeDropdown, JComboBox<Shape> shapeDropdown) {
        JButton button = new JButton("Clear");
        button.addActionListener(event -> {
            simulationController.clearGrid();
            lineToolButton.setEnabled(true);
            brushDropdown.setEnabled(true);
            brushSizeDropdown.setEnabled(true);
            shapeDropdown.setEnabled(true);
            startStopButton.setText("Start");
        });
        return button;
    }

    private JComboBox<State> createBrushDropdown(GridInteractionController interactionController) {
        JComboBox<State> dropdown = new JComboBox<>();
        if (grid.getStateSet() == StateSet.CONWAYS_LIFE) {
            dropdown.addItem(State.ALIVE);
        } else {
            dropdown.addItem(State.ROCK);
            dropdown.addItem(State.PAPER);
            dropdown.addItem(State.SCISSORS);
        }
        dropdown.setSelectedItem(interactionController.getSelectedDrawState());
        dropdown.addActionListener(event -> {
            State selected = (State) dropdown.getSelectedItem();
            if (selected != null) {
                interactionController.setSelectedDrawState(selected);
            }
        });
        return dropdown;
    }

    private JComboBox<Integer> createBrushSizeDropdown(GridInteractionController interactionController) {
        JComboBox<Integer> dropdown = new JComboBox<>();
        dropdown.addItem(1);
        dropdown.addItem(2);
        dropdown.addItem(3);
        dropdown.addItem(5);
        dropdown.setSelectedItem(interactionController.getBrushSize());
        dropdown.addActionListener(event -> {
            Integer selected = (Integer) dropdown.getSelectedItem();
            if (selected != null) {
                interactionController.setBrushSize(selected);
            }
        });
        return dropdown;
    }

    private JButton createLineToolButton(GridInteractionController interactionController) {
        JButton button = new JButton("Line Tool");
        button.addActionListener(event -> {
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
        if (grid.getStateSet() == StateSet.CONWAYS_LIFE) {
            dropdown.addItem(new GliderPreset());
            dropdown.addItem(new BlinkerPreset());
            dropdown.addItem(new CloverleafPreset());
            dropdown.addItem(new HammerheadPreset());
        }

        // display shape names, and "— None —" for the null option
        dropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? "— None —" : ((Shape) value).getName());
                return this;
            }
        });

        dropdown.addActionListener(event -> {
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

    static void main(String[] args) {
        GameOfLifeApp app = GameOfLifeApp
                .builder()
                .withRows(DEFAULT_GRID_HEIGHT)
                .withColumns(DEFAULT_GRID_WIDTH)
                .withConwaysRule()
                .withCellSize(DEFAULT_CELL_SIZE)
                .withStepIntervalMillis(DEFAULT_STEP_INTERVAL_MILLIS)
                //.withRockPaperScissorsRule()
                .build();
        app.show();
    }

    public static final class Builder {
        private String title = null;
        private Rule rule = null;
        private GridPreset gridPreset = null;
        private Grid grid;
        private int rows = -1;
        private int columns = -1;
        private int cellSize = -1;
        private int stepIntervalMillis = -1;

        private Builder() {
        }

        public Builder withConwaysRule() {
            this.rule = new ConwaysRule();
            this.title = "Conway's Game of Life";
            return this;
        }

        public Builder withRockPaperScissorsRule() {
            this.rule = new RockPaperScissorsRule();
            this.title = "Rock Paper Scissors";
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
            if (rows <= 0 || columns <= 0) {
                throw new IllegalArgumentException("Grid dimensions must be set and positive.");
            }
            if (cellSize <= 0) {
                throw new IllegalArgumentException("Cell size must be set and positive.");
            }
            if (stepIntervalMillis <= 0) {
                throw new IllegalArgumentException("Step interval must be set and positive.");
            }
            this.grid = new Grid(rule, rows, columns);
            if (gridPreset != null) {
                gridPreset.apply(grid); // stamps the initial pattern onto the grid
            }
            return new GameOfLifeApp(this);
        }
    }
}
