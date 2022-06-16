import java.util.ArrayList;

import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

class PlayingCards extends VBox {
    private ArrayList<MovementOption> cards = new ArrayList<>();

    public PlayingCards(MovementOption m1, MovementOption m2, Pos pos) {
        cards.add(m1);
        cards.add(m2);

        setAlignment(Pos.CENTER);
        getChildren().addAll(m1, m2);
    }

    public PlayingCards(MovementOption m1, Pos pos) {
        cards.add(m1);

        setAlignment(pos);
        getChildren().addAll(m1);
    }

    public MovementOption get(int index) {
        return cards.get(index);
    }

    public void set(int index, MovementOption m1) {
        getChildren().remove(cards.get(index));
        getChildren().add(m1);
        cards.set(index, m1);
    }
}