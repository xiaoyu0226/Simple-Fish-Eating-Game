package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.GamePanel;
import ui.ScorePanel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BackgroundFishTest {
    public static final int TANK_RIGHT_EDGE = GameInTank.WIDTH;
    public static final int TANK_BOTTOM_EDGE = GameInTank.HEIGHT;
    public static final String DEFAULT_DIRECTION = "up";
    private BackgroundFish backgroundFish;

    @BeforeEach
    void runBefore() {
        backgroundFish = new BackgroundFish(TANK_RIGHT_EDGE / 2, TANK_BOTTOM_EDGE / 2, DEFAULT_DIRECTION);
    }

    @Test
    void testBackgroundFish() {
        assertNotNull(backgroundFish);
        assertEquals(TANK_RIGHT_EDGE / 2, backgroundFish.getX());
        assertEquals(TANK_BOTTOM_EDGE / 2, backgroundFish.getY());
        assertEquals(DEFAULT_DIRECTION, backgroundFish.getDirection());
    }

    @Test
    void testSwimVertically() {
        assertEquals(DEFAULT_DIRECTION, backgroundFish.getDirection());

        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2, backgroundFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2 - i * BackgroundFish.SPEED, backgroundFish.getY());
        }

        backgroundFish.setDirection("down");
        backgroundFish.setY(TANK_BOTTOM_EDGE / 2);
        assertEquals("down", backgroundFish.getDirection());
        assertEquals(TANK_BOTTOM_EDGE / 2, backgroundFish.getY());

        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2, backgroundFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2 + i * BackgroundFish.SPEED, backgroundFish.getY());
        }
    }

    @Test
    void testSwimHorizontally() {
        backgroundFish.setDirection("left");
        assertEquals("left", backgroundFish.getDirection());

        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2 - i * BackgroundFish.SPEED, backgroundFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2, backgroundFish.getY());
        }

        backgroundFish.setDirection("right");
        backgroundFish.setX(TANK_RIGHT_EDGE / 2);
        assertEquals("right", backgroundFish.getDirection());
        assertEquals(TANK_RIGHT_EDGE / 2, backgroundFish.getX());

        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2 + i * BackgroundFish.SPEED, backgroundFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2, backgroundFish.getY());
        }
    }

    @Test
    void testHandleHitTopTankEdge() {
        int numOfSwim = 3;
        backgroundFish.setY(ScorePanel.LBL_HEIGHT + numOfSwim * BackgroundFish.SPEED);
        assertEquals(ScorePanel.LBL_HEIGHT + numOfSwim * BackgroundFish.SPEED,
                backgroundFish.getY());

        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(ScorePanel.LBL_HEIGHT + (numOfSwim - i) * BackgroundFish.SPEED,
                    backgroundFish.getY());
            assertEquals(DEFAULT_DIRECTION, backgroundFish.getDirection());
        }

        backgroundFish.swim();
        assertEquals(ScorePanel.LBL_HEIGHT - BackgroundFish.SPEED, backgroundFish.getY());
        assertEquals(DEFAULT_DIRECTION, backgroundFish.getDirection());
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(ScorePanel.LBL_HEIGHT, backgroundFish.getY());
        assertEquals("down", backgroundFish.getDirection());

        backgroundFish.swim();
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(ScorePanel.LBL_HEIGHT + BackgroundFish.SPEED, backgroundFish.getY());
        assertEquals("down", backgroundFish.getDirection());
    }

    @Test
    void testHandleHitBottomTankEdge() {
        int numOfSwim = 3;
        backgroundFish.setY((TANK_BOTTOM_EDGE - BackgroundFish.SIZE / 2 - numOfSwim * BackgroundFish.SPEED));
        assertEquals(TANK_BOTTOM_EDGE -  BackgroundFish.SIZE / 2 - numOfSwim * BackgroundFish.SPEED,
                backgroundFish.getY());

        backgroundFish.setDirection("down");
        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(TANK_BOTTOM_EDGE  - BackgroundFish.SIZE / 2 - (numOfSwim - i) * BackgroundFish.SPEED,
                    backgroundFish.getY());
            assertEquals("down", backgroundFish.getDirection());
        }

        backgroundFish.swim();
        assertEquals(TANK_BOTTOM_EDGE - BackgroundFish.SIZE / 2 + BackgroundFish.SPEED,
                backgroundFish.getY());
        assertEquals("down", backgroundFish.getDirection());
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(TANK_BOTTOM_EDGE - BackgroundFish.SIZE / 2, backgroundFish.getY());
        assertEquals("up", backgroundFish.getDirection());

        backgroundFish.swim();
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(TANK_BOTTOM_EDGE - BackgroundFish.SIZE / 2 - BackgroundFish.SPEED,
                backgroundFish.getY());
        assertEquals("up", backgroundFish.getDirection());
    }

    @Test
    void testHandleHitLeftTankEdge() {
        int numOfSwim = 3;
        backgroundFish.setX(numOfSwim * BackgroundFish.SPEED + BackgroundFish.SIZE / 2);
        assertEquals(numOfSwim * BackgroundFish.SPEED + BackgroundFish.SIZE / 2, backgroundFish.getX());

        backgroundFish.setDirection("left");
        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals((numOfSwim - i) * BackgroundFish.SPEED + BackgroundFish.SIZE / 2,
                    backgroundFish.getX());
            assertEquals("left", backgroundFish.getDirection());
        }

        backgroundFish.swim();
        assertEquals(BackgroundFish.SIZE / 2 - BackgroundFish.SPEED, backgroundFish.getX());
        assertEquals("left", backgroundFish.getDirection());
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(BackgroundFish.SIZE / 2, backgroundFish.getX());
        assertEquals("right", backgroundFish.getDirection());

        backgroundFish.swim();
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(BackgroundFish.SIZE / 2 + BackgroundFish.SPEED, backgroundFish.getX());
        assertEquals("right", backgroundFish.getDirection());
    }

    @Test
    void testHandleHitRightTankEdge() {
        int numOfSwim = 3;
        backgroundFish.setX((TANK_RIGHT_EDGE - BackgroundFish.SIZE / 2 - numOfSwim * BackgroundFish.SPEED));
        assertEquals((TANK_RIGHT_EDGE - BackgroundFish.SIZE / 2 - numOfSwim * BackgroundFish.SPEED),
                backgroundFish.getX());

        backgroundFish.setDirection("right");
        for (int i = 1; i <= 3; i++) {
            backgroundFish.swim();
            backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(TANK_RIGHT_EDGE  - BackgroundFish.SIZE / 2 - (numOfSwim - i) * BackgroundFish.SPEED,
                    backgroundFish.getX());
            assertEquals("right", backgroundFish.getDirection());
        }

        backgroundFish.swim();
        assertEquals(TANK_RIGHT_EDGE - BackgroundFish.SIZE / 2 + BackgroundFish.SPEED,
                backgroundFish.getX());
        assertEquals("right", backgroundFish.getDirection());
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(TANK_RIGHT_EDGE - BackgroundFish.SIZE / 2, backgroundFish.getX());
        assertEquals("left", backgroundFish.getDirection());

        backgroundFish.swim();
        backgroundFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
        assertEquals(TANK_RIGHT_EDGE - BackgroundFish.SIZE / 2 - BackgroundFish.SPEED,
                backgroundFish.getX());
        assertEquals("left", backgroundFish.getDirection());
    }
}
