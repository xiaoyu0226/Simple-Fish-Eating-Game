package model;

import ui.ScorePanel;

import static java.lang.Math.sqrt;

/*
 * Represent the fish controlled by player
 */
public class PlayerFish implements Fish {
    public static final int SIZE = 30;
    public static final String DEFAULT_DIRECTION = "right";
    public static final int SPEED = 5;
    private int positionX;
    private int positionY;
    private String direction;

    // Constructs a player fish
    // EFFECTS: constructs a player fish with initial (x,y) coordinate
    // initialize direction facing right
    public PlayerFish(int x, int y) {
        this.positionX = x;
        this.positionY = y;
        this.direction = DEFAULT_DIRECTION;
    }

    // REQUIRES: rightEdge > 0 && bottomEdge > 0
    // EFFECTS: check whether the player fish hit and passed the top, bottom, right and left allowed edge
    // of tank
    // if hit/pass the edge, change the coordinate of fish so fish is visible and right close to the edge
    // MODIFIES: this
    @Override
    public void handleHitTankEdge(int rightEdge, int bottomEdge) {
        if (this.positionX <= (SIZE / 2)) {
            this.setX((SIZE / 2));
        } else if (this.positionX >= rightEdge - (SIZE / 2)) {
            this.setX(rightEdge - (SIZE / 2));
        }
        if (this.positionY <= ScorePanel.LBL_HEIGHT) {
            this.setY(ScorePanel.LBL_HEIGHT);
        } else if (this.positionY >= bottomEdge - (SIZE / 2)) {
            this.setY(bottomEdge - (SIZE / 2));
        }
    }

    // EFFECTS: move/swim the player fish by speed according to direction facing
    //         - move up by 1 speed unit if direction is up
    //         - move down by 1 speed unit if direction is down
    //         - move left by 1 speed unit if direction is left
    //         - move right by 1 speed unit if direction is right
    // MODIFIES: this
    @Override
    public void swim() {
        switch (this.direction) {
            case "up":
                this.positionY -= SPEED;
                break;
            case "down":
                this.positionY += SPEED;
                break;
            case "left":
                this.positionX -= SPEED;
                break;
            default:
                this.positionX += SPEED;
        }
    }

    // REQUIRES: fish != null
    // EFFECTS: return true if the player fish collides with other fish
    //          or will be involved in a side by side head on collision
    //          return false otherwise
    public boolean collideWith(BackgroundFish fish) {
        double dx = this.positionX - fish.getX();
        double dy = this.positionY - fish.getY();
        double distance = sqrt(dx * dx + dy * dy);
        return (distance < SIZE);
    }

    // EFFECTS: get x coordinate of player fish
    @Override
    public int getX() {
        return this.positionX;
    }

    // EFFECTS: get y coordinate of player fish
    @Override
    public int getY() {
        return this.positionY;
    }

    // EFFECTS: get direction/facing of this fish
    @Override
    public String getDirection() {
        return this.direction;
    }

    // EFFECTS: set new x coordinate of player fish
    // MODIFIES: this
    @Override
    public void setX(int xpos) {
        this.positionX = xpos;
    }

    // EFFECTS: set new y coordinate of player fish
    // MODIFIES: this
    @Override
    public void setY(int ypos) {
        this.positionY = ypos;
    }

    // EFFECTS: set new direction facing of player fish
    // MODIFIES: this
    @Override
    public void setDirection(String direction) {
        this.direction = direction;
    }
}
