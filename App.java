
/**************************************************
*   Author: David B
**************************************************/
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class App extends Application {

    private static MovementOption[] cards = new MovementOption[16];
    static {
        cards[0] = new MovementOption("Tiger", "c5 c2");
        cards[1] = new MovementOption("Dragon", "a4 e4 b2 d2");
        cards[2] = new MovementOption("Crab", "c4 a3 e3");
        cards[3] = new MovementOption("Elephant", "b4 d4 b3 d3");
        cards[4] = new MovementOption("Monkey", "b4 d4 b2 d2");
        cards[5] = new MovementOption("Mantis", "b4 d4 c2");
        cards[6] = new MovementOption("Crane", "c4 b2 d2");
        cards[7] = new MovementOption("Boar", "c4 b3 d3");
        cards[8] = new MovementOption("Frog", "b4 a3 d2");
        cards[9] = new MovementOption("Rabbit", "d4 e3 b2");
        cards[10] = new MovementOption("Goose", "b4 b3 d3 d2");
        cards[11] = new MovementOption("Rooster", "d4 b3 d3 b2");
        cards[12] = new MovementOption("Horse", "c4 b3 c2");
        cards[13] = new MovementOption("Ox", "c4 d3 c2");
        cards[14] = new MovementOption("Eel", "b4 d3 b2");
        cards[15] = new MovementOption("Cobra", "d4 b3 d2");

    }

    private Random generator;

    private Team left, right;
    private boolean currTeam;
    private PlayingCards transitionArea;
    private GridPane playArea;
    private Tile board[][];

    public App() {
        generator = new Random();

        ArrayList<MovementOption> playingCards = generateCards();
        left = new Team(playingCards.get(0), playingCards.get(1), true, Color.BLUE, "Blue");
        right = new Team(playingCards.get(2), playingCards.get(3), false, Color.ORANGE, "Orange");
        transitionArea = new PlayingCards(playingCards.get(4), Pos.BASELINE_CENTER);

        currTeam = true;

        playArea = new GridPane();

        board = new Tile[5][5];
        for (int i = 0; i < board.length; i++) {
            if (i == 2) {
                board[0][i] = new Piece(PieceType.MASTER, 0, i, left);
                board[4][i] = new Piece(PieceType.MASTER, 4, i, right);
            } else {
                board[0][i] = new Piece(PieceType.PAWN, 0, i, left);
                board[4][i] = new Piece(PieceType.PAWN, 4, i, right);
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 1; j < board[0].length - 1; j++) {
                board[j][i] = new Tile("", j, i);
            }
        }

    }

    @Override
    public void init() {
    }

    @Override
    public void start(Stage primary) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: green;");

        makePlayArea();
        layout.setTop(transitionArea);
        BorderPane.setMargin(transitionArea, new Insets(20));
        HBox board = new HBox(left.getHand(), playArea, right.getHand());
        board.setAlignment(Pos.CENTER);
        HBox.setMargin(playArea, new Insets(20));
        // BorderPane.setAlignment(board, );

        layout.setCenter(board);

        Scene scene = new Scene(layout, 700, 600);
        primary.setScene(scene);
        primary.show();
    }

    private ArrayList<MovementOption> generateCards() {
        ArrayList<MovementOption> playCards = new ArrayList<>();
        while (playCards.size() < 5) {
            MovementOption possCard = cards[generator.nextInt(16)];
            if (!playCards.contains(possCard))
                playCards.add(possCard);
        }

        // playCards.forEach(e -> System.out.println(e));
        return playCards;
    }

    private void makePlayArea() {
        playArea.setHgap(5);
        playArea.setVgap(5);
        // playArea.setGridLinesVisible(true);
        playArea.setAlignment(Pos.CENTER);
    }

    public Tile getTileByPos(final int x, final int y) {
        return (Tile) playArea.getChildren().stream()
                .filter(node -> {
                    if (Tile.class.isAssignableFrom(node.getClass())) {
                        return ((Tile) node).x == x;
                    } else
                        return false;
                })
                .filter(node -> {
                    if (Tile.class.isAssignableFrom(node.getClass())) {
                        return ((Tile) node).y == y;
                    } else
                        return false;
                }).findFirst().get();
    }

    @Override
    public void stop() {
    }

    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[j][i] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

    class Tile extends ToggleButton {
        int x, y;
        private Optional<Piece> listener;
        private Optional<MovementOption> method;

        public Tile(String name, int x, int y) {
            super(name);

            this.x = x;
            this.y = y;
            clearListener();

            setPrefSize(80, 80);
            getStylesheets().add(getClass().getResource("tile.css").toExternalForm());

            playArea.add(this, x, y);

            setOnAction(e -> {
                if (listener.isPresent()) {
                    listener.get().moveTo(this, method.get());
                }
            });

        }

        protected void setPos(int x, int y) {
            playArea.getChildren().remove(this);
            // board[this.x][this.y] = null;
            this.x = x;
            this.y = y;
            board[x][y] = this;
            playArea.add(this, this.x, this.y);
        }

        public void setListener(Piece p, MovementOption method) {
            listener = Optional.of(p);
            this.method = Optional.of(method);
        }

        public void clearListener() {
            listener = Optional.empty();
            method = Optional.empty();
        }

        public boolean hasListener() {
            return listener.isPresent();
        }

        public Optional<Piece> getListener() {
            return listener;
        }

        @Override
        public String toString() {
            return "Tile (" + x + ", " + y
                    + (listener.isPresent() ? ", " + listener.get() + " from " + method.get() : "") + ")";
        }

    }

    class Piece extends Tile {
        private Team team;
        private PieceType type;

        public Piece(PieceType type, int x, int y, Team team) {
            super(type.name().substring(0, 1), x, y);
            this.type = type;
            this.team = team;
            team.getToggles().add(this);
            getStylesheets().add(getClass().getResource("piece.css").toExternalForm());
            setStyle("-selected:" + team.getDefaultColor());

        }

        // after moving, switches turn
        public void moveTo(Tile diff, MovementOption method) {
            this.setSelected(false);
            diff.setSelected(false);
            team.resetPossMoves(this);

            // if these pieces are on the same team, skip
            if (Piece.class.isAssignableFrom(diff.getClass()) && ((Piece) diff).team == this.team) {
                System.out.println("same team, skipping");
                diff.setSelected(true);
                return;
            }

            checkIfWinningMove(diff);

            // handle moving into different piece
            if (Piece.class.isAssignableFrom(diff.getClass())) {
                diff = ((Piece) diff).destroy();
            }

            // swap pieces
            int tempX = diff.x, tempY = diff.y;
            diff.setPos(x, y);
            this.setPos(tempX, tempY);
            int index = -1;

            // switching the MovementOption used
            if (method.equals(team.hand.get(0))) {
                index = 0;
            } else if (method.equals(team.hand.get(1))) {
                index = 1;
            } else {
                System.err.println("Method called by wrong team: " + method + " by " + this);
                System.err.println("Poss methods: \n\t" + team.hand.get(0) + "\n\t" + team.hand.get(1));
            }
            if (index != -1) {
                MovementOption temp = transitionArea.get(0);
                transitionArea.set(0, team.hand.get(index));
                team.hand.set(index, temp);
            }

            currTeam = !currTeam;

            // System.out.println("Swapped " + this + " " + diff);
            // printBoard();

        }

        private void checkIfWinningMove(Tile t) {
            if (Piece.class.isAssignableFrom(t.getClass()) && ((Piece) t).getType() == PieceType.MASTER) {
                System.out.println("Team " + team + " won by Master Capture!");
                endGame(team);
            } else if (t.y == 2 && t.x == (team.side ? 4 : 0)) {
                System.out.println("Team " + team + " won by Fort Capture!");
                endGame(team);
            }
        }

        private void endGame(Team winner) {
            Stage popup = new Stage();
            popup.setTitle("Game Over!");
            Label l = new Label("Team " + winner.getName() + " won!");
            Button closeButton = new Button("Close App");
            closeButton.setOnAction(e -> {
                Platform.exit();
            });
            GridPane gp = new GridPane();
            gp.add(l, 0, 0);
            gp.add(closeButton, 0, 1);
            gp.setAlignment(Pos.CENTER);

            popup.setScene(new Scene(gp, 300, 200));
            popup.show();
        }

        private Tile destroy() {
            team.getToggles().remove(this);
            Tile replacement = new Tile("", x, y);
            board[x][y] = replacement;
            return replacement;
        }

        public PieceType getType() {
            return type;
        }

        @Override
        public String toString() {
            return getText() + " " + super.toString();
        }
    }

    class Team extends ToggleGroup {
        private PlayingCards hand;
        private boolean side;
        private Color teamColor;
        private String name;

        public Team(MovementOption m1, MovementOption m2, boolean side, Color color, String name) {
            super();
            Pos pos = (side ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
            hand = new PlayingCards(m1, m2, pos);

            this.side = side;
            this.teamColor = color;
            this.name = name;
            selectedToggleProperty().addListener((c, o, n) -> {
                // won't show moves unless it's this turn's side
                if (currTeam != side)
                    return;

                // need to do otherwise moving pieces causes trouble.
                if (n != null && ((Piece) n).hasListener())
                    return;
                if (o != null && ((Piece) o).hasListener())
                    return;

                if (o != null) {
                    resetPossMoves((Piece) o);
                }
                if (n != null) {

                    stylePossMoves(getDesatColor(), (Piece) n);
                }
            });
        }

        private void resetPossMoves(Piece sel) {
            List<Integer[]> possOptions = hand.get(0).getPossOptions(sel.x, sel.y, side);
            possOptions.addAll(hand.get(1).getPossOptions(sel.x, sel.y, side));
            for (Integer[] pos : possOptions) {
                board[pos[0]][pos[1]].setStyle(buildStyle("white"));
                board[pos[0]][pos[1]].clearListener();
            }
        }

        private void stylePossMoves(String color, Piece sel) {
            hand.get(0).getPossOptions(sel.x, sel.y, side).forEach(pos -> {

                board[pos[0]][pos[1]].setStyle(buildStyle(color));
                board[pos[0]][pos[1]].setListener(sel, hand.get(0));
            });
            hand.get(1).getPossOptions(sel.x, sel.y, side).forEach(pos -> {
                board[pos[0]][pos[1]].setStyle(buildStyle(color));
                board[pos[0]][pos[1]].setListener(sel, hand.get(1));
            });

        }

        public String getDefaultColor() {
            return "#" + teamColor.toString().substring(2, 8);
        }

        public String getDesatColor() {
            return "#" + teamColor.desaturate().desaturate().toString().substring(2, 8);
        }

        public String buildStyle(String desat) {
            return "-normal:" + desat + "; -selected:" + getDefaultColor() + ";";
        }

        public PlayingCards getHand() {
            return hand;
        }

        public String getName() {
            return name;
        }
    }

}