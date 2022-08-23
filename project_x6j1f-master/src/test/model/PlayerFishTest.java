package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ScorePanel;

import static org.junit.jupiter.api.Assertions.*;

class PlayerFishTest {
    private static final int TANK_RIGHT_EDGE = GameInTank.WIDTH;
    private static final int TANK_BOTTOM_EDGE = GameInTank.HEIGHT;
    private PlayerFish playerFish;
    private BackgroundFish backgroundFish1;
    private BackgroundFish backgroundFish2;
    private BackgroundFish backgroundFish3;

    @BeforeEach
    void runBefore() {
        playerFish= new PlayerFish(TANK_RIGHT_EDGE / 2, TANK_BOTTOM_EDGE / 2);
        backgroundFish1 = new BackgroundFish(TANK_RIGHT_EDGE / 2, TANK_BOTTOM_EDGE / 2, "up");
        backgroundFish2 = new BackgroundFish(TANK_RIGHT_EDGE / 2 + PlayerFish.SIZE / 4,
                TANK_BOTTOM_EDGE / 2 - PlayerFish.SIZE / 4, "down");
        backgroundFish3 = new BackgroundFish(TANK_RIGHT_EDGE / 2 + PlayerFish.SIZE,
                TANK_BOTTOM_EDGE / 2 + PlayerFish.SIZE, "up");
    }

    @Test
    void testBackgroundFish() {
        assertNotNull(playerFish);
        assertEquals(TANK_RIGHT_EDGE / 2, playerFish.getX());
        assertEquals(TANK_BOTTOM_EDGE / 2, playerFish.getY());
        assertEquals(PlayerFish.DEFAULT_DIRECTION, playerFish.getDirection());
    }

    @Test
    void testSwimVertically() {
        playerFish.setDirection("up");
        assertEquals("up", playerFish.getDirection());

        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2, playerFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2 - i * PlayerFish.SPEED, playerFish.getY());
        }

        playerFish.setDirection("down");
        playerFish.setY(TANK_BOTTOM_EDGE / 2);
        assertEquals("down", playerFish.getDirection());
        assertEquals(TANK_BOTTOM_EDGE / 2, playerFish.getY());

        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2, playerFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2 + i * PlayerFish.SPEED, playerFish.getY());
        }
    }

    @Test
    void testSwimHorizontally() {
        assertEquals(PlayerFish.DEFAULT_DIRECTION, playerFish.getDirection());

        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2 + i * PlayerFish.SPEED, playerFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2, playerFish.getY());
        }

        playerFish.setDirection("left");
        playerFish.setX(TANK_RIGHT_EDGE / 2);
        assertEquals("left", playerFish.getDirection());
        assertEquals(TANK_RIGHT_EDGE / 2, playerFish.getX());

        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            assertEquals(TANK_RIGHT_EDGE / 2 - i * PlayerFish.SPEED, playerFish.getX());
            assertEquals(TANK_BOTTOM_EDGE / 2, playerFish.getY());
        }
    }

    @Test
    void testHandleHitTopTankEdge() {
        int numOfSwim = 3;
        playerFish.setY(ScorePanel.LBL_HEIGHT + numOfSwim * PlayerFish.SPEED);
        assertEquals(ScorePanel.LBL_HEIGHT + numOfSwim * PlayerFish.SPEED, playerFish.getY());

        playerFish.setDirection("up");
        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(ScorePanel.LBL_HEIGHT + (numOfSwim - i) * PlayerFish.SPEED,
                    playerFish.getY());
        }

        for (int i = 1; i <= 2; i++) {
            playerFish.swim();
            assertEquals(ScorePanel.LBL_HEIGHT - PlayerFish.SPEED, playerFish.getY());
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(ScorePanel.LBL_HEIGHT, playerFish.getY());
        }
    }

    @Test
    void testHandleHitBottomTankEdge() {
        int numOfSwim = 3;
        playerFish.setY(TANK_BOTTOM_EDGE - PlayerFish.SIZE / 2 - numOfSwim * PlayerFish.SPEED);
        assertEquals(TANK_BOTTOM_EDGE - PlayerFish.SIZE / 2 - numOfSwim * PlayerFish.SPEED, playerFish.getY());

        playerFish.setDirection("down");
        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(TANK_BOTTOM_EDGE  - PlayerFish.SIZE / 2 - (numOfSwim - i) * PlayerFish.SPEED,
                    playerFish.getY());
        }

        for (int i = 1; i <= 2; i++) {
            playerFish.swim();
            assertEquals(TANK_BOTTOM_EDGE - PlayerFish.SIZE / 2 + PlayerFish.SPEED, playerFish.getY());
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(TANK_BOTTOM_EDGE - PlayerFish.SIZE / 2, playerFish.getY());
        }
    }

    @Test
    void testHandleHitLeftTankEdge() {
        int numOfSwim = 3;
        playerFish.setX(numOfSwim * PlayerFish.SPEED + PlayerFish.SIZE / 2);
        assertEquals(numOfSwim * PlayerFish.SPEED + PlayerFish.SIZE / 2, playerFish.getX());

        playerFish.setDirection("left");
        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals((numOfSwim - i) * PlayerFish.SPEED + PlayerFish.SIZE / 2, playerFish.getX());
            assertEquals("left", playerFish.getDirection());
        }

        for (int i = 1; i <= 2; i++) {
            playerFish.swim();
            assertEquals(PlayerFish.SIZE / 2 - PlayerFish.SPEED, playerFish.getX());
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(PlayerFish.SIZE / 2, playerFish.getX());
        }
    }

    @Test
    void testHandleHitRightTankEdge() {
        int numOfSwim = 3;
        playerFish.setX(TANK_RIGHT_EDGE - PlayerFish.SIZE / 2 - numOfSwim * PlayerFish.SPEED);
        assertEquals(TANK_RIGHT_EDGE - PlayerFish.SIZE / 2 - numOfSwim * PlayerFish.SPEED,
                playerFish.getX());

        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(TANK_RIGHT_EDGE  - PlayerFish.SIZE / 2 - (numOfSwim - i) * PlayerFish.SPEED,
                    playerFish.getX());
            assertEquals("right", playerFish.getDirection());
        }

        for (int i = 1; i <= 3; i++) {
            playerFish.swim();
            assertEquals(TANK_RIGHT_EDGE - PlayerFish.SIZE / 2 + PlayerFish.SPEED, playerFish.getX());
            playerFish.handleHitTankEdge(TANK_RIGHT_EDGE, TANK_BOTTOM_EDGE);
            assertEquals(TANK_RIGHT_EDGE - PlayerFish.SIZE / 2, playerFish.getX());
        }
    }

    @Test
    void testCollideWith() {
        assertTrue(playerFish.collideWith(backgroundFish1));
        assertFalse(playerFish.collideWith(backgroundFish3));
        assertTrue(playerFish.collideWith(backgroundFish2));
    }

