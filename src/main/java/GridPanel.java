import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {
    private static final int CELL_SIZE = 8;
    private int cols = 0;
    private int rows = 0;

    public GridPanel(Grid grid) {
        int cols = grid.getMaxColumns() + 1;
        int rows = grid.getMaxRows() + 1;
        setPreferredSize(new Dimension(cols * CELL_SIZE, rows * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        // TODO: Drawing example, replace this with Grid data
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                graphics2d.setColor(Color.BLACK);
                graphics2d.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                graphics2d.setColor(Color.DARK_GRAY);
                graphics2d.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }
}
