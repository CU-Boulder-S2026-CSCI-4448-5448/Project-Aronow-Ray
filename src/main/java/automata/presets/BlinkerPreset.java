package automata.presets;

import java.util.List;

public class BlinkerPreset implements GridPreset {

    @Override
    public String getName() {
        return "Blinker";
    }

    @Override
    public List<int[]> getRelativeCells() {
        return List.of(
            new int[]{-1, 0},
            new int[]{ 0, 0},
            new int[]{ 1, 0}
        );
    }
}
