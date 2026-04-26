package automata.presets;

import java.util.List;

public interface Shape {
    String getName();
    List<int[]> getRelativeCells(); // row/col offsets from click point
}
