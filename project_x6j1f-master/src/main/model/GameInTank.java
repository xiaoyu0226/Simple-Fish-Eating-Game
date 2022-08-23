package model;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Random;

/*
 * Represent the game in tank
 */
public class GameInTank {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int INITIAL_FISH_NUMBER = 5;
    public static final int LEVEL_UP_REQUIREMENT = 5;
    public static final int NUMBER_TICKS_CHANGE_DIRECTION = 30;
    public static final Color ENEMY_COLOR = new Color(200, 0, 0);
    public static final Color PREY_COLOR = new Color(0, 150, 0);
    private final int tankWidth;
    private final int tankHeight;
    private PlayerFish player;
    private Set<BackgroundFish> listPreyFish;
    private Set<BackgroundFish> listEnemyFish;
    private int difficulty;
    private int score;
    private boolean endGame;
    private boolean showStartOption;
    private boolean progressSaved;
    private boolean noMemory;
    private boolean loadingError;
    private boolean pause;
    private int numTicks;

    // constructs a game in tank
    // EFFECTS: create a gameInTank object with initial tank width and height
    //          - initial progressSaved is false
    //          - initial noMemory is false
    //          - initial loadingError is false
    //          - initial showStartOption is true
    //          - initial pause is false
    //          - initial end game is false
    //          - initial score be 0 and difficulty of 1
    //          - initial game ticked 0 times
    //          - initialize a new player fish with coordinate centered in tank and direction facing right
    //          - two empty sets for prey fish and enemy fish
    public GameInTank() {
        this.noMemory = false;
        this.loadingError = false;
        this.progressSaved = false;
        this.showStartOption = true;
        this.pause = false;
        this.endGame = false;
        this.tankWidth = WIDTH;
        this.tankHeight = HEIGHT;
        this.player = new PlayerFish(this.tankWidth / 2, this.tankHeight / 2);
        this.score = 0;
        this.difficulty = 1;
        this.listPreyFish = new HashSet<>();
        this.listEnemyFish = new HashSet<>();
        this.numTicks = 0;
    }

    // EFFECTS: initialize two fish sets and add initial allowed number of enemy fish to enemy set
    //          - initialize allowed initial number of prey fish to prey fish set
    // MODIFIES: this
    public void initialization() {
        for (int i = 1; i <= INITIAL_FISH_NUMBER; i++) {
            listEnemyFish.add(generateFish(generateRandomCoordinate()));
            listPreyFish.add(generateFish(generateRandomCoordinate()));
        }
    }

    // EFFECTS: generate a random coordinate with random x and y coordinates on screen
    public Coordinate generateRandomCoordinate() {
        int offset = 2;
        int upperBoundX = tankWidth - offset;
        int upperBoundY = tankHeight - offset;

        Random random = new Random();
        int coordinateX = random.nextInt(upperBoundX) + offset;
        int coordinateY = random.nextInt(upperBoundY) + offset;

        return new Coordinate(coordinateX, coordinateY);
    }

    // REQUIRES: coordinate !=null
    // EFFECTS: generate a background fish with given coordinate and random facing direction
    public BackgroundFish generateFish(Coordinate coordinate) {
        String facing = generateFacing();
        return new BackgroundFish(coordinate.getX(), coordinate.getY(), facing);
    }

    // EFFECTS: generate a random facing direction
    public String generateFacing() {
        Random random = new Random();
        int upperBound = 4;
        int randomNum = random.nextInt(upperBound);
        if (randomNum == 1) {
            return "up";
        } else if (randomNum == 2) {
            return "down";
        } else if (randomNum == 3) {
            return "left";
        } else {
            return "right";
        }
    }

    // REQUIRES: given facing is either left, right, up or down
    // EFFECTS: generate a random facing direction different from current facing
    public String generateFacingNoRepeat(String facing) {
        String newFacing = facing;
        Random random = new Random();
        int upperBound = 4;

        while (Objects.equals(newFacing, facing)) {
            int randomNum = random.nextInt(upperBound);
            if (randomNum == 1) {
                newFacing = "up";
            } else if (randomNum == 2) {
                newFacing = "down";
            } else if (randomNum == 3) {
                newFacing = "left";
            } else {
                newFacing = "right";
            }
        }
        return newFacing;
    }

