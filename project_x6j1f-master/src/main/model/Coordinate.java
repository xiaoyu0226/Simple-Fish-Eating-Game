package model;

/*
 * Represents a coordinate
 */
public class Coordinate {
    private int posX;
    private int posY;

    // EFFECTS: create a coordinate (x, y)
    public Coordinate(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    // EFFECTS: return the x coordinate
    public int getX() {
        return this.posX;
    }

    // EFFECTS: return the y coordinate
    public int getY() {
        return this.posY;
    }
}
