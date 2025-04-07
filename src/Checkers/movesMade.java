package Checkers;

class movesMade { // movesMade class begins

    int fromRow, fromCol; // declares from row and column as public ints
    int toRow, toCol; // declares to row and column as public ints

    movesMade(int r1, int c1, int r2, int c2) { // movesMade constructor takes in selected squares and assigns them to public ints
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    boolean isJump() { // checks if move is a jump
        return (fromRow - toRow == 2 || fromRow - toRow == -2);
    }

}