    // EFFECTS: run 1 tick cycle of game
    // swim player fish, handle whether player hit tank edge
    // swim each enemy and prey fish in both fish sets, and handle if prey and enemy fish hit tank edge
    // check whether to change the direction facing of all prey fish and enemy fish
    // check whether player hit enemy fish and whether to end the game or not
    // check whether player eat any prey fish
    // check the difficulty of the game
    // increase the number of game tick cycle by 1
    // MODIFIES: this
    public void tick() {
        this.player.swim();
        this.player.handleHitTankEdge(this.tankWidth, this.tankHeight);

        swimListFish(listPreyFish);
        swimListFish(listEnemyFish);

        checkChangeFishListDirection();

        if (playerHitEnemy()) {
            this.endGame = true;
            return;
        }

        checkEatPrey();
        checkDifficulty();
        numTicks++;
    }

    // EFFECTS: check whether to change list of enemy and prey fish direction of swimming
    //          if game ticks reach a certain amount:
    //          - change each fish to a different direction
    //          - reset the counting on number of game ticks to 0
    // MODIFIES: this
    public void checkChangeFishListDirection() {
        if (numTicks == NUMBER_TICKS_CHANGE_DIRECTION) {
            for (BackgroundFish prey : listPreyFish) {
                prey.setDirection(generateFacingNoRepeat(prey.getDirection()));
            }
            for (BackgroundFish enemy : listEnemyFish) {
                enemy.setDirection(generateFacingNoRepeat(enemy.getDirection()));
            }
            numTicks = 0;
        }
    }

    // EFFECTS: make the list of background fish swim and handle if fish hit tank edge
    // MODIFIES: this
    public void swimListFish(Set<BackgroundFish> listFish) {
        for (BackgroundFish fish : listFish) {
            fish.swim();
            fish.handleHitTankEdge(this.tankWidth, this.tankHeight);
        }
    }

    // EFFECTS: check the difficulty of the current game progress
    //          if necessary, increase difficulty base on current score
    //          add log event if difficulty level up
    // MODIFIES: this
    public void checkDifficulty() {
        this.difficulty = this.score / LEVEL_UP_REQUIREMENT + 1;

        if (listEnemyFish.size() < this.difficulty * INITIAL_FISH_NUMBER) {
            int numEnemyNeeded = this.difficulty * INITIAL_FISH_NUMBER - listEnemyFish.size();
            for (int i = 1; i <= numEnemyNeeded; i++) {
                listEnemyFish.add(generateFish(generateRandomCoordinate()));
            }

            EventLog.getInstance().logEvent(new Event("Difficulty level up! Increase "
                    + INITIAL_FISH_NUMBER + " enemies to game."));
        }
    }

    // EFFECTS: check whether player fish hit any enemy fish
    public boolean playerHitEnemy() {
        boolean hitEnemy = false;
        for (BackgroundFish enemy : listEnemyFish) {
            if (this.player.collideWith(enemy)) {
                hitEnemy = true;
                break;
            }
        }
        return hitEnemy;
    }

    // EFFECTS: check whether player ate a prey
    //          - if player hit a prey, remove eaten prey
    //          - add new prey with random coordinate and facing
    //          - increase score by 1
    //          - add log event related to eat prey
    // MODIFIES: this
    public void checkEatPrey() {
        for (BackgroundFish prey : listPreyFish) {
            if (this.player.collideWith(prey)) {
                listPreyFish.remove(prey);
                listPreyFish.add(generateFish(generateRandomCoordinate()));
                this.score += 1;

                EventLog.getInstance().logEvent(new Event("You consumed a prey! A new prey added to game."));
                break;
            }
        }
    }

    // EFFECTS: return endGame
    public boolean getEndGame() {
        return this.endGame;
    }

    // EFFECTS: return list of prey fish
    public Set<BackgroundFish> getPrey() {
        return this.listPreyFish;
    }

    // EFFECTS: return list of enemy fish
    public Set<BackgroundFish> getEnemy() {
        return this.listEnemyFish;
    }

    // EFFECTS: return game score
    public int getScore() {
        return this.score;
    }

    // EFFECTS: return game player
    public PlayerFish getPlayer() {
        return this.player;
    }

    // EFFECTS: return game difficulty
    public int getDifficulty() {
        return this.difficulty;
    }

    // EFFECTS: return game tank height
    public int getHeight() {
        return this.tankHeight;
    }

    // EFFECTS: return game tank width
    public int getWidth() {
        return this.tankWidth;
    }

    // EFFECTS: return number of game cycle ticks
    public int getNumTick() {
        return this.numTicks;
    }

    // REQUIRES: num >= 0;
    // EFFECTS: set the value of number of game cycle ticks
    // MODIFIES: this
    public void setNumTick(int num) {
        this.numTicks = num;
    }

