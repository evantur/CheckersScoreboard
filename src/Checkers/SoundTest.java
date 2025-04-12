package Checkers;

public class SoundTest {
    public static void main(String[] args) {
        try {
            Sound pieceMove = new Sound("src/Sounds/piece-move.wav");
            pieceMove.play();
            pieceMove.play();
            pieceMove.play();
            pieceMove.play();
            pieceMove.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
