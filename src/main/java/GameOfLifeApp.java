import javax.swing.*;

// Creates the window (JFrame) and starts the application.
void main() {
    JFrame frame = new JFrame("Conway's Game of Life");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Grid grid = new Grid();
    frame.add(new GridPanel(grid));
    frame.pack();
    frame.setVisible(true);
}