    // REQUIRES: num >= 0
    // EFFECTS: set the score according to num
    // MODIFIES: this
    public void setScore(int num) {
        this.score = num;
    }

    // REQUIRES: num >= 1;
    // EFFECTS: set the value of game difficulty
    // MODIFIES: this
    public void setDifficulty(int loadedDifficulty) {
        this.difficulty = loadedDifficulty;
    }

    // EFFECTS: set the player for the game
    // MODIFIES: this
    public void setPlayer(PlayerFish player) {
        this.player = player;
    }

    // EFFECTS: set the list of enemy fish for this game
    // MODIFIES: this
    public void setEnemy(Set<BackgroundFish> enemy) {
        this.listEnemyFish = enemy;
    }

    // EFFECTS: set the list of prey fish for this game
    // MODIFIES: this
    public void setPrey(Set<BackgroundFish> prey) {
        this.listPreyFish = prey;
    }

    // EFFECTS: return showStartOption
    public boolean showStartScreen() {
        return this.showStartOption;
    }

    // EFFECTS: set show start option to false
    //          add log event related to starting new game
    // MODIFIES: this
    public void closeStartScreen() {
        this.showStartOption = false;

        EventLog.getInstance().logEvent(new Event("Start new game with 5 enemy, 5 prey and player added "
                + "to game"));
    }

    // EFFECTS: set progressSaved to true
    // MODIFIES: this
    public void savedProgress() {
        this.progressSaved = true;
    }

    // EFFECTS: return progressSaved
    public boolean getSavedProgress() {
        return this.progressSaved;
    }

    // EFFECTS: set loadingError to true
    // MODIFIES: this
    public void setLoadingError() {
        this.loadingError = true;
    }

    // EFFECTS: return loadingError
    public boolean getLoadingError() {
        return this.loadingError;
    }

    // EFFECTS: set noMemory to true
    // MODIFIES: this
    public void setNoMemoryError() {
        this.noMemory = true;
    }

    // EFFECTS: return noMemory
    public boolean getNoMemory() {
        return this.noMemory;
    }

    // EFFECTS: set pause
    // MODIFIES: this
    public void setPause() {
        this.pause = true;
    }

    // EFFECTS: return pause
    public boolean getPause() {
        return this.pause;
    }

    // EFFECTS: resume game
    // MODIFIES: this
    public void resume() {
        this.pause = false;
    }

    // EFFECTS: decrease the difficulty of game by 1, lowest is difficulty 1
    //          modify the score according to difficulty
    //          add log event related to decrease difficulty
    // MODIFIES: this
    public void decreaseDifficulty() {
        if (this.difficulty > 1) {
            this.difficulty--;

            this.score = (this.difficulty - 1) * INITIAL_FISH_NUMBER;

            Set<BackgroundFish> enemiesRemaining = new HashSet<>();
            int numEnemiesAdded = 0;
            for (BackgroundFish enemy : this.getEnemy()) {
                if (numEnemiesAdded < this.difficulty * INITIAL_FISH_NUMBER) {
                    enemiesRemaining.add(enemy);
                    numEnemiesAdded++;
                } else {
                    break;
                }
            }
            this.setEnemy(enemiesRemaining);

            EventLog.getInstance().logEvent(new Event("Decreased difficulty by 1, remove "
                    + INITIAL_FISH_NUMBER
                    + " enemy from the game."));
        }
    }

    // EFFECTS: increase the difficulty of game by 1
    //          modify the score according to difficulty
    //          add log event related to increase difficulty
    // MODIFIES: this
    public void increaseDifficulty() {
        this.difficulty++;
        this.score = (this.difficulty - 1) * INITIAL_FISH_NUMBER;

        EventLog.getInstance().logEvent(new Event("Increased difficulty by 1, add " + INITIAL_FISH_NUMBER
                + " more enemy to game."));
    }

    // EFFECTS: set progressSaved to false
    // MODIFIES: this
    public void unsavedProgress() {
        this.progressSaved = false;
    }

    // EFFECTS: set show start option to false
    //          add log event and message related to loading previous game
    // MODIFIES: this
    public void loadAndCloseStartScreen() {
        this.showStartOption = false;

        EventLog.getInstance().logEvent(new Event("previous difficulty loaded with previous player and "
                + this.listEnemyFish.size() + " enemy and " + this.listPreyFish.size() + " prey added to game"));
    }

    // EFFECTS: print the event log to screen
    public void printLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString());
        }
    }
}

