package ui;

import model.BackgroundFish;
import model.GameInTank;
import model.PlayerFish;
import org.json.JSONException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

/*
 * Represents the game panel
 */
public class GamePanel extends JPanel {
    private static final String JSON_STORE = "./data/gamememory.json";
    private static final int BUTTON_HEIGHT = 35;
    private static final int BUTTON_WIDTH = 100;
    private static final String LOGO_STORE = "./data/fishLogo.jpg";
    private static final String RED_FISH_STORE = "./data/fishLogo1.jpg";
    private static final String YELLOW_FISH_STORE = "./data/fishLogo2.jpg";
    private static final String GREEN_FISH_STORE = "./data/fishLogo3.jpg";
    private static final String GAMEOVER_STORE = "./data/endAnime.png";
    private static final String PLAYER_STORE = "./data/player.png";
    private static final String SELECT_TXT = "Select from the following options:";
    private static final String LOAD_TXT = "Load";
    private static final String NEW_GAME_TXT = "NewGame";
    private static final String SAVE_TXT = "Save";
    private static final String QUIT_TXT = "Quit";
    private static final String INC_DIFFICULTY = "+Difficulty";
    private static final String DEC_DIFFICULTY = "-Difficulty";
    private static final String RESUME_TXT = "resume";
    private static final int NUM_TICK_CHANGE_PIC = 25;
    private static final int BETWEEN_MESSAGE_HEIGHT = 30;
    private static final int BUTTON_DISTANCE = 40;
    private static final int BUTTON_ONE_Y = 450;
    private int numTick;
    private JButton loadBtn;
    private JButton newGameBtn;
    private JButton saveBtn;
    private JButton quitBtn;
    private JButton increaseBtn;
    private JButton decreaseBtn;
    private JButton resumeBtn;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;
    private GameInTank game;

    // EFFECTS:  sets size and background colour of panel,
    //           sets up the load, newGame, save, quit, increase difficulty, decrease difficulty and resume buttons
    //           sets up the JsonWriter and JsonReader
    //           initialize all the buttons
    //           set initial number of tick be 0
    //           updates this with the game to be displayed
    public GamePanel(GameInTank g) throws IOException {
        setPreferredSize(new Dimension(GameInTank.WIDTH, GameInTank.HEIGHT));
        setBackground(Color.white);
        loadBtn = new JButton(LOAD_TXT);
        newGameBtn = new JButton(NEW_GAME_TXT);
        saveBtn = new JButton(SAVE_TXT);
        quitBtn = new JButton(QUIT_TXT);
        increaseBtn = new JButton(INC_DIFFICULTY);
        decreaseBtn = new JButton(DEC_DIFFICULTY);
        resumeBtn = new JButton(RESUME_TXT);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        this.game = g;
        this.numTick = 0;

        initializeButton();
    }

    // EFFECTS: add associated action for each button upon press
    //          load previous game if load button pressed
    //          start new game if new game button pressed
    //          save game progress if save button pressed
    //          quit game if quit button pressed
    //          increase difficulty of game if increase button pressed
    //          decrease difficulty of game if decrease button pressed
    //          resume game if resume button pressed
    // MODIFIES: this, JButton
    private void initializeButton() {
        loadBtn.addActionListener(e -> loadPreviousDifficulty());
        newGameBtn.addActionListener(e -> this.game.closeStartScreen());
        saveBtn.addActionListener(e -> saveAndQuit());
        quitBtn.addActionListener(e -> quit());
        increaseBtn.addActionListener(e -> this.game.increaseDifficulty());
        decreaseBtn.addActionListener(e -> this.game.decreaseDifficulty());
        resumeBtn.addActionListener(e -> this.game.resume());
    }

    // EFFECTS: let game print log and quit the game
    private void quit() {
        game.printLog();
        System.exit(0);
    }

