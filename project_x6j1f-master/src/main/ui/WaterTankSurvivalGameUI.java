package ui;

import model.GameInTank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/*
 * Represents the water tank survival game frame
 */
public class WaterTankSurvivalGameUI extends JFrame {
    private static final int INTERVAL = 10;
    private GameInTank game;
    private GamePanel gp;
    private ScorePanel sp;

    // EFFECTS: Constructs main game window
    //          sets up window in which Water Tank Survival Game will be displayed
    //          set up and initialize game
    //          set up and add game panel and score panel
    //          add key listener
    //          adjust game window size and center the window on screen
    //          set game window visible and focusable
    //          add timer
    public WaterTankSurvivalGameUI() throws IOException {
        super("Water Tank Survival Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        game = new GameInTank();
        game.initialization();
        gp = new GamePanel(game);
        sp = new ScorePanel(game);
        add(gp);
        add(sp, BorderLayout.NORTH);
        addKeyListener(new KeyHandler());
        pack();
        centreOnScreen();
        setVisible(true);
        setFocusable(true);
        addTimer();
    }

    // EFFECTS: Set up timer
    //          initializes a timer that updates game each
    //          INTERVAL milliseconds
    // MODIFIES: game, gamePanel, scorePanel
    private void addTimer() {
        Timer t = new Timer(INTERVAL, ae -> {
            if (!game.getEndGame() && !game.showStartScreen() && !game.getPause()) {
                game.tick();
            }
            gp.repaint();
            sp.update();
        });

        t.start();
    }

    // EFFECTS: Centres the frame on desktop
    // MODIFIES: this
    private void centreOnScreen() {
        Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
    }

     /*
     * A key handler to respond to key events
     */
    private class KeyHandler extends KeyAdapter {

        // EFFECTS: handle key events
        //          if game not ended and game not paused, handle user key input
        @Override
        public void keyPressed(KeyEvent e) {
            if (!game.getEndGame() && !game.getPause()) {
                handleUserInput(e.getKeyCode());
            }
        }
    }

    // EFFECTS: process user key presses
    //          - change player direction moving up if up arrow pressed
    //          - change player direction down if down arrow pressed
    //          - change player direction left if left arrow pressed
    //          - change player direction right if right arrow pressed
    //          - no key pressed, do not modify anything
    // MODIFIES: this, game
    private void handleUserInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_P:
                this.game.setPause();
            case KeyEvent.VK_UP:
                this.game.getPlayer().setDirection("up");
                break;
            case KeyEvent.VK_DOWN:
                this.game.getPlayer().setDirection("down");
                break;
            case KeyEvent.VK_RIGHT:
                this.game.getPlayer().setDirection("right");
                break;
            case KeyEvent.VK_LEFT:
                this.game.getPlayer().setDirection("left");
                break;
        }
    }

    // EFFECTS: create and start the game
    public static void main(String[] args) throws IOException {
        WaterTankSurvivalGameUI game = new WaterTankSurvivalGameUI();
    }
}
