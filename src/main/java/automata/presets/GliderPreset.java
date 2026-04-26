package automata.presets;

import java.util.List;

public class GliderPreset implements GridPreset {

    @Override
    public String getName() {
        return "Glider";
    }

    @Override
    public List<int[]> getRelativeCells() {
        return List.of(
            new int[]{ 0,  1},
            new int[]{ 1,  2},
            new int[]{ 2,  0},
            new int[]{ 2,  1},
            new int[]{ 2,  2}
        );
    }
}
