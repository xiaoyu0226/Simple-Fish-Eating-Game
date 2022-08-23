package model;

import ui.ScorePanel;

/*
 * Represent the fish in background
 */
public class BackgroundFish implements Fish {
    public static final int SIZE = 30;
    public static final int SPEED = 5;
    private int positionX;
    private int positionY;
    private String direction;

    // constructs a BackgroundFish
    // EFFECTS: set the initial x, y coordinate of fish and initialize the direction facing of fish
    public BackgroundFish(int x, int y, String facing) {
        this.positionX = x;
        this.positionY = y;
        this.direction = facing;
    }

    // EFFECTS: move/swim the  fish by speed according to direction facing
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

    // REQUIRES: rightEdge > 0 && bottomEdge > 0
    // EFFECTS: check whether the fish hit and passed the top, bottom, right and left allowed edge
    // of tank
    // if hit/pass the edge, change the coordinate of fish right at edge and change the fish
    // direction to opposite facing
    // MODIFIES: this
    @Override
    public void handleHitTankEdge(int rightEdge, int bottomEdge) {
        if (this.positionX < SIZE / 2) {
            this.setX(SIZE / 2);
            this.direction = "right";
        } else if (this.positionX > rightEdge - (SIZE / 2)) {
            this.setX(rightEdge - (SIZE / 2));
            this.direction = "left";
        }

        if (this.positionY < ScorePanel.LBL_HEIGHT) {
            this.setY(ScorePanel.LBL_HEIGHT);
            this.direction = "down";
        } else if (this.positionY > bottomEdge - (SIZE / 2)) {
            this.setY(bottomEdge - (SIZE / 2));
            this.direction = "up";
        }
    }

    // EFFECTS: get x coordinate of the fish
    @Override
    public int getX() {
        return this.positionX;
    }

    // EFFECTS: get y coordinate of the fish
    @Override
    public int getY() {
        return this.positionY;
    }

    // EFFECTS: get direction/facing of this fish
    @Override
    public String getDirection() {
        return this.direction;
    }

    // EFFECTS: set new x coordinate for fish
    // MODIFIES: this
    @Override
    public void setX(int xpos) {
        this.positionX = xpos;
    }

    // EFFECTS: set new y coordinate for fish
    // MODIFIES: this
    @Override
    public void setY(int ypos) {
        this.positionY = ypos;
    }

    // EFFECTS: set a new direction facing for fish
    // MODIFIES: this
    @Override
    public void setDirection(String direction) {
        this.direction = direction;
    }

}