    // Citation: partially learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: draw game screen
    //          draw game start screen if need to show start screen and disable start screen button otherwise
    //          if game is not ended and game not paused and game not showing start screen, draw the on going game
    //          if game is paused but not ended, and we do not show start screen, draw pause screen, otherwise disable
    //          pause button and delete saved message
    //          if game ended, draw game end screen
    // MODIFIES: this, graphics g
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game.showStartScreen()) {
            gameStartOption(g);
        } else {
            disableStartScreenButton();
        }

        if (!game.showStartScreen() && !game.getEndGame() && !game.getPause()) {
            drawGame(g);
        }

        if (!game.showStartScreen() && !game.getEndGame() && game.getPause()) {
            pauseGame(g);
        } else {
            disablePauseScreenButton();
        }
        if (game.getEndGame()) {
            gameOver(g);
        }
    }

    // EFFECTS: disable load and new game buttons associated with start screen and remove them
    // MODIFIES: this, Jbutton
    private void disableStartScreenButton() {
        loadBtn.setFocusable(false);
        newGameBtn.setFocusable(false);
        remove(loadBtn);
        remove(newGameBtn);
    }

    // EFFECTS: disable save, increase, decrease and resume buttons associated with pause screen and remove them
    // MODIFIES: this, Jbutton
    private void disablePauseScreenButton() {
        saveBtn.setFocusable(false);
        increaseBtn.setFocusable(false);
        decreaseBtn.setFocusable(false);
        resumeBtn.setFocusable(false);
        remove(increaseBtn);
        remove(decreaseBtn);
        remove(resumeBtn);
        remove(saveBtn);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draws the game
    //          draw players, enemy fish and prey fish
    // MODIFIES: graphics g
    private void drawGame(Graphics g) {
        try {
            drawPlayer(g);
        } catch (IOException e) {
            System.out.println("error loading image");
        }
        drawEnemyFishes(g);
        drawPreyFishes(g);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draw the player
    // MODIFIES: graphics g
    private void drawPlayer(Graphics g) throws IOException {
        BufferedImage playerImg = ImageIO.read(new File(PLAYER_STORE));
        PlayerFish t = game.getPlayer();
        g.drawImage(playerImg,t.getX() - PlayerFish.SIZE / 2,
                t.getY() - PlayerFish.SIZE / 2, PlayerFish.SIZE,
                PlayerFish.SIZE, null);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draw the list of enemy fish
    // MODIFIES: grpahics g
    private void drawEnemyFishes(Graphics g) {
        for (BackgroundFish next : game.getEnemy()) {
            drawEnemyFish(g, next);
        }
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draw an enemy fish
    // modifies: graphics g
    private void drawEnemyFish(Graphics g, BackgroundFish e) {
        Color savedCol = g.getColor();
        g.setColor(GameInTank.ENEMY_COLOR);
        g.fillOval(e.getX() - BackgroundFish.SIZE,
                e.getY() - BackgroundFish.SIZE / 2,
                BackgroundFish.SIZE,
                BackgroundFish.SIZE);
        g.setColor(savedCol);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draw the list of prey fish
    // MODIFIES: graphics g
    private void drawPreyFishes(Graphics g) {
        for (BackgroundFish next : game.getPrey()) {
            drawPreyFish(g, next);
        }
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draw a prey fish
    // MODIFIES: graphics g
    private void drawPreyFish(Graphics g, BackgroundFish e) {
        Color savedCol = g.getColor();
        g.setColor(GameInTank.PREY_COLOR);
        g.fillOval(e.getX() - BackgroundFish.SIZE,
                e.getY() - BackgroundFish.SIZE / 2,
                BackgroundFish.SIZE,
                BackgroundFish.SIZE);
        g.setColor(savedCol);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draws the game start screen with message
    //          draw the start screen buttons
    //          draw warning message on screen if game has loading error or no memory error
    // MODIFIES: graphics g
    private void gameStartOption(Graphics g) {
        try {
            drawStartImage(g);
        } catch (IOException e) {
            System.out.println("image cannot be loaded");
        }
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", 20, 18));
        FontMetrics fm = g.getFontMetrics();
        centreString("Welcome to the Survival Game as a Fish!", g, fm, GameInTank.HEIGHT / 2);
        centreString("You can press p to pause the game, and use button to increase or decrease the difficulty",
                g, fm, GameInTank.HEIGHT / 2 + BETWEEN_MESSAGE_HEIGHT);
        centreString(SELECT_TXT, g, fm, GameInTank.HEIGHT / 2 + 2 * BETWEEN_MESSAGE_HEIGHT);
        centreString("Load previous level from file", g, fm, GameInTank.HEIGHT / 2 + 3 * BETWEEN_MESSAGE_HEIGHT);
        centreString("Start New Game", g, fm, GameInTank.HEIGHT / 2 + 4 * BETWEEN_MESSAGE_HEIGHT);

        drawStartScreenButtons();

        if (game.getLoadingError()) {
            centreString("Cannot Load the file", g, fm, GameInTank.HEIGHT / 2 + 5 * BETWEEN_MESSAGE_HEIGHT);
        } else if (game.getNoMemory()) {
            centreString("No Saved Game Progress In History",
                    g, fm, GameInTank.HEIGHT / 2 + 5 * BETWEEN_MESSAGE_HEIGHT);
        }

        g.setColor(saved);
    }

    // EFFECTS: draw load and new game start screen buttons
    // MODIFIES: this, graphic g
    private void drawStartScreenButtons() {
        loadBtn.setBounds(GameInTank.WIDTH / 2 - loadBtn.getWidth() / 2,
                BUTTON_ONE_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        newGameBtn.setBounds(GameInTank.WIDTH / 2 - newGameBtn.getWidth() / 2,
                BUTTON_ONE_Y + BUTTON_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT);

        this.setLayout(null);
        add(loadBtn);
        add(newGameBtn);
    }

    // EFFECTS: draw decrease difficulty, increase difficulty and resume button associated with pause screen
    // MODIFIES: this, graphic g
    private void drawPauseScreenButtons() {
        decreaseBtn.setBounds(GameInTank.WIDTH / 2 - decreaseBtn.getWidth() / 2,
                BUTTON_ONE_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        increaseBtn.setBounds(GameInTank.WIDTH / 2 - increaseBtn.getWidth() / 2,
                BUTTON_ONE_Y + BUTTON_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT);
        resumeBtn.setBounds(GameInTank.WIDTH / 2 - resumeBtn.getWidth() / 2,
                BUTTON_ONE_Y + 2 * BUTTON_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT);
        loadBtn.setBounds(GameInTank.WIDTH / 2 - loadBtn.getWidth() / 2,
                BUTTON_ONE_Y - BUTTON_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT);
        saveBtn.setBounds(GameInTank.WIDTH / 2 - saveBtn.getWidth() / 2,
                BUTTON_ONE_Y - 2 * BUTTON_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT);

        this.setLayout(null);
        add(decreaseBtn);
        add(increaseBtn);
        add(resumeBtn);
        add(loadBtn);
        add(saveBtn);
    }

    // EFFECTS: draw beginning start screen animation
    // MODIFIES: this, graphic g
    private void drawStartImage(Graphics g) throws IOException {
        BufferedImage img = ImageIO.read(new File(LOGO_STORE));
        BufferedImage imgRed = ImageIO.read(new File(RED_FISH_STORE));
        BufferedImage imgYellow = ImageIO.read(new File(YELLOW_FISH_STORE));
        BufferedImage imgGreen = ImageIO.read(new File(GREEN_FISH_STORE));

        if (numTick < NUM_TICK_CHANGE_PIC) {
            g.drawImage(imgRed, GameInTank.WIDTH / 2 - img.getWidth() / 10,
                    GameInTank.WIDTH / 5, imgRed.getWidth() / 5, imgRed.getHeight() / 5, null);
        } else if (numTick < 2 * NUM_TICK_CHANGE_PIC) {
            g.drawImage(imgYellow, GameInTank.WIDTH / 2 - img.getWidth() / 10 + imgRed.getWidth() / 5,
                    GameInTank.WIDTH / 5, imgYellow.getWidth() / 5,
                    imgYellow.getHeight() / 5, null);
        } else if (numTick < 3 * NUM_TICK_CHANGE_PIC) {
            g.drawImage(imgGreen, GameInTank.WIDTH / 2
                            - img.getWidth() / 10
                            + imgRed.getWidth() / 5
                            + imgYellow.getWidth() / 5,
                    GameInTank.WIDTH / 5,
                    imgGreen.getWidth() / 5, imgGreen.getHeight() / 5, null);
        } else {
            g.drawImage(img, GameInTank.WIDTH / 2 - img.getWidth() / 10,
                    GameInTank.WIDTH / 5, img.getWidth() / 5, img.getHeight() / 5, null);
        }

        numTick++;
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draws the game over messages and save and quit options
    //          draw end screen buttons
    // MODIFIES: graphics g
    private void gameOver(Graphics g) {
        try {
            drawEndImage(g);
        } catch (IOException e) {
            System.out.println("image cannot be loaded");
        }
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString("You finished with a score of " + game.getScore(), g, fm, GameInTank.HEIGHT / 2);
        centreString(SELECT_TXT, g, fm, GameInTank.HEIGHT / 2 + BETWEEN_MESSAGE_HEIGHT);
        centreString("Save Progress and Quit",
                g, fm, GameInTank.HEIGHT / 2 + 2 * BETWEEN_MESSAGE_HEIGHT);
        centreString("Quit Game", g, fm, GameInTank.HEIGHT / 2 + 3 * BETWEEN_MESSAGE_HEIGHT);
        drawEndScreenButtons();

        if (game.getSavedProgress()) {
            centreString("Saved the game to" + JSON_STORE,
                    g, fm, GameInTank.HEIGHT / 2 + 4 * BETWEEN_MESSAGE_HEIGHT);
        }

        g.setColor(saved);
    }

    // EFFECTS: draw game end picture
    // MODIFIES: this, graphic g
    private void drawEndImage(Graphics g) throws IOException {
        BufferedImage endImg = ImageIO.read(new File(GAMEOVER_STORE));
        g.drawImage(endImg, GameInTank.WIDTH / 2 - endImg.getWidth() / 8,
                GameInTank.WIDTH / 20, endImg.getWidth() / 4, endImg.getHeight() / 4, null);
    }

    // EFFECTS: draw save and quit button associated with game end screen
    // MODIFIES: this, graphic g
    private void drawEndScreenButtons() {
        saveBtn.setBounds(GameInTank.WIDTH / 2 - saveBtn.getWidth() / 2,
                BUTTON_ONE_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        quitBtn.setBounds(GameInTank.WIDTH / 2 - quitBtn.getWidth() / 2,
                BUTTON_ONE_Y + BUTTON_DISTANCE, BUTTON_WIDTH, BUTTON_HEIGHT);

        this.setLayout(null);
        add(saveBtn);
        add(quitBtn);
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Draw the pause screen with message with option message to increase or decrease level
    //          draw pause screen buttons
    // MODIFIES: graphic g
    private void pauseGame(Graphics g) {
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString("Game Paused", g, fm, GameInTank.HEIGHT / 3);
        centreString(SELECT_TXT, g, fm, GameInTank.HEIGHT / 3 + BETWEEN_MESSAGE_HEIGHT);
        centreString("Increase or Decrease Difficulty: Current Difficulty " + game.getDifficulty(),
                g, fm, GameInTank.HEIGHT / 3 + 2 * BETWEEN_MESSAGE_HEIGHT);
        centreString("Load from Memory or Save and Quit Game",
                g, fm, GameInTank.HEIGHT / 3 + 3 * BETWEEN_MESSAGE_HEIGHT);
        centreString("Resume", g, fm, GameInTank.HEIGHT / 3 + 4 * BETWEEN_MESSAGE_HEIGHT);

        if (game.getSavedProgress()) {
            centreString("Saved the game to" + JSON_STORE,
                    g, fm, GameInTank.HEIGHT / 3 + 5 * BETWEEN_MESSAGE_HEIGHT);
        }
        drawPauseScreenButtons();
    }

    // Citation: Learned from SpaceInvader provided in Class CPSC210
    // EFFECTS: Centres a string on the screen horizontally on graphics
    // modifies: graphics g
    private void centreString(String str, Graphics g, FontMetrics fm, int y) {
        int width = fm.stringWidth(str);
        g.drawString(str, (GameInTank.WIDTH - width) / 2, y);
    }

    // Citation: Learned from JsonSerializationDemo provided in Class CPSC210
    // EFFECTS: saves the game progress to file
    // MODIFIES: this, jsonWriter
    private void saveDifficulty() {
        try {
            jsonWriter.open();
            jsonWriter.write(this.game);
            jsonWriter.close();
            game.savedProgress();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // Citation: Learned from JsonSerializationDemo provided in Class CPSC210
    // MODIFIES: this, GameInTank
    // EFFECTS: load the previous game parameters from file and set game parameters
    //          - load previous difficulty
    //          - load previous score
    //          - load previous player
    //          - load previous list of prey fish
    //          - load previous list of enemy fish
    //          - close start screen to true
    //          - catch loading and saving exception and draw warning on screen for user
    private void loadPreviousDifficulty() {
        try {
            game.setDifficulty(jsonReader.getPreviousDifficulty());
            game.setPrey(jsonReader.getPreviousPrey());
            game.setEnemy(jsonReader.getPreviousEnemy());
            game.setPlayer(jsonReader.getPreviousPlayer());
            game.setScore(jsonReader.getPreviousScore());
            removeLoadedCollisionEnemyFish();
            game.loadAndCloseStartScreen();
        } catch (IOException e) {
            game.setLoadingError();
        } catch (JSONException e) {
            game.setNoMemoryError();
        }
    }

    // EFFECTS: remove the loaded enemy fish that previous collided with the player
    // MODIFIES: this
    private void removeLoadedCollisionEnemyFish() {
        Set<BackgroundFish> enemies = game.getEnemy();
        for (BackgroundFish enemy: enemies) {
            if (game.getPlayer().collideWith(enemy)) {
                enemies.remove(enemy);
                break;
            }
        }
        game.setEnemy(enemies);
    }

    // EFFECTS: save game progress and let game print log then quit game after time delay
    private void saveAndQuit() {
        saveDifficulty();
        Timer timer = new Timer(2000, ae -> {
            game.printLog();
            System.exit(0);
        });
        timer.start();
    }
}
