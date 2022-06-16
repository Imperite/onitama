import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.control.Label;
import javafx.geometry.Pos;

class MovementOption extends Label {

    private final String name;
    private final List<Integer[]> relativePositions;

    public MovementOption(String name, String moves) {
        super(name);
        this.name = name;
        setStyle("-fx-background-color:white");
        setPrefWidth(100);
        setAlignment(Pos.CENTER);

        relativePositions = new ArrayList<>();
        Arrays.asList(moves.split(" ")).forEach(s -> {
            Integer coords[] = { s.charAt(1) - '3', s.charAt(0) - 'c' };
            relativePositions.add(coords);
        });
    }

    // true for moving from left, false for moving from right
    public List<Integer[]> getPossOptions(int x, int y, boolean dir) {
        ArrayList<Integer[]> results = new ArrayList<>();
        for (Integer shift[] : relativePositions) {
            int sign = dir ? 1 : -1;
            Integer newPos[] = { x + sign * shift[0], y + shift[1] };
            boolean inBoundsX = newPos[0] >= 0 && newPos[0] < 5;
            boolean inBoundsY = newPos[1] >= 0 && newPos[1] < 5;
            if (inBoundsX && inBoundsY) {
                results.add(newPos);
            }
        }
        return results;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(name);
        for (Integer[] shift : relativePositions) {
            sb.append(" [" + shift[0] + " " + shift[1] + "]");
        }
        return sb.toString();
    }
}