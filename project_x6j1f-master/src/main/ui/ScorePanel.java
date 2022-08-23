package ui;

import model.GameInTank;

import javax.swing.*;
import java.awt.*;

/*
 * Represents the score panel
 */
public class ScorePanel extends JPanel {
    public static final int LBL_WIDTH = 200;
    public static final int LBL_HEIGHT = 30;
    private static final String SCORE_TXT = "Score: ";
    private static final String DIFFICULTY_TXT = "Difficulty: ";
    private GameInTank game;
    private JLabel difficultyLbl;
    private JLabel scoreLbl;

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Constructs a score panel
    //          sets the background colour and draws the initial labels;
    //          updates this with the game whose score and difficulty to be displayed
    public ScorePanel(GameInTank g) {
        game = g;
        setBackground(new Color(180, 180, 180));
        scoreLbl = new JLabel(SCORE_TXT + game.getScore());
        scoreLbl.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        difficultyLbl = new JLabel(DIFFICULTY_TXT + game.getDifficulty());
        difficultyLbl.setPreferredSize(new Dimension(LBL_WIDTH, LBL_HEIGHT));
        add(scoreLbl);
        add(Box.createHorizontalStrut(10));
        add(difficultyLbl);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Updates the score panel
    //          updates score of player and difficulty of current progress
    // MODIFIES: this
    public void update() {
        scoreLbl.setText(SCORE_TXT + game.getScore());
        difficultyLbl.setText(DIFFICULTY_TXT + game.getDifficulty());
        repaint();
    }
}