/*    @Test
    void testCheckHeadOnCollision() {
        assertTrue(playerFish.checkHeadOnCollision(backgroundFish4));
        assertFalse(playerFish.checkHeadOnCollision(backgroundFish1));

        playerFish.setDirection("left");
        assertTrue(playerFish.checkHeadOnCollision(backgroundFish5));
        assertFalse(playerFish.checkHeadOnCollision(backgroundFish1));

        playerFish.setDirection("up");
        assertTrue(playerFish.checkHeadOnCollision(backgroundFish2));
        assertFalse(playerFish.checkHeadOnCollision(backgroundFish1));

        playerFish.setDirection("down");
        assertTrue(playerFish.checkHeadOnCollision(backgroundFish3));
        assertFalse(playerFish.checkHeadOnCollision(backgroundFish1));
    }

    @Test
    void testCheckUpHeadOnCollision(){
        assertFalse(playerFish.checkUpHeadOnCollision(backgroundFish3));
        assertTrue(playerFish.checkUpHeadOnCollision(backgroundFish2));

        backgroundFish2.setX(TANK_RIGHT_EDGE / 2 + 1);
        assertFalse(playerFish.checkUpHeadOnCollision(backgroundFish2));

        backgroundFish2.setX(TANK_RIGHT_EDGE / 2);
        backgroundFish2.setY(TANK_BOTTOM_EDGE / 2 - 2);
        assertFalse(playerFish.checkUpHeadOnCollision(backgroundFish2));
    }

    @Test
    void testCheckDownHeadOnCollision(){
        assertFalse(playerFish.checkDownHeadOnCollision(backgroundFish4));
        assertTrue(playerFish.checkDownHeadOnCollision(backgroundFish3));

        backgroundFish3.setX(TANK_RIGHT_EDGE / 2 + 1);
        assertFalse(playerFish.checkDownHeadOnCollision(backgroundFish3));

        backgroundFish3.setX(TANK_RIGHT_EDGE / 2);
        backgroundFish3.setY(TANK_BOTTOM_EDGE / 2 - 2);
        assertFalse(playerFish.checkDownHeadOnCollision(backgroundFish3));
    }

    @Test
    void testCheckLeftHeadOnCollision(){
        assertFalse(playerFish.checkLeftHeadOnCollision(backgroundFish4));
        assertTrue(playerFish.checkLeftHeadOnCollision(backgroundFish5));

        backgroundFish5.setY(TANK_BOTTOM_EDGE / 2 + 1);
        assertFalse(playerFish.checkLeftHeadOnCollision(backgroundFish5));

        backgroundFish5.setY(TANK_BOTTOM_EDGE / 2);
        backgroundFish5.setX(TANK_RIGHT_EDGE / 2 - 2);
        assertFalse(playerFish.checkLeftHeadOnCollision(backgroundFish5));
    }

    @Test
    void testCheckRightHeadOnCollision(){
        assertFalse(playerFish.checkRightHeadOnCollision(backgroundFish5));
        assertTrue(playerFish.checkRightHeadOnCollision(backgroundFish4));

        backgroundFish4.setY(TANK_BOTTOM_EDGE / 2 + 1);
        assertFalse(playerFish.checkRightHeadOnCollision(backgroundFish4));

        backgroundFish4.setY(TANK_BOTTOM_EDGE / 2);
        backgroundFish4.setX(TANK_RIGHT_EDGE / 2 - 2);
        assertFalse(playerFish.checkRightHeadOnCollision(backgroundFish4));
    }*/
}