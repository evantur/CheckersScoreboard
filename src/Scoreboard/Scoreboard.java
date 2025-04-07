package Scoreboard;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Scoreboard {
    private ServerSocket listener;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws Exception {
        System.out.println("Scoreboard is running");
        // set up networking
        listener = new ServerSocket(port);
        client = listener.accept();
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        // create frame
        JFrame frame = new JFrame("Live Scoreboard");
        frame.setIconImage(new ImageIcon("project2/Checkers_logo.png").getImage());

        // create and add new scoreboard
        ScorePanel panel = new ScorePanel();
        frame.add(panel);
        frame.add(panel.message0);
        frame.add(panel.mainScore);
        frame.add(panel.message1);
        frame.add(panel.kingScore);
        frame.add(panel.white);
        frame.add(panel.black);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 275);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

        // properly place components
        panel.message0.setBounds(0, 0, 600, 40);
        panel.mainScore.setBounds(200, 50, 200, 60);
        panel.message1.setBounds(0, 120, 600, 30);
        panel.kingScore.setBounds(0, 155, 600, 40);
        panel.white.setBounds(100, 55, 120, 60);
        panel.black.setBounds(395, 55, 120, 60);

        // start the loop
        String input;
        while ((input = in.readLine()) != null) {
            System.out.println(input);
            if (!input.contains(":")) { // end condition
                panel.setTitle(input);
                break;
            }
            else {
                // take input string and use it to update scoreboard
                panel.setScores(input);
            }
        }
    }

    public void stop() throws Exception {
        in.close();
        out.close();
        client.close();
        listener.close();
    }

    public static void main(String[] args) throws Exception {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.start(8901);
    }

}

class ScorePanel extends JPanel {
    JLabel message0, message1, white, black, mainScore, kingScore;

    public ScorePanel() {
        // display message0
        message0 = new JLabel("Pieces Remaining:");
        message0.setFont(new Font("Serif", Font.CENTER_BASELINE, 30));
        message0.setHorizontalAlignment(SwingConstants.CENTER);
        message0.setVerticalAlignment(SwingConstants.TOP);
        message0.setForeground(Color.darkGray);

        // display main score
        mainScore = new JLabel("0  :  0");
        mainScore.setFont(new Font("Serif", Font.CENTER_BASELINE, 50));
        mainScore.setHorizontalAlignment(SwingConstants.CENTER);
        mainScore.setVerticalAlignment(SwingConstants.TOP);
        mainScore.setForeground(Color.darkGray);

        // display 'white' and 'black' text beside the score
        // separate labels makes editing the score easier
        white = new JLabel("White");
        white.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
        white.setForeground(Color.LIGHT_GRAY);

        black = new JLabel("Black");
        black.setFont(new Font("Serif", Font.CENTER_BASELINE, 40));
        black.setForeground(Color.black);

        // display message1
        message1 = new JLabel("King Count:");
        message1.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));
        message1.setHorizontalAlignment(SwingConstants.CENTER);
        message1.setVerticalAlignment(SwingConstants.TOP);
        message1.setForeground(Color.darkGray);

        // display king count
        kingScore = new JLabel("0 : 0");
        kingScore.setFont(new Font("Serif", Font.CENTER_BASELINE, 35));
        kingScore.setHorizontalAlignment(SwingConstants.CENTER);
        kingScore.setVerticalAlignment(SwingConstants.TOP);
        kingScore.setForeground(Color.darkGray);
    }

    public void setScores(String input) {
        // retreive score values from input
        String[] values = input.split(":");

        // update scoreboard
        mainScore.setText(String.format("%s  :  %s", values[0], values[2]));
        kingScore.setText(String.format("%s : %s", values[1], values[3]));
    }

    public void setTitle(String input) {
        message0.setText(input);
    }
}