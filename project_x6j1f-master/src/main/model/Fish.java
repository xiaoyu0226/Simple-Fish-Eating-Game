package model;

/*
 * Represent a fish
 */
public interface Fish {
    // REQUIRES: rightEdge > 0 && bottomEdge > 0
    // EFFECTS: check whether the fish hit and passed the top, bottom, right and left allowed edge
    // of tank
    // if hit/pass the edge, change the coordinate of fish so fish stays inside screen right at edge
    // MODIFIES: this
    void handleHitTankEdge(int rightEdge, int bottomEdge);

    // EFFECTS: move/swim the fish by speed according to direction facing
    // MODIFIES: this
    void swim();

    // EFFECTS: get x coordinate of fish
    int getX();

    // EFFECTS: get y coordinate of fish
    int getY();

    // EFFECTS: get direction/facing of this fish
    String getDirection();

    // EFFECTS: set new x coordinate of fish
    // MODIFIES: this
    void setX(int xpos);

    // EFFECTS: set new y coordinate of fish
    // MODIFIES: this
    void setY(int ypos);

    // EFFECTS: set new direction facing of fish
    // MODIFIES: this
    void setDirection(String direction);
}
