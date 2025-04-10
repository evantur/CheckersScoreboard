package Checkers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Board extends JPanel implements ActionListener, MouseListener {

    Data board; // declares new Data class to store the game's information
    boolean gameInProgress; // boolean to check if game is in progress
    int currentPlayer; // tracks whose turn it is
    int selectedRow, selectedCol; // tracks which squares have been selected
    movesMade[] legalMoves; // declares new movesMade array
    JLabel title; // title JLabel on frame
    JButton newGame; // newGame JButton on frame - starts a new game
    JButton howToPlay; // howToPlay JButton on frame - gives intro to Checkers and how to play
    JLabel message; // message JLabel on frame - indicates whose turn it is
    String Player1; // first player's name
    String Player2; // second player's name
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    BufferedImage player1_piece, player2_piece, player1_king, player2_king;

    public Board() throws Exception { // default constructor

        addMouseListener(this); // implements Mouse Listener

        // setup images
        player1_piece = ImageIO.read(new File("src/Images/Player1_piece.png"));
        player2_piece = ImageIO.read(new File("src/Images/Player2_piece.png"));
        player1_king = ImageIO.read(new File("src/Images/Player1_king.png"));
        player2_king = ImageIO.read(new File("src/Images/Player2_king.png"));

        // assigns all JLabels and JButtons to their values, as well as styles them
        title = new JLabel("Checkers!");
        title.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.darkGray);
        howToPlay = new JButton("Rules");
        howToPlay.addActionListener(this);
        newGame = new JButton("New Game");
        newGame.addActionListener(this);
        message = new JLabel("",JLabel.CENTER);
        message.setFont(new  Font("Serif", Font.BOLD, 14));
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setForeground(Color.darkGray);

        // set up connection to Scoreboard
        System.out.println("Attempting connection to scoreboard...");
        socket = new Socket("localhost", 8901);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        board = new Data(); // assigns to new Data class
        getPlayersNames(); // calls to get players' names
        NewGame(); // calls to start a new game
    }

    public BufferedImage resizeImage(BufferedImage inputImage, int width, int height) throws IOException {
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2d = resultImage.createGraphics();
        graphics2d.drawImage(inputImage, 0, 0, width, height, null);
        graphics2d.dispose();
        return resultImage;
    }

    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == newGame) // if newGame button is pressed, a new game is created
            NewGame();
        else if (src == howToPlay) // if howToPlay button is pressed, instructions pop up
            instructions();
    }

    void NewGame() {
        board.setUpBoard();
        currentPlayer = Data.player1; // indicates its player 1's move
        legalMoves = board.getLegalMoves(Data.player1); // searches for legal moves
        selectedRow = -1; // no square is selected
        message.setText("It's " + Player1 + "'s turn."); // indicates whose turn it is
        gameInProgress = true; // sets gameInProgress as true
        newGame.setEnabled(true); // enables newGame button
        howToPlay.setEnabled(true); // enables howToPlayButton
        repaint(); // repaints board
        System.out.println("Sending to board: " + board.score());
        out.println(board.score());
        out.println("Pieces Remaining:");
    }

    public void getPlayersNames(){ // gets players names through JTextField

        JTextField player1Name = new JTextField("Player 1");
        JTextField player2Name = new JTextField("Player 2");

        // creates new JPanel to store the JTextFields
        JPanel getNames = new JPanel();
        getNames.setLayout(new BoxLayout(getNames, BoxLayout.PAGE_AXIS));
        getNames.add(player1Name);
        getNames.add(player2Name);

        // player inputs name through Confirm Dialog
        int result = JOptionPane.showConfirmDialog(null, getNames, "Enter Your Names!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        
        if (result == JOptionPane.OK_OPTION) { // if players give names, names are assigned
            Player1 = player1Name.getText();
            Player2 = player2Name.getText();
        } else { // otherwise default names are given
            Player1 = "Player 1";
            Player2 = "Player 2";
        }

    }

    void instructions() { // when howToPlay button is pressed, instruction Message Dialog appears

        // brief history of Checkers and a link to read the instructions - the link is not clickable
        String intro = "Checkers, called Draughts in most countries,\n" +
                "has been traced back to the 1300s, though it\n" +
                "may indeed stretch further into history than that.\n" +
                "These are the standard U.S. rules for Checkers.\n\n"+
                "Read how to play: http:// abt.cm/1d0fHKE";

        JOptionPane.showMessageDialog(null, intro, "What is Checkers", JOptionPane.PLAIN_MESSAGE); // shows message

    }

    void gameOver(String str) { // when game is over

        message.setText(str); // indicates who won
        newGame.setEnabled(true); // enables newGame button
        howToPlay.setEnabled(true); // enables howToPlayButton
        gameInProgress = false; // sets gameInProgress as false, until new game is initialized

    }

    public void mousePressed(MouseEvent evt) { // when the board is clicked

        if (!gameInProgress){ // if game is not in progress
            message.setText("Start a new game."); // indicates to start a new game
        }else { // otherwise, calculates which square was pressed
            int col = (evt.getX() - 2) / 40; // calculation of square's column
            int row = (evt.getY() - 2) / 40; // calculation of square's row
            if (col >= 0 && col < 8 && row >= 0 && row < 8) // if square is on the board
                ClickedSquare(row,col); // calls ClickedSquare
        }
    }

    void ClickedSquare(int row, int col) { // processes legal moves

        for (int i = 0; i < legalMoves.length; i++){ // runs through all legal moves
            if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) { // if selected piece can be moved
                selectedRow = row; // assigns selected row
                selectedCol = col; // assigns selected column
                if (currentPlayer == Data.player1) // indicates whose turn it is
                    message.setText("It's " + Player1 + "'s turn.");
                else
                    message.setText("It's " + Player2 + "'s turn.");
                repaint(); // repaints board
                return;
            }
        }

        if (selectedRow < 0) { // if no square is selected
            message.setText("Select a piece to move."); // indicates player to pick a piece to move
            return;
        }

        for (int i = 0; i < legalMoves.length; i++){ // runs through all legal moves
            if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol // if already selected piece can move
                && legalMoves[i].toRow == row && legalMoves[i].toCol == col) { // and the selected piece's destination is legal
                MakeMove(legalMoves[i]); // make the move
                return;
            }
        }

        // when a piece is selected and player clicks elsewhere besides legal destination, program encourages player to move piece
        message.setText("Where do you want to move it?");  

    }

    void MakeMove(movesMade move) { // moves the piece

        board.makeMove(move); // calls makeMove method in Data class
        out.println(board.score()); // sends score to scoreboard to update

        if (move.isJump()) { // checks if player must continue jumping
            legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);
            if (legalMoves != null) { // if player must jump again
                if (currentPlayer == Data.player1)
                    message.setText(Player1 + ", you must jump.");
                else
                    message.setText(Player2 + ", you must jump.");
                selectedRow = move.toRow; 
                selectedCol = move.toCol;
                repaint(); // repaints board

                return;
            }
        }

        if (currentPlayer == Data.player1) { // if it was player 1's turn
            currentPlayer = Data.player2; // it's now player 2's
            legalMoves = board.getLegalMoves(currentPlayer); // gets legal moves for player 2
            if (legalMoves == null) {// if there aren't any moves, player 1 wins
                gameOver(Player1 + " wins!");
                out.println("Game Over! " + Player1 + " wins");
            }
            else if (legalMoves[0].isJump()) // if player 2 must jump, it indicates so
                message.setText(Player2 + ", you must jump.");
            else // otherwise, it indicates it's player 2's turn
                message.setText("It's " + Player2 + "'s turn.");
        } else { // otherwise, if it was player 2's turn
            currentPlayer = Data.player1; // it's now player 1's turn
            legalMoves = board.getLegalMoves(currentPlayer);
            if (legalMoves == null) {// if there aren't any moves, player 2 wins
                gameOver(Player2 + " wins!");
                out.println("Game Over! " + Player2 + " wins");
            }
            else if (legalMoves[0].isJump())
                message.setText(Player1 + ", you must jump.");
            else // otherwise, it indicates it's player 1's turn
                message.setText("It's " + Player1 + "'s turn.");
        }

        selectedRow = -1; // no squares are not selected

        if (legalMoves != null) {
            boolean sameFromSquare = true;
            for (int i = 1; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow != legalMoves[0].fromRow
                        || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                    sameFromSquare = false;
                    break;
                }
            if (sameFromSquare) { // if true, the player's final piece is already selected
                selectedRow = legalMoves[0].fromRow;
                selectedCol = legalMoves[0].fromCol;
            }
        }

        repaint();

    }


    public void paintComponent(Graphics g) { // paints board

        // boarder around game board
        g.setColor(new Color(139,119,101));
        g.fillRect(0, 0, 324, 324);

        // creates checkered effect
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                // paints squares
                if ( row % 2 == col % 2 )
                    g.setColor(new Color(77,77,77));
                else
                    g.setColor(new Color(230,230,230));
                g.fillRect(2 + col*40, 2 + row*40, 40, 40);

                // paints squares with pieces on them
                switch (board.pieceAt(row,col)) {
                    case Data.player1:
                        g.drawImage(player1_piece, 4 + col*40, 7 + row*40, null);
                        break;
                    case Data.player2:
                        g.drawImage(player2_piece, 4 + col*40, 7 + row*40, null);
                        break;
                    case Data.playerKing1:
                        g.drawImage(player1_king, 4 + col*40, 7 + row*40, null);
                        break;
                    case Data.playerKing2:
                        g.drawImage(player2_king, 4 + col*40, 7 + row*40, null);
                        break;
                }
            }
        }

        if (gameInProgress) { // if game is in progress

            g.setColor(new Color(0, 255,0));
            for (int i = 0; i < legalMoves.length; i++) { // runs through all legal move
                // highlights, in green, all the possible squares the player can move
                g.drawRect(2 + legalMoves[i].fromCol*40, 2 + legalMoves[i].fromRow*40, 39, 39);
            }

            if (selectedRow >= 0){ // if a square is selected
                g.setColor(Color.white); // the square is highlighted in white
                g.drawRect(2 + selectedCol*40, 2 + selectedRow*40, 39, 39);
                g.drawRect(3 + selectedCol*40, 3 + selectedRow*40, 37, 37);
                g.setColor(Color.green);
                for (int i = 0; i < legalMoves.length; i++) { // its legal moves are then highlighted in green
                    if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow)
                        g.drawRect(2 + legalMoves[i].toCol*40, 2 + legalMoves[i].toRow*40, 39, 39);
                }
            }
        }
    }

    // implements Mouse entered, clicked, released and exited
    public void mouseEntered(MouseEvent evt) { }
    public void mouseClicked(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }

}




